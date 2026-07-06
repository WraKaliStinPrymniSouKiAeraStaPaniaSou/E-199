package servlets;

import database.init.InitDatabase;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class InitDB extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet InitDB</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet InitDB at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
   
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {            
            response.setContentType("text/html;charset=UTF-8");
            InitDatabase init = new InitDatabase();
            init.initDatabase();
            init.initTables();
            init.addToDatabaseExamples();
            init.updateRecords();
            init.databaseToJSON();
            
            // Set status 200!
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException ex) {
            response.sendError(500, "InitDB failed: " + ex.getMessage());
            Logger.getLogger(InitDB.class.getName()).log(Level.SEVERE, "InitDB SQL error", ex);
        } catch (ClassNotFoundException ex) {
            response.sendError(500, "InitDB failed: " + ex.getMessage());
            Logger.getLogger(InitDB.class.getName()).log(Level.SEVERE, "InitDB class error", ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Initialize Database";
    }
}
