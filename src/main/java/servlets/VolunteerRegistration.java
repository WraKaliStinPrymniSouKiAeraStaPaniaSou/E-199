package servlets;

import database.tables.volunteers_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;
import mainClasses.Volunteer;
import com.google.gson.Gson;

public class VolunteerRegistration extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet VolunteerRegistration</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VolunteerRegistration at " + request.getContextPath() + "</h1>");
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
            System.out.println("JSON: " +JSON.toString());
            
            // From JSON to Map
            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(JSON.toString(), Map.class);
            System.out.println("Map: " + data);
            
            Volunteer volunteer = new Volunteer();
            volunteer.setUsername(data.get("username").toString());
            volunteer.setEmail(data.get("email").toString());
            volunteer.setPassword(data.get("password").toString());
            volunteer.setFirstname(data.get("firstname").toString());
            volunteer.setLastname(data.get("lastname").toString());
            volunteer.setBirthdate(data.get("birthdate").toString());
            volunteer.setGender(data.get("gender").toString());
            volunteer.setAfm(data.get("afm").toString());
            volunteer.setCountry(data.get("country").toString());
            volunteer.setAddress(data.get("address").toString());
            volunteer.setMunicipality(data.get("municipality").toString());
            volunteer.setPrefecture(data.get("prefecture").toString());
            volunteer.setJob(data.get("job").toString());
            volunteer.setTelephone(data.get("telephone").toString());
            volunteer.setLat(Double.valueOf(data.get("lat").toString()));
            volunteer.setLon(Double.valueOf(data.get("lon").toString()));
            volunteer.setVolunteer_type(data.get("volunteer_type").toString());
            volunteer.setHeight(Double.valueOf(data.get("height").toString()));
            volunteer.setWeight(Double.valueOf(data.get("weight").toString()));

            volunteers_DAO VolunteerDAO = new volunteers_DAO();
            boolean success = VolunteerDAO.InsertVolunteer(volunteer);
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                result.write("{\"success\": true, \"message\": \"Volunteer registered successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.write("{\"success\": false, \"message\": \"Failed to register volunteer!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Error in server occurred!\"}");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "This servlet registers a firefighter volunteer in database";
    }
}
