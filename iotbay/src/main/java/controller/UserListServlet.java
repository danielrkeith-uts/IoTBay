package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import model.ExtendedUser;
import model.dao.UserDBManager;

@WebServlet("/UserListServlet")
public class UserListServlet extends HttpServlet {
    private static final String PAGE = "userlist.jsp";
    private static final String ERROR_ATTR = "userListError";
    private Logger logger;

    @Override
    public void init() {
        logger = Logger.getLogger(UserListServlet.class.getName());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UserDBManager userDBManager = (UserDBManager) session.getAttribute("userDBManager");
        
        if (userDBManager == null) {
            throw new ServletException("UserDBManager retrieved from session is null");
        }

        String searchTerm = request.getParameter("search");
        if (searchTerm == null) {
            searchTerm = "";
        }

        try {
            List<ExtendedUser> users = userDBManager.getAllUsersFilteredByName(searchTerm);
            request.setAttribute("users", users);
            request.setAttribute("searchTerm", searchTerm);
            request.getRequestDispatcher(PAGE).forward(request, response);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving users", e);
            session.setAttribute(ERROR_ATTR, "Failed to retrieve user list");
            request.getRequestDispatcher(PAGE).forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
