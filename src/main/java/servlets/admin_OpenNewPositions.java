package servlets;

import com.google.gson.Gson;
import database.tables.participants_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;

public class admin_OpenNewPositions extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet admin_OpenNewPositions</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet admin_OpenNewPositions at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            int positions = Integer.parseInt(data.get("positions").toString());
                
            participants_DAO pDAO = new participants_DAO();
            boolean success = pDAO.AddParticipantsToDatabase(incident_id, positions);
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": true, \"message\": \"The participants were updated successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Something went wrong with the insertion!\"}");
            }    
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Error occurred while inserting the job positions for the participants.\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet adds new participants' positions";
    }
}
