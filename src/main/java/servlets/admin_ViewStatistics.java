package servlets;

import database.tables.incidents_DAO;
import database.tables.users_DAO;
import database.tables.volunteers_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class admin_ViewStatistics extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet admin_ViewStatistics</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet admin_ViewStatistics at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter result = response.getWriter();
        
        try {
            // Incidents count per type
            incidents_DAO iDAO = new incidents_DAO();
            int FloodCount = iDAO.GetFloodCount();
            int EarthquakeCount = iDAO.EarthquakeCount();
            int FireCount = iDAO.GetAccidentCount();
            int AccidentCount = iDAO.GetAccidentCount();
            
            // Total firemen and vehicles
            int VehiclesCount = iDAO.GetVehiclesCount();
            int FiremenCount = iDAO.GetFiremenCount();
            
            // users count
            users_DAO uDAO = new users_DAO();
            int UsersCount = uDAO.UsersCount();
            
            volunteers_DAO vDAO = new volunteers_DAO();
            int VolunteersCount = vDAO.GetVolunteerCount();
            
            // JSON response 
            result.write("{\"success\":true,\"data\":{"
                    + "\"flood\":" + FloodCount + ","
                    + "\"earthquake\":" + EarthquakeCount + ","
                    + "\"fire\":" + FireCount + ","
                    + "\"accident\":" + AccidentCount + ","
                    + "\"vehicles\":" + VehiclesCount + ","
                    + "\"firemen\":" + FiremenCount + ","
                    + "\"users\":" + UsersCount + ","
                    + "\"volunteers\":" + VolunteersCount
                    + "}}");
        } catch (SQLException ex) {
            Logger.getLogger(admin_ViewStatistics.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An error occurred while getting statistics.\"}");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(admin_ViewStatistics.class.getName()).log(Level.SEVERE, null, ex);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"An error occurred while getting statistics.\"}");
        }
    }
} 

