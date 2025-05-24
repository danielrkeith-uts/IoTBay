package controller;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Staff;
import model.dao.UserDBManager;
import utils.Validator;
import model.exceptions.InvalidInputException;

@WebServlet("/AddStaffServlet")
public class AddStaffServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session       = req.getSession();
        UserDBManager mgr         = (UserDBManager) session.getAttribute("userDBManager");

        String firstName          = req.getParameter("firstName");
        String lastName           = req.getParameter("lastName");
        String email              = req.getParameter("email");
        String phone              = req.getParameter("phone");
        String password           = req.getParameter("password");
        String staffCardIdParam   = req.getParameter("staffCardId");
        String position           = req.getParameter("position");
        boolean isAdmin           = false;                           

        try {
            int staffCardId = Validator.validateStaffCardId(staffCardIdParam);

            Validator.validateUser(new Staff(
                -1, firstName, lastName, email, phone, password,
                staffCardId, isAdmin, position
            ));

            Staff staff = new Staff(
                -1, firstName, lastName, email, phone, password,
                staffCardId, isAdmin, position
            );

            mgr.addStaff(staff);

            req.setAttribute("success", "Staff member created!");
        } catch (InvalidInputException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Database error: could not add staff.");
        }

        req.getRequestDispatcher("addstaff.jsp").forward(req, resp);
    }
}


