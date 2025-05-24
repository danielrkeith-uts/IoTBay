package controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.Card;
import model.Payment;
import model.User;
import model.Enums.PaymentStatus;
import model.dao.PaymentDBManager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
    private static final String ERROR_ATTR    = "paymentError";
    private static final String FORM_PAGE     = "/paymentform.jsp";
    private static final String HISTORY_PAGE  = "/paymenthistory.jsp";

    private Logger logger;
    private PaymentDBManager paymentDBManager;

    @Override
    public void init() throws ServletException {
        super.init();
        logger = Logger.getLogger(PaymentServlet.class.getName());
        try {
            paymentDBManager = new PaymentDBManager(); 
            getServletContext().setAttribute("paymentDBManager", paymentDBManager);
        } catch (Exception e) {
            throw new ServletException("Failed to initialize PaymentDBManager", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (paymentDBManager == null) {
            throw new ServletException("PaymentDBManager not initialized.");
        }
        
        HttpSession session = request.getSession();
    
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");

        // Delete Card action
        if ("deleteCard".equals(action)) {
            String cardIdStr = request.getParameter("cardId");
            try {
                int cardId = Integer.parseInt(cardIdStr);
                paymentDBManager.deleteCard(cardId, user.getUserId());
                session.setAttribute("message", "Card deleted successfully.");
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to delete card", e);
                session.setAttribute("error", "Failed to delete card.");
            }
            response.sendRedirect(request.getContextPath() + "/PaymentServlet");
            return;
        } 
        // Edit (Update) Card action
        else if ("editCard".equals(action)) {
            try {
                int cardId = Integer.parseInt(request.getParameter("cardId"));
                String cardName = request.getParameter("cardName");
                String cardNumber = request.getParameter("cardNumber");
                YearMonth cardExpiry = YearMonth.parse(request.getParameter("cardExpiry"));
                String cardCVC = request.getParameter("cardCVC");

                Card card = new Card(cardId, cardName, cardNumber, cardExpiry, cardCVC);
                paymentDBManager.updateCard(card, user.getUserId());
                session.setAttribute("message", "Card updated successfully.");
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to update card", e);
                session.setAttribute("error", "Failed to update card: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/PaymentServlet");
            return;
        }

        // Make Payment (or add card + pay) action
        try {
            double amount = Double.parseDouble(request.getParameter("amount"));
            
            String cardIdStr = request.getParameter("cardId");
            Card card = null;
            
            if (cardIdStr != null && !cardIdStr.isEmpty()) {
                // User selected a saved card
                int cardId = Integer.parseInt(cardIdStr);
                int cardOwnerId = paymentDBManager.getCardOwner(cardId);
                
                if (cardOwnerId != user.getUserId()) {
                    throw new Exception("Invalid or unauthorized card selected.");
                }

                card = paymentDBManager.getCardById(cardId);
            } else {
                // User entered a new card on the form
                String cardName       = request.getParameter("cardName");
                String cardNumber     = request.getParameter("cardNumber");
                String cardExpiryStr  = request.getParameter("cardExpiry");
                String cardCVC        = request.getParameter("cardCVC");

                // parse YearMonth (expected format: yyyy-MM)
                YearMonth cardExpiry = YearMonth.parse(cardExpiryStr);
                card = new Card(0, cardName, cardNumber, cardExpiry, cardCVC);
            }

            Date date = new Date();
            Payment payment = new Payment(
                    amount,
                    card,
                    PaymentStatus.PENDING,
                    date,
                    user.getUserId()
            );
            paymentDBManager.addPayment(payment);

            // Show payment history on success
            List<Payment> payments = paymentDBManager.getAllPaymentsForUser(user.getUserId());
            request.setAttribute("payments", payments);

            session.removeAttribute(ERROR_ATTR);

            RequestDispatcher rd = request.getRequestDispatcher(HISTORY_PAGE);
            rd.forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing payment", e);
            session.setAttribute(ERROR_ATTR,
                "Something went wrong: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher(FORM_PAGE);
            rd.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (paymentDBManager == null) {
            throw new ServletException("PaymentDBManager not initialized.");
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            try {
                int paymentId = Integer.parseInt(idStr);
                Payment payment = paymentDBManager.getPayment(paymentId);
                if (payment == null || payment.getUserId() != user.getUserId()) {
                    session.setAttribute("error", "Unauthorized payment deletion attempt.");
                } else {
                    paymentDBManager.deletePayment(paymentId);
                    session.setAttribute("message", "Payment deleted successfully.");
                }
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid payment ID.");
            } catch (Exception e) {
                session.setAttribute("error", "Failed to delete payment.");
            }
            
            response.sendRedirect(request.getContextPath() + "/PaymentServlet");
            return;
        }

        // Load saved cards for user to show in payment form
        try {
            List<Card> cards = paymentDBManager.getCardsForUser(user.getUserId());
            request.setAttribute("cards", cards);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading cards", e);
            session.setAttribute(ERROR_ATTR, "Unable to load saved cards: " + e.getMessage());
        }

        // Load payments with optional filtering by status and date range
        String status = request.getParameter("status");
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");

        Date parsedfromDate = null;
        Date parsedtoDate = null;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (fromDateStr != null && !fromDateStr.isEmpty()) {
                parsedfromDate = sdf.parse(fromDateStr);
            }
            if (toDateStr != null && !toDateStr.isEmpty()) {
                parsedtoDate = sdf.parse(toDateStr);
            }
        } catch (Exception e) {
            session.setAttribute("error", "Invalid date format.");
        }

        final Date fromDate = parsedfromDate;
        final Date toDate = parsedtoDate;

        try {
            List<Payment> payments = paymentDBManager.getAllPaymentsForUser(user.getUserId());
            if ((status != null && !status.isEmpty()) || fromDate != null || toDate != null) {
                payments = payments.stream()
                        .filter(p -> (status == null || status.isEmpty() || p.getPaymentStatus().toString().equalsIgnoreCase(status)))
                        .filter(p -> (fromDate == null || !p.getDate().before(fromDate)))
                        .filter(p -> (toDate == null || !p.getDate().after(toDate)))
                        .toList();
            }

            request.setAttribute("payments", payments);
            RequestDispatcher rd = request.getRequestDispatcher(HISTORY_PAGE);
            rd.forward(request, response);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading payments", e);
            session.setAttribute(ERROR_ATTR, "Unable to load payment history: " + e.getMessage());
            RequestDispatcher rd = request.getRequestDispatcher(FORM_PAGE);
            rd.forward(request, response);
        }
    }
}
