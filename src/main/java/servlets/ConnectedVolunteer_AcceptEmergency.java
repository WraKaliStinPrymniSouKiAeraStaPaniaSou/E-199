package servlets;

import com.google.gson.Gson;
import database.tables.participants_DAO;
import database.tables.volunteers_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.util.Map;
import mainClasses.Volunteer;

public class ConnectedVolunteer_AcceptEmergency extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedVolunteer_AcceptEmergency</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedVolunteer_AcceptEmergency at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter result = response.getWriter();
        
        try {
            BufferedReader reader = request.getReader();
            StringBuilder JSON = new StringBuilder();
            String Line;
            while ((Line = reader.readLine()) != null) {
                JSON.append(Line);
            }

            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(JSON.toString(), Map.class);
            
            // Take the incident_id from the request
            int incident_id = ((Number) data.get("incident_id")).intValue();
            System.out.println("incident_id: " +incident_id);
             
            // Take the username from the session
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("ConnectedVolunteer") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.write("{\"success\": false, \"message\": \"Invalid volunteer session!\"}");
                return;
            }
            String username = (String) session.getAttribute("ConnectedVolunteer");
            System.out.println("username: " +username);
            
            // Take the volunteer_type through volunteers_DAO
            volunteers_DAO vDAO = new volunteers_DAO();
            Volunteer volunteer = vDAO.GetVolunteerByUsername(username);
            String volunteer_type = volunteer.getVolunteer_type();
            System.out.println("volunteer_type: " +volunteer_type);
            
            // Update the participant 
            participants_DAO pDAO = new participants_DAO();
            boolean success = pDAO.UpdateParticipant(incident_id, username, volunteer_type);
            if (success) {
                result.write("{\"success\": true, \"message\": \"Participant updated successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.write("{\"success\": false, \"message\": \"Failed to update participant.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An error occurred.\"}");
        }   
    }

    @Override
    public String getServletInfo() {
        return "This servlet updates a participant to status 'requested'";
    }
}
