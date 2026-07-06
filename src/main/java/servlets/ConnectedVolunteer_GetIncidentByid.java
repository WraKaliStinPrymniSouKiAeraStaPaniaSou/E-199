package servlets;

import com.google.gson.Gson;
import database.tables.incidents_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class ConnectedVolunteer_GetIncidentByid extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedVolunteer_GetIncidentByid</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedVolunteer_GetIncidentByid at " + request.getContextPath() + "</h1>");
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
            int incident_id = Integer.parseInt(request.getParameter("incident_id"));
            
            incidents_DAO iDAO = new incidents_DAO();
            Map<String, String> IncidentDetails = iDAO.GetIncidentByIncidentId(incident_id);
            
            if (IncidentDetails != null) {
                result.write("{\"success\": true, \"data\": " + new Gson().toJson(IncidentDetails) + "}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.write("{\"success\": false, \"message\": \"Incident not found.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.write("{\"success\": false, \"message\": \"Error occured!\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Error occured!\"}");
        }
        
    }

    @Override
    public String getServletInfo() {
        return "This servlet returns more info about an incident based on an incident_id";
    }
}
