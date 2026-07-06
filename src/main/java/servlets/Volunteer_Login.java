package servlets;

import database.DB_Connection;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Volunteer_Login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Volunteer_Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Volunteer_Login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        
        // Check if the volunteer is already logged in
        HttpSession CurrentSession = request.getSession(false); // return the current session, if there is one
        if (CurrentSession != null && CurrentSession.getAttribute("ConnectedVolunteer") != null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
            response.getWriter().write("{\"success\": false, \"message\": \"Volunteer is logged in!\"}");
            return ;
        } 
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || username.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"username parameter is missing\"}");
            return ;
        } else if (password == null || password.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"password parameter is missing\"}");
            return ;
        }
        
        try (Connection connection = DB_Connection.getConnection()) {
            // Prepare the query and a Statement object for it.
            String query = "SELECT * FROM volunteers WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            
            // Execute the query.
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                String CorrectPassword = results.getString("password");
                if (CorrectPassword.equals(password)) {
                    // Create a session for the user
                    HttpSession VolunteerSession = request.getSession();
                    VolunteerSession.setAttribute("ConnectedVolunteer", username);
                    System.out.println("Session ID: " + VolunteerSession.getId());
                    System.out.println("ConnectedUser: " + VolunteerSession.getAttribute("ConnectedVolunteer"));

                    response.setStatus(HttpServletResponse.SC_OK); // Status 200, OK!
                    response.getWriter().write("{\"success\": true, \"message\": \"Login successful\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Status 401, wrong password!
                    response.getWriter().write("{\"success\": false, \"message\": \"Invalid username or password\"}");
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Status 401, the user doesn't exist!
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid username or password\"}");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("error in try catch!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database driver not found\"}");
        } catch (SQLException e) {
            System.out.println("error in try catch!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Database error\"}");
        } catch (Exception e) {
            System.out.println("error in try catch!");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Unexpected error\"}");
        }   
    }
    
    @Override
    public String getServletInfo() {
        return "This servlet handles the volunteer's login.";
    }
}
