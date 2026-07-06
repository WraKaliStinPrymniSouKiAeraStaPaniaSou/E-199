package servlets;

import com.google.gson.Gson;
import database.tables.users_DAO;
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

public class ConnectedUser_UpdateInfo extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedUser_UpdateInfo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedUser_UpdateInfo at " + request.getContextPath() + "</h1>");
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
            
            
            HttpSession UserSession = request.getSession(false);
            if (UserSession == null || UserSession.getAttribute("ConnectedUser") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.write("{\"success\": false, \"message\": \"Invalid user session!\"}");
                return;
            }
            String username = (String) UserSession.getAttribute("ConnectedUser");
            
            users_DAO userDAO = new users_DAO();
            
            if (edited_data.containsKey("password")) {
                userDAO.UpdatePassword(username, edited_data.get("password"));
            }
            if (edited_data.containsKey("firstname")) {
                userDAO.UpdateFirstname(username, edited_data.get("firstname"));
            }
            if (edited_data.containsKey("lastname")) {
                userDAO.UpdateLastname(username, edited_data.get("lastname"));
            }
            if (edited_data.containsKey("birthdate")) {
                userDAO.UpdateBirthdate(username, edited_data.get("birthdate"));
            }
            if (edited_data.containsKey("gender")) {
                userDAO.UpdateGender(username, edited_data.get("gender"));
            }
            if (edited_data.containsKey("afm")) {
                userDAO.UpdateAfm(username, edited_data.get("afm"));
            }
            if (edited_data.containsKey("country")) {
                userDAO.UpdateCountry(username, edited_data.get("country"));
            }
            if (edited_data.containsKey("address")) {
                userDAO.UpdateAddress(username, edited_data.get("address"));
            }
            if (edited_data.containsKey("municipality")) {
                userDAO.UpdateMunicipality(username, edited_data.get("municipality"));
            }
            if (edited_data.containsKey("prefecture")) {
                userDAO.UpdatePrefecture(username, edited_data.get("prefecture"));
            }
            if (edited_data.containsKey("job")) {
                userDAO.UpdateJob(username, edited_data.get("job"));
            }
            if (edited_data.containsKey("telephone")) {
                System.out.println("Geia");
                userDAO.UpdateTelephone(username, edited_data.get("telephone"));
            }
            if (edited_data.containsKey("lat")) {
                userDAO.UpdateLat(username, Double.valueOf(edited_data.get("lat")));
            }
            if (edited_data.containsKey("lon")) {
                userDAO.UpdateLon(username, Double.valueOf(edited_data.get("lon")));
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
        return "This servlet updates a user's information";
    }
}
