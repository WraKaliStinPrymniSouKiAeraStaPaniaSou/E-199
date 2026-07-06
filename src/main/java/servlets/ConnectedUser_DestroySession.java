package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ConnectedUser_DestroySession extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ConnectedUser_DestroySession</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ConnectedUser_DestroySession at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
         response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");  
        
        HttpSession UserSession = request.getSession(false);
        if (UserSession != null) {
            UserSession.invalidate();
        }
        response.getWriter().write("{\"success\": true}");
    }
    
    @Override
    public String getServletInfo() {
        return "This servlet destroys the user's session";
    }
}
