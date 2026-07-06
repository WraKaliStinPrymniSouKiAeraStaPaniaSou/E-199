package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import database.tables.participants_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;

public class admin_AcceptVolunteersRequest extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AcceptVolunteersRequest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AcceptVolunteersRequest at " + request.getContextPath() + "</h1>");
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
            Map<String, String> data = gson.fromJson(JSON.toString(), Map.class);
            
            String volunteer_username = data.get("volunteer_username");
            
            participants_DAO pDAO = new participants_DAO();
            boolean success = pDAO.AcceptVolunteersRequest(volunteer_username);
            if (success) {
                result.write("{\"success\": true, \"message\": \"Volunteer's request accepted successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.write("{\"success\": false, \"message\": \"Volunteer's request has not been found!\"}");
            }
        } catch (JsonSyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.write("{\"success\": false, \"message\": \"Invalid JSON format.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An error occurred.\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet accepts a volunteer's request to become a participant.";
    }
}