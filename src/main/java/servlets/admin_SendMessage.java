package servlets;

import com.google.gson.Gson;
import database.tables.messages_DAO;
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
import mainClasses.Message;

public class admin_SendMessage extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedUser_SendMessage</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedUser_SendMessage at " + request.getContextPath() + "</h1>");
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
            System.out.println("JSON Received: " + JSON.toString());

             // From JSON to Map
            Gson gson = new Gson();
            Map<String, String> data = gson.fromJson(JSON.toString(), Map.class);
            System.out.println("Data Map: " + data);

            Message msg = new Message();
            msg.setIncident_id(Integer.parseInt(data.get("incident_id")));
            msg.setMessage((String) data.get("message"));
            msg.setSender("admin");
            msg.setRecipient("public");
            msg.setDate_time();
            
            System.out.println("Message Object: " + msg);

            messages_DAO messageDAO = new messages_DAO();
            boolean success = messageDAO.AddMessageToDatabase(msg);

            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                result.write("{\"success\": true, \"message\": \"The message added to the database successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                result.write("{\"success\": false, \"message\": \"The message already exists!\"}");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Database error occurred!\"}");
            System.out.println("SQL Exception");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Server error occurred! Driver not found!\"}");
            System.out.println("Class Not Found Exception");
        } catch (IOException ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"I/O error occurred!\"}");
            System.out.println("IO Exception");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An unexpected error occurred: " + ex.getMessage() + "\"}");
            System.out.println("Exception!");
        } finally {
            result.close();
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet adds a message to the database";
    }
}
