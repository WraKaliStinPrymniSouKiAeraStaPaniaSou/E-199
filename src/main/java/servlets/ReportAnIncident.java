package servlets;

import com.google.gson.Gson;
import database.tables.incidents_DAO;
import database.tables.participants_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.util.Map;
import mainClasses.Incident;

public class ReportAnIncident extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Guest_ReportAnIncident</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Guest_ReportAnIncident at " + request.getContextPath() + "</h1>");
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
            HttpSession session = request.getSession(false);
            String user_type = "guest";     // Default user_type
            String status = "submitted";    // Default status
            
            // check the user_type
            if (session != null) {
                if (session.getAttribute("ConnectedUser") != null) {
                    user_type = "user";
                } else if (session.getAttribute("ConnectedAdmin") != null) {
                    user_type = "admin";
                    status = "running";
                }
            }
            
            System.out.println("user_type: " + user_type);
            
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
            
            // Create an incident
            Incident incident = new Incident();
            incident.setIncident_type((String) data.get("incident_type"));
            incident.setDescription(data.getOrDefault("description", "").toString());
            incident.setUser_phone(data.getOrDefault("user_phone", "").toString());
            incident.setUser_type(user_type);
            incident.setAddress((String) data.get("address"));

            Object latObj = data.get("lat");
            Object lonObj = data.get("lon");

            if (latObj instanceof Number && lonObj instanceof Number) {
                incident.setLat(((Number) latObj).doubleValue());
                incident.setLon(((Number) lonObj).doubleValue());
            } else {
                throw new IllegalArgumentException("Latitude or Longitude must be valid numbers.");
            }

            incident.setMunicipality(data.getOrDefault("municipality", "").toString());
            incident.setPrefecture(data.getOrDefault("prefecture", "").toString());
            incident.setStart_datetime();
            //incident.setEnd_datetime(null);
            incident.setDanger((String) data.get("danger"));
            incident.setStatus(status);
            incident.setFinalResult(null);
            incident.setVehicles(0);
            incident.setFiremen(0);
            System.out.println("Inserting Incident: " + incident);
            
            incidents_DAO incidentDAO = new incidents_DAO();
            int incident_id = incidentDAO.InsertIncident(incident);
            
            participants_DAO pDAO = new participants_DAO();
            pDAO.AddParticipantsToDatabase(incident_id, 3);
            
            response.setStatus(HttpServletResponse.SC_OK);
            result.write("{\"success\": true, \"message\": \"Incident reported successfully!\"}");
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Database error occurred!\"}");
            System.out.println("SQL Exception: " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Database driver not found!\"}");
            System.out.println("Class Not Found Exception: " + ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"I/O error occurred!\"}");
            System.out.println("I/O Exception: " + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.write("{\"success\": false, \"message\": \"" + ex.getMessage() + "\"}");
            System.out.println("Invalid Input: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An unexpected error occurred: " + ex.getMessage() + "\"}");
            System.out.println("Unexpected Exception: " + ex.getMessage());
        } finally {
            result.close();
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet adds an incident to the database";
    }
}
