package servlets;

import com.google.gson.Gson;
import database.tables.users_DAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.Map;
import mainClasses.User;

public class UserRegistration extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserRegistration</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserRegistration at " + request.getContextPath() + "</h1>");
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
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            Gson gson = new Gson();
            Map<String, Object> data = gson.fromJson(jsonBuilder.toString(), Map.class);
            
            User user = new User();
            user.setUsername(data.get("username").toString());
            user.setEmail(data.get("email").toString());
            user.setPassword(data.get("password").toString());
            user.setFirstname(data.get("firstname").toString());
            user.setLastname(data.get("lastname").toString());
            user.setBirthdate(data.get("birthdate").toString());
            user.setGender(data.get("gender").toString());
            user.setAfm(data.get("afm").toString());
            user.setCountry(data.get("country").toString());
            user.setAddress(data.get("address").toString());
            user.setMunicipality(data.get("municipality").toString());
            user.setPrefecture(data.get("prefecture").toString());
            user.setJob(data.get("job").toString());
            user.setTelephone(data.get("telephone").toString());
            user.setLat(Double.parseDouble(data.get("lat").toString()));
            user.setLon(Double.parseDouble(data.get("lon").toString()));
            
            users_DAO UserDAO = new users_DAO();
            boolean success = UserDAO.InsertUser(user);
            if (success) {
                response.setStatus(HttpServletResponse.SC_OK);
                result.write("{\"success\": true, \"message\": \"User registered successfully!\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.write("{\"success\": false, \"message\": \"Failed to register user!\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.write("{\"success\": false, \"message\": \"Error in server occured!\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "This servlet registers a user in database";
    }
}
