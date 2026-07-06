package servlets;

import com.google.gson.Gson;
import database.tables.users_DAO;
import database.tables.volunteers_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.User;
import mainClasses.Volunteer;

public class ConnectedVolunteer_GetInfo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedVolunteer_GetInfo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedVolunteer_GetInfo at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("ConnectedVolunteer");
        
        try {
            volunteers_DAO vDAO = new volunteers_DAO();
            Volunteer volunteer = vDAO.GetVolunteerByUsername(username);

            // Make the volunteer in JSON form and add the (success = true) field in the response
            Gson gson = new Gson();
            String UserJSON = gson.toJson(volunteer);
            String ResponseToJSON = "{\"success\": true, \"data\": " + UserJSON + "}";
            response.getWriter().write(ResponseToJSON);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectedUser_GetInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectedUser_GetInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
