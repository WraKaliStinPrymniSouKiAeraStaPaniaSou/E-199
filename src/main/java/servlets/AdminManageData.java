package servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import database.DB_Connection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminManageData extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("ConnectedAdmin") == null) {
                response.setStatus(401);
                out.write("{\"success\":false,\"message\":\"Not authorized\"}");
                return;
            }

            String type = request.getParameter("type");
            if (type == null || type.isEmpty()) {
                response.setStatus(400);
                out.write("{\"success\":false,\"message\":\"Missing type parameter\"}");
                return;
            }

            String[] validTypes = {"incidents", "users", "volunteers", "messages", "participants"};
            boolean valid = false;
            for (String t : validTypes) {
                if (t.equals(type)) { valid = true; break; }
            }
            if (!valid) {
                response.setStatus(400);
                out.write("{\"success\":false,\"message\":\"Invalid type\"}");
                return;
            }

            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();
            String query = "SELECT * FROM " + type + " ORDER BY "
                + (type.equals("incidents") ? "incident_id" :
                   type.equals("users") ? "user_id" :
                   type.equals("volunteers") ? "volunteer_id" :
                   type.equals("messages") ? "message_id" : "participant_id") + " DESC";
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            JsonArray arr = new JsonArray();
            while (rs.next()) {
                JsonObject obj = new JsonObject();
                for (int i = 1; i <= colCount; i++) {
                    String val = rs.getString(i);
                    obj.addProperty(meta.getColumnName(i), val != null ? val : "");
                }
                arr.add(obj);
            }
            rs.close();
            stmt.close();
            con.close();

            JsonObject result = new JsonObject();
            result.addProperty("success", true);
            result.add("data", arr);
            out.write(result.toString());
        } catch (Exception e) {
            response.setStatus(500);
            out.write("{\"success\":false,\"message\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("ConnectedAdmin") == null) {
                response.setStatus(401);
                out.write("{\"success\":false,\"message\":\"Not authorized\"}");
                return;
            }

            String type = request.getParameter("type");
            String id = request.getParameter("id");

            if (type == null || id == null || type.isEmpty() || id.isEmpty()) {
                response.setStatus(400);
                out.write("{\"success\":false,\"message\":\"Missing type or id\"}");
                return;
            }

            Connection con = DB_Connection.getConnection();
            Statement stmt = con.createStatement();

            if ("incidents".equals(type)) {
                stmt.executeUpdate("DELETE FROM messages WHERE incident_id = " + id);
                stmt.executeUpdate("DELETE FROM participants WHERE incident_id = " + id);
                stmt.executeUpdate("DELETE FROM incidents WHERE incident_id = " + id);
            } else if ("users".equals(type)) {
                stmt.executeUpdate("DELETE FROM users WHERE user_id = " + id);
            } else if ("volunteers".equals(type)) {
                stmt.executeUpdate("DELETE FROM volunteers WHERE volunteer_id = " + id);
            } else if ("messages".equals(type)) {
                stmt.executeUpdate("DELETE FROM messages WHERE message_id = " + id);
            } else if ("participants".equals(type)) {
                stmt.executeUpdate("DELETE FROM participants WHERE participant_id = " + id);
            }

            stmt.close();
            con.close();

            out.write("{\"success\":true,\"message\":\"Deleted successfully\"}");
        } catch (Exception e) {
            response.setStatus(500);
            out.write("{\"success\":false,\"message\":\"" + e.getMessage().replace("\"", "'") + "\"}");
        }
    }
}
