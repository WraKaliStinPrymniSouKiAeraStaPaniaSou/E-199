package servlets;

import com.google.gson.Gson;
import database.tables.messages_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mainClasses.SpecialMessage;

public class ConnectedUser_GetActiveIncidentsAndExtra extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedUser_GetActiveIncidentsAndExtra</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedUser_GetActiveIncidentsAndExtra at " + request.getContextPath() + "</h1>");
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
            messages_DAO MessagesDAO = new messages_DAO();
            List<SpecialMessage> Messages = MessagesDAO.getMessageForAI();
                  
            // To JSON form
            Gson gson = new Gson();
            String JSON_form = gson.toJson(Messages);
            
            response.setStatus(HttpServletResponse.SC_OK);
            result.write("{\"success\": true, \"data\": " + JSON_form + "}");
        } catch (SQLException ex) {
            Logger.getLogger(ConnectedUser_ViewALLIncidents.class.getName()).log(Level.SEVERE, null, ex);
            result.write("{\"success\": false, \"message\": \"Database error occurred!\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectedUser_ViewALLIncidents.class.getName()).log(Level.SEVERE, null, ex);
            result.write("{\"success\": false, \"message\": \"Server error occurred!\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            Logger.getLogger(ConnectedUser_ViewALLIncidents.class.getName()).log(Level.SEVERE, null, ex);
            result.write("{\"success\": false, \"message\": \"Unknown error occurred!" + ex.getMessage() + "\"}");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            result.close();
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet returns the active incidents' info and more to the user.";
    }
}
