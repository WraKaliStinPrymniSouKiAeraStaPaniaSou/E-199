package servlets;

import com.google.gson.Gson;
import database.tables.participants_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.Participant;

public class admin_GetVolunteersRequests extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet admin_GetVolunteersRequests</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet admin_GetVolunteersRequests at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter result = response.getWriter();
        
        try {
            participants_DAO pDAO = new participants_DAO();
            List<Participant> requests = pDAO.GetALLRequests();
            
            // To JSON form
            Gson gson = new Gson();
            String JSON_form = gson.toJson(requests);
            
            response.setStatus(HttpServletResponse.SC_OK);
            result.write("{\"success\": true, \"data\": " + JSON_form + "}");
        } catch (SQLException ex) {
            Logger.getLogger(admin_GetVolunteersRequests.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\":\"An error occurred while getting the requests.\"}");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(admin_GetVolunteersRequests.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\":\"An error occurred while getting the requests.\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet returns the volunteers' requests to be participants.";
    }
}
