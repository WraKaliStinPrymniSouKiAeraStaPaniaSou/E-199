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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectedVolunteer_GetAvailableEmergencies extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedVolunteer_GetAvailableEmergencies</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedVolunteer_GetAvailableEmergencies at " + request.getContextPath() + "</h1>");
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
            List<Integer> OpenIncidents = pDAO.GetOpenIncidentIds();
        
            Gson gson = new Gson();
            Map<String, Object> jsonResponse = new HashMap<>();
            jsonResponse.put("success", true);
            jsonResponse.put("data", OpenIncidents);

            String JSON = gson.toJson(jsonResponse);
            result.write(JSON);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectedVolunteer_GetAvailableEmergencies.class.getName()).log(Level.SEVERE, null, ex);
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Gson gson = new Gson();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("message", "An error occurred!!");

            String JSON = gson.toJson(errorResponse);
            result.write(JSON);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectedVolunteer_GetAvailableEmergencies.class.getName()).log(Level.SEVERE, null, ex);

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Gson gson = new Gson();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("success", "false");
            errorResponse.put("message", "An error occurred!!");

            String JSON = gson.toJson(errorResponse);
            result.write(JSON);
        }
    }


    @Override
    public String getServletInfo() {
        return "This servlet returns the incidents they want volunteers for.";
    }
}
