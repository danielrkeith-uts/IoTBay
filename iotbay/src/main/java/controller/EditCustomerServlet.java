package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Customer;
import model.dao.UserDBManager;

@WebServlet("/EditCustomerServlet")
public class EditCustomerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");

            Customer customer = userDBManager.getCustomer(userId); 
            if (customer != null) {
                customer.setFirstName(firstName); 
                customer.setLastName(lastName);
                customer.setPhone(phone);

                userDBManager.updateCustomer(customer); 

                customer = userDBManager.getCustomer(userId);

                request.setAttribute("customer", customer);
                request.setAttribute("success", "Customer updated successfully.");
            } else {
                request.setAttribute("error", "Customer not found.");
            }

            request.getRequestDispatcher("editcustomer.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating customer.");
            request.getRequestDispatcher("editcustomer.jsp").forward(request, response);
        }
    }
}
