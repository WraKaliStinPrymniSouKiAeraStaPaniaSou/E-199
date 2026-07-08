package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class Admin_Login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Admin_Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Admin_Login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        
        HttpSession AdminSession = request.getSession(false);
        
        if (AdminSession != null && AdminSession.getAttribute("ConnectedAdmin") != null) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409 Conflict
            response.getWriter().write("{\"success\": false, \"message\": \"Admin already logged in!\"}");
            return ;
        }
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        String adminUser = System.getenv("ADMIN_USER");
        String adminPass = System.getenv("ADMIN_PASS");
        if (adminUser == null) adminUser = "admin";
        if (adminPass == null) adminPass = "K9mP2vLx7!";

        if (adminUser.equals(username) && adminPass.equals(password)) {
            AdminSession = request.getSession(true);
            AdminSession.setAttribute("ConnectedAdmin", true);
            
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true, \"message\": \"Login successful\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid username or password!\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlets handles the login for the admin.";
    }
}
