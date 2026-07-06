package servlets;

import database.DB_Connection;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User_CheckEmail extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Volunteer_CheckEmail</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Volunteer_CheckEmail at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // The response is in JSON form.
        response.setContentType("application/json");
        
        // Take the email from the query.
        String email = request.getParameter("email");
        
        if (email == null || email.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Email parameter is missing\"}");
            return ;
        }
        
        try (Connection connection = DB_Connection.getConnection()) {
            // Prepare the query and a Statement object for it.
            String query = "SELECT COUNT(*) AS count_emails FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            
            // Execute the query.
            ResultSet results = statement.executeQuery();
            results.next();
            
            // Counts how many users there are with the email the user gave.
            int count_emails = results.getInt("count_emails");
            System.out.println("Count from DB: " + count_emails);
            if (count_emails > 0) {
                response.getWriter().write("{\"available\": false}");
            } else {
                response.getWriter().write("{\"available\": true}");
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
        return "This servlet checks if the email is unique in the database";
    }
}
