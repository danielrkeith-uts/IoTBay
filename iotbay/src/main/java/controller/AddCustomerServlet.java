package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Customer;
import model.dao.UserDBManager;
import utils.Validator;
import model.exceptions.InvalidInputException;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/AddCustomerServlet")
public class AddCustomerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        if (userDBManager == null) {
            request.setAttribute("error", "Database error.");
            request.getRequestDispatcher("addcustomer.jsp").forward(request, response);
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String typeParam = request.getParameter("type");

        try {
            if (typeParam == null || typeParam.isEmpty()) {
                throw new InvalidInputException("Customer type is required.");
            }

            Customer.Type type = Customer.Type.valueOf(typeParam.toUpperCase());
            Customer customer = new Customer(-1, firstName, lastName, email, phone, password, type);
            Validator.validateUser(customer);

            if (userDBManager.userExists(email)) {
                throw new InvalidInputException("User with that email already exists.");
            }

            userDBManager.addCustomer(customer);
            request.setAttribute("success", "Customer added successfully.");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid customer type.");
        } catch (InvalidInputException e) {
            request.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            request.setAttribute("error", "Failed to add customer to database.");
            e.printStackTrace();
        }

        request.getRequestDispatcher("addcustomer.jsp").forward(request, response);
    }
}
