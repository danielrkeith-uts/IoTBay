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

@WebServlet("/LoadEditCustomerServlet")
public class LoadEditCustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            Customer customer = userDBManager.getCustomer(userId); 

            if (customer == null) {
                request.setAttribute("error", "Customer not found.");
            } else {
                request.setAttribute("customer", customer); 
            }

            request.getRequestDispatcher("editcustomer.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Unable to load customer.");
            request.getRequestDispatcher("editcustomer.jsp").forward(request, response); 
        }
    }
}
