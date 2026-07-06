package servlets;

import database.tables.participants_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ConnectedVolunteer_IsVolunteerBusy extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedVolunteer_IsVolunteerBusy</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedVolunteer_IsVolunteerBusy at " + request.getContextPath() + "</h1>");
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
            // Take the username from the session
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("ConnectedVolunteer") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                result.write("{\"success\": false, \"message\": \"Invalid volunteer session!\"}");
                return;
            }
            String username = (String) session.getAttribute("ConnectedVolunteer");
            System.out.println("username: " +username);
            
            participants_DAO pDAO = new participants_DAO();
            boolean success = pDAO.IsVolunteerBusy(username);
            if (success) {
                result.write("{\"success\": true, \"message\": \"Participant is busy!\"}");
            } else {
                result.write("{\"success\": false, \"message\": \"Participant is not busy.\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An error occurred.\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet looks if the volunteer is busy";
    }
}
