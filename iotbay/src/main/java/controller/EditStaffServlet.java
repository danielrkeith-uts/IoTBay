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

@WebServlet("/EditStaffServlet")
public class EditStaffServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDBManager mgr = (UserDBManager) session.getAttribute("userDBManager");

        String userIdParam = req.getParameter("userId");
        if (mgr == null || userIdParam == null) {
            resp.sendRedirect("StaffListServlet");
            return;
        }

        try {
            int userId = Integer.parseInt(userIdParam);
            Staff staff = (Staff) mgr.getUser(userId);
            if (staff == null) {
                req.setAttribute("error", "Staff not found");
                req.getRequestDispatcher("stafflist.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("staff", staff);
            req.getRequestDispatcher("editstaff.jsp").forward(req, resp);
        } catch (NumberFormatException|SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserDBManager mgr = (UserDBManager) session.getAttribute("userDBManager");

        try {
            int userId      = Integer.parseInt(req.getParameter("userId"));
            String fn       = req.getParameter("firstName");
            String ln       = req.getParameter("lastName");
            String email    = req.getParameter("email");
            String phone    = req.getParameter("phone");
            String password = req.getParameter("password");
            String staffCardRaw = req.getParameter("staffCardId");
            String position = req.getParameter("position");

            Staff staff = (Staff) mgr.getUser(userId);
            if (staff == null) {
                throw new SQLException("Staff not found");
            }

            // update fields
            staff.setFirstName(fn);
            staff.setLastName(ln);
            staff.setEmail(email);
            staff.setPhone(phone);
            staff.setPassword(password);
            staff.setPosition(position);

            // validations
            Validator.validateUser(staff);
            int newCardId = Validator.validateStaffCardId(staffCardRaw);
            staff.setStaffCardId(newCardId);

            // commit
            mgr.updateStaff(staff);

            req.setAttribute("success", "Staff updated successfully.");
            req.setAttribute("staff", staff);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid number format.");
        } catch (InvalidInputException e) {
            req.setAttribute("error", e.getMessage());
        } catch (SQLException e) {
            req.setAttribute("error", "Database error: could not update staff.");
        }

        req.getRequestDispatcher("editstaff.jsp").forward(req, resp);
    }
}



