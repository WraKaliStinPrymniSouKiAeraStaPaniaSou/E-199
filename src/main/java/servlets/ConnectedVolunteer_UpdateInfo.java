package servlets;

import com.google.gson.Gson;
import database.tables.volunteers_DAO;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectedVolunteer_UpdateInfo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedVolunteer_UpdateInfo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedVolunteer_UpdateInfo at " + request.getContextPath() + "</h1>");
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
            BufferedReader reader = request.getReader();
            StringBuilder JSON = new StringBuilder();
            String Line;
            while ((Line = reader.readLine()) != null) {
                JSON.append(Line);
            }

            Gson gson = new Gson();
            Map<String, String> edited_data = gson.fromJson(JSON.toString(), Map.class);
            System.out.println("Map: " +edited_data);
            
            HttpSession UserSession = request.getSession(false);
            if (UserSession == null || UserSession.getAttribute("ConnectedVolunteer") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.write("{\"success\": false, \"message\": \"Invalid volunteer session!\"}");
                return;
            }
            String username = (String) UserSession.getAttribute("ConnectedVolunteer");
            
            volunteers_DAO vDAO = new volunteers_DAO();
            
            if (edited_data.containsKey("password")) {
                vDAO.UpdatePassword(username, edited_data.get("password"));
            }
            if (edited_data.containsKey("firstname")) {
                vDAO.UpdateFirstname(username, edited_data.get("firstname"));
            }
            if (edited_data.containsKey("lastname")) {
                vDAO.UpdateLastname(username, edited_data.get("lastname"));
            }
            if (edited_data.containsKey("birthdate")) {
                vDAO.UpdateBirthdate(username, edited_data.get("birthdate"));
            }
            if (edited_data.containsKey("gender")) {
                vDAO.UpdateGender(username, edited_data.get("gender"));
            }
            if (edited_data.containsKey("afm")) {
                vDAO.UpdateAfm(username, edited_data.get("afm"));
            }
            if (edited_data.containsKey("country")) {
                vDAO.UpdateCountry(username, edited_data.get("country"));
            }
            if (edited_data.containsKey("address")) {
                vDAO.UpdateAddress(username, edited_data.get("address"));
            }
            if (edited_data.containsKey("municipality")) {
                vDAO.UpdateMunicipality(username, edited_data.get("municipality"));
            }
            if (edited_data.containsKey("prefecture")) {
                vDAO.UpdatePrefecture(username, edited_data.get("prefecture"));
            }
            if (edited_data.containsKey("job")) {
                vDAO.UpdateJob(username, edited_data.get("job"));
            }
            if (edited_data.containsKey("telephone")) {
                vDAO.UpdateTelephone(username, edited_data.get("telephone"));
            }
            if (edited_data.containsKey("category")) {
                System.out.println("Geia");
                vDAO.UpdateVolunteer_Type(username, edited_data.get("category"));
                System.out.println("Geia");
            }
            if (edited_data.containsKey("height")) {
                vDAO.UpdateHeight(username, Double.valueOf(edited_data.get("height")));
            }
            if (edited_data.containsKey("weight")) {
                vDAO.UpdateWeight(username, Double.valueOf(edited_data.get("weight")));
            }
            
            if (edited_data.containsKey("lat")) {
                vDAO.UpdateLat(username, Double.valueOf(edited_data.get("lat")));
            }
            if (edited_data.containsKey("lon")) {
                vDAO.UpdateLon(username, Double.valueOf(edited_data.get("lon")));
            }

            response.setStatus(HttpServletResponse.SC_OK);
            result.write("{\"success\": true, \"message\": \"User's information updated successfully!\"}");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectedUser_UpdateInfo.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Error updating user's information!\"}");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectedUser_UpdateInfo.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Error updating user's information!\"}");
        } finally {
            result.close();
        } 
    }

    @Override
    public String getServletInfo() {
        return "This servlet updates a volunteer's information";
    }
}
