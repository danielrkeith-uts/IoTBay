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
        HttpSession session = req.getSession(false);
        
        if (session == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");
        PaymentDBManager paymentDBManager = (PaymentDBManager) session.getAttribute("paymentDBManager");

        if (action == null) action = "showForm";

        try {
            switch (action) {
                case "showForm":
                    if (cart == null) {
                        resp.sendRedirect("cart.jsp");
                        return;
                    }

                    // Set amount from cart total
                    req.setAttribute("amount", cart.totalCost());

                    // Get saved payment methods if user is logged in
                    if (user != null) {
                        List<Card> methods = paymentDBManager.getMethodsByUser(user.getUserId());
                        req.setAttribute("paymentMethods", methods);
                    }
                    
                    req.getRequestDispatcher("/paymentform.jsp").forward(req, resp);
                    break;

                case "viewHistory":
                    if (user == null) {
                        resp.sendRedirect("login.jsp");
                        return;
                    }
                    List<Payment> payments = paymentDBManager.getAllPayments()
                        .stream()
                        .filter(p -> p.getUserId() == user.getUserId())
                        .collect(Collectors.toList());
                    req.setAttribute("payments", payments);
                    req.getRequestDispatcher("paymenthistory.jsp").forward(req, resp);
                    break;

                case "editMethod":
                    if (user == null) {
                        resp.sendRedirect("login.jsp");
                        return;
                    }
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
                    if (user == null) {
                        resp.sendRedirect("login.jsp");
                        return;
                    }
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
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

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
                    
                    if (save) {
                        cardId = paymentDBManager.insertPaymentMethod(user.getUserId(), card);
                        card.setCardId(cardId);
                    }

                    Payment payment = new Payment(
                        0,
                        user.getUserId(),
                        amount,
                        card,
                        PaymentStatus.PENDING
                    );
                    paymentDBManager.insertPayment(payment);

                    // Clear cart after successful payment
                    session.removeAttribute("cart");
                    
                    resp.sendRedirect("confirmation.jsp");
                    break;

                case "updateMethod":
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
