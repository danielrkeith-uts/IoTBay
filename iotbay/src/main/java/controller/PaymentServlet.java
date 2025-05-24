package controller;

import model.Payment;
import model.Card;
import model.Cart;
import model.User;
import model.Enums.PaymentStatus;
import model.dao.PaymentDBManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(true);
        
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");
        PaymentDBManager paymentDBManager = (PaymentDBManager) session.getAttribute("paymentDBManager");

        if (paymentDBManager == null) {
            throw new ServletException("PaymentDBManager not found in session");
        }

        if (user == null && (action != null && !action.equals("showForm"))) {
            session.setAttribute("redirectAfterLogin", "PaymentServlet?action=" + action);
            resp.sendRedirect("login.jsp");
            return;
        }

        if (action == null) action = "showForm";

        try {
            switch (action) {
                case "showForm":
                    if (cart == null) {
                        resp.sendRedirect("cart.jsp");
                        return;
                    }

                    req.setAttribute("amount", cart.totalCost());

                    if (user != null) {
                        List<Card> methods = paymentDBManager.getMethodsByUser(user.getUserId());
                        req.setAttribute("paymentMethods", methods);
                    }
                    
                    req.getRequestDispatcher("/paymentform.jsp").forward(req, resp);
                    break;

                case "viewHistory":
                    List<Payment> payments = paymentDBManager.getAllPayments()
                        .stream()
                        .filter(p -> p.getUserId() == user.getUserId())
                        .collect(Collectors.toList());
                    req.setAttribute("payments", payments);
                    req.getRequestDispatcher("paymenthistory.jsp").forward(req, resp);
                    break;

                case "editMethod":
                    String cardId = req.getParameter("cardId");
                    if (cardId != null) {
                        Card card = paymentDBManager.getPaymentMethod(Integer.parseInt(cardId));
                        if (card != null) {
                            req.setAttribute("editCard", card);
                            req.getRequestDispatcher("/paymentform.jsp").forward(req, resp);
                            return;
                        }
                    }
                    resp.sendRedirect("PaymentServlet?action=showForm");
                    break;

                case "deleteMethod":
                    String deleteId = req.getParameter("cardId");
                    if (deleteId != null) {
                        paymentDBManager.deletePaymentMethod(Integer.parseInt(deleteId));
                    }
                    resp.sendRedirect("PaymentServlet?action=showForm");
                    break;

                default:
                    resp.sendRedirect("PaymentServlet?action=showForm");
                    break;
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        User user = (User) session.getAttribute("user");
        PaymentDBManager paymentDBManager = (PaymentDBManager) session.getAttribute("paymentDBManager");
        String action = req.getParameter("action");

        try {
            switch (action) {
                case "createPayment":
                    double amount = Double.parseDouble(req.getParameter("amount"));
                    String name = req.getParameter("name");
                    String number = req.getParameter("cardNumber");
                    String expiryStr = req.getParameter("expiry");
                    YearMonth expiry = YearMonth.parse(expiryStr);
                    String cvv = req.getParameter("cvv");
                    boolean save = req.getParameter("saveMethod") != null;

                    Card card = new Card(0, name, number, expiry, cvv);
                    int cardId = 0;
                    
                    if (user != null && save) {
                        cardId = paymentDBManager.insertPaymentMethod(user.getUserId(), card);
                        card.setCardId(cardId);
                    }

                    Payment payment = new Payment(
                        0,
                        user != null ? user.getUserId() : 0,
                        amount,
                        card,
                        PaymentStatus.PENDING
                    );
                    paymentDBManager.insertPayment(payment);

                    session.removeAttribute("cart");
                    
                    resp.sendRedirect("confirmation.jsp");
                    break;

                case "updateMethod":
                    if (user == null) {
                        resp.sendRedirect("login.jsp");
                        return;
                    }
                    String updateId = req.getParameter("cardId");
                    if (updateId != null) {
                        Card updatedCard = new Card(
                            Integer.parseInt(updateId),
                            req.getParameter("name"),
                            req.getParameter("cardNumber"),
                            YearMonth.parse(req.getParameter("expiry")),
                            req.getParameter("cvv")
                        );
                        paymentDBManager.updatePaymentMethod(updatedCard);
                    }
                    resp.sendRedirect("PaymentServlet?action=showForm");
                    break;

                default:
                    resp.sendRedirect("PaymentServlet?action=showForm");
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
