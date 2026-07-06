package database.tables;

import database.DB_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mainClasses.Participant;

public class participants_DAO {
    private final Connection connection;
    
    public participants_DAO() throws SQLException, ClassNotFoundException {
        this.connection = DB_Connection.getConnection();
    }
    
    public boolean AddParticipantsToDatabase(int incident_id, int positions) throws SQLException {
        String query = "INSERT INTO participants (incident_id, volunteer_username, volunteer_type, status, success, comment) VALUES (?, NULL, 'unknown', 'open', NULL, NULL)";
        int RowsInserted = 0;
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Add a new participant "position" times
            for (int i=0; i<positions; i++) {
                statement.setInt(1, incident_id);
                RowsInserted += statement.executeUpdate();
            }
        }
        
        return RowsInserted == positions;
    }
    
    public boolean UpdateParticipant(int incident_id, String username, String volunteer_type) throws SQLException {
        String query = "UPDATE participants SET volunteer_username = ?, volunteer_type = ?, status = 'requested' WHERE incident_id = ? AND status = 'open' LIMIT 1";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, volunteer_type);
            statement.setInt(3, incident_id);
            
            int RowsUpdated = statement.executeUpdate();
            return RowsUpdated > 0;
        }
    }
    
    public List<Integer> GetOpenIncidentIds() throws SQLException {
        String query = "SELECT DISTINCT incident_id " +
            "FROM participants " +
            "WHERE status = 'open';";

        List<Integer> IncidentIds = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet result = stmt.executeQuery()) {
            while (result.next()) {
                IncidentIds.add(result.getInt("incident_id"));
            }
        }

        return IncidentIds;
    }
    
    public boolean IsVolunteerBusy(String username) throws SQLException {
        String query = "SELECT COUNT(*) FROM participants WHERE volunteer_username = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, username);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public boolean AcceptVolunteersRequest(String volunteer_name) throws SQLException {
        String query = "UPDATE participants SET status = 'accepted' WHERE volunteer_username = ? AND status = 'requested'";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, volunteer_name);
            
            int RowsUpdated = statement.executeUpdate();
            return RowsUpdated > 0;
        }
    }
    
    public List<Participant> GetALLRequests() throws SQLException {
        String query = "SELECT * FROM participants WHERE status = 'requested'";
        List<Participant> Requests = new ArrayList<>();
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet results = statement.executeQuery();
            
            while (results.next()) {
                Participant participant = new Participant();
                // We only need the username and the volunteer's type
                participant.setVolunteer_username(results.getString("volunteer_username"));
                participant.setVolunteer_type(results.getString("volunteer_type"));
                participant.setIncident_id(results.getInt("incident_id"));
                Requests.add(participant);
            }
        }
        
        return Requests;
    }
    
    public Participant GetParticipationForVolunteer(String volunteer_username) throws SQLException {
        String query = "SELECT * FROM participants WHERE volunteer_username = ? AND status = 'accepted' LIMIT 1";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) { 
            statement.setString(1, volunteer_username);
            
            ResultSet results = statement.executeQuery();
            
            if (results.next()) {
                Participant participant = new Participant();
                participant.setIncident_id(results.getInt("incident_id"));
                return participant;
            }
        }
        
        return null;
    }
}