package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class admin_Logout extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet admin_Logout</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet admin_Logout at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        request.setCharacterEncoding("UTF-8");
        
        boolean success = false;
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("ConnectedAdmin") != null) {
            session.removeAttribute("ConnectedAdmin"); // remove the attribute, not the whole session!
            success = true;
        }
        
        response.getWriter().write("{\"success\": " + success + "}");
    }

    @Override
    public String getServletInfo() {
        return "This servlet logs out the admin.";
    }
}
