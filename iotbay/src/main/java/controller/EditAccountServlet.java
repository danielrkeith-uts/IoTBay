package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import java.io.IOException;

import model.Customer;
import model.dao.UserDBManager;

@WebServlet("/EditAccountServlet")
public class EditAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Customer customer = (Customer) session.getAttribute("user");
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("edit_account.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        Customer customer = (Customer) session.getAttribute("user");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        // Update customer object
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);

        // Save changes to the DB
        try {
            UserDBManager manager = (UserDBManager) session.getAttribute("manager");
            manager.updateCustomer(customer);

            // Update session with fresh customer info
            session.setAttribute("user", customer);
            response.sendRedirect("ViewAccountServlet");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to update account details.");
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("edit_account.jsp").forward(request, response);
        }
    }
}
