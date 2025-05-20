package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.UserDBManager;

@WebServlet("/reactivateCustomer")
public class ReactivateCustomerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");

        try {
            int userId = Integer.parseInt(request.getParameter("id"));
            userDBManager.reactivateCustomer(userId); 
            response.sendRedirect("customerlist.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
