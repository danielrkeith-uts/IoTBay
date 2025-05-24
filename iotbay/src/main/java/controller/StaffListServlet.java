package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import model.Staff;
import model.User;
import model.dao.UserDBManager;

@WebServlet("/StaffListServlet")
public class StaffListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        UserDBManager mgr = (UserDBManager) session.getAttribute("userDBManager");
        User current     = (User) session.getAttribute("user");

       
        if (mgr == null) {
            req.setAttribute("errorMessage", "Database unavailable.");
            req.getRequestDispatcher("/error.jsp")
               .forward(req, resp);
            return;
        }
        if (current == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        
        String q        = req.getParameter("q");         
        String position = req.getParameter("position");  

        try {
            List<Staff> staffList;
            if ((q != null && !q.trim().isEmpty())
             || (position != null && !"ALL".equals(position))) {
                
                staffList = mgr.searchStaff(q, position);
            } else {
                staffList = mgr.getAllStaff();
            }

            
            req.setAttribute("q",         q);
            req.setAttribute("position",  position);
            req.setAttribute("staffList", staffList);

            req.getRequestDispatcher("/stafflist.jsp")
               .forward(req, resp);

        } catch (SQLException e) {
            req.setAttribute("errorMessage","Could not load staff.");
            req.getRequestDispatcher("/error.jsp")
               .forward(req, resp);
        }
    }
}



