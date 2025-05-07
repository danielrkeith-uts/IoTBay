package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import java.io.IOException;

import model.Customer;
//import model.dao.DBManager;

@WebServlet("/ViewAccountServlet")
public class ViewAccountServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Customer customer = (Customer) session.getAttribute("user");

        // You can fetch fresh data from DB if needed here, using a DBManager instance
        // DBManager manager = (DBManager) session.getAttribute("manager");
        // Customer freshCustomer = manager.findCustomerById(customer.getId());
        // request.setAttribute("customer", freshCustomer);

        request.setAttribute("customer", customer);
        request.getRequestDispatcher("view_account.jsp").forward(request, response);
    }
}
