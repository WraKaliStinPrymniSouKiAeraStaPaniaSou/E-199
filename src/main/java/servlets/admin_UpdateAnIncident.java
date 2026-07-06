package servlets;

import com.google.gson.Gson;
import database.tables.incidents_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;

public class admin_UpdateAnIncident extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet admin_UpdateAnIncident</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet admin_UpdateAnIncident at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        PrintWriter result = response.getWriter();
        
        try {
            // Take the data from the request
            BufferedReader reader = request.getReader();
            StringBuilder JSON = new StringBuilder();
            String Line;
            while ((Line = reader.readLine()) != null) {
                JSON.append(Line);
            }
            System.out.println("JSON: " +JSON.toString());
            
            // From JSON to Map
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(JSON.toString(), Map.class);
            System.out.println("Map: " + data);
            
            // take the data
            int incident_id = Integer.parseInt(data.get("incident_id").toString());
            System.out.println("incident_id: " + incident_id);

            String status = data.get("status").toString();
            System.out.println("status: " + status);

            String danger = data.get("danger").toString();
            System.out.println("danger: " + danger);
            
            int vehicles = Integer.parseInt(data.get("vehicles").toString());
            System.out.println("vehicles: " + vehicles);

            int firemen = Integer.parseInt(data.get("firemen").toString());
            System.out.println("firemen: " + firemen);

            incidents_DAO IncidentsDAO = new incidents_DAO();
            boolean success = IncidentsDAO.UpdateIncident(incident_id, status, danger, vehicles, firemen);
            
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true, \"message\": \"The incident was updated successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"The incident was not found!\"}");
            }    
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Error occurred while updating the incident.\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet updates an incident's info that the admin changed";
    }
}
