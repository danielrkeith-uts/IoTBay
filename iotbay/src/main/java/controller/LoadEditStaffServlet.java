package controller;

import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Staff;
import model.dao.UserDBManager;

@WebServlet("/LoadEditStaffServlet")
public class LoadEditStaffServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    HttpSession session = req.getSession();
    UserDBManager mgr = (UserDBManager) session.getAttribute("userDBManager");
    String idParam = req.getParameter("userId");

    if (mgr == null || idParam == null) {
      resp.sendRedirect("StaffListServlet");
      return;
    }

    try {
      int userId = Integer.parseInt(idParam);
      Staff staff = (Staff) mgr.getUser(userId);
      if (staff == null) {
        // not found → back to list
        resp.sendRedirect("StaffListServlet");
        return;
      }
      req.setAttribute("staff", staff);
      req.getRequestDispatcher("/editstaff.jsp").forward(req, resp);
    } catch (NumberFormatException | SQLException e) {
      resp.sendRedirect("StaffListServlet");
    }
  }
}


