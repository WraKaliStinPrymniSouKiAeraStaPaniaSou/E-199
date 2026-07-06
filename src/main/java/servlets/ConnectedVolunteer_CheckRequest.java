package servlets;

import database.tables.participants_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mainClasses.Participant;

public class ConnectedVolunteer_CheckRequest extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedVolunteer_CheckRequest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedVolunteer_CheckRequest at " + request.getContextPath() + "</h1>");
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
            HttpSession session = request.getSession(false);
            String volunteer_username = (String) session.getAttribute("ConnectedVolunteer");
            
            participants_DAO pDAO = new participants_DAO();
            Participant participant = pDAO.GetParticipationForVolunteer(volunteer_username);
            
            if (participant != null) {

                result.write("{\"success\": true, \"incident_id\": " + participant.getIncident_id() + "}");
            } else {
                result.write("{\"success\": false, \"message\": \"No accepted participation found.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An error occurred during check.\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet checks if the volunteer has been accepted.";
    }
}
