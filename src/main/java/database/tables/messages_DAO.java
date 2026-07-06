package database.tables;

import database.DB_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mainClasses.Message;
import mainClasses.SpecialMessage;

public class messages_DAO {
    private final Connection connection;

    public messages_DAO() throws SQLException, ClassNotFoundException {
        this.connection = DB_Connection.getConnection();
    }
    
    public boolean AddMessageToDatabase (Message msg) throws SQLException {
        String query_check = "SELECT COUNT(*) FROM messages WHERE incident_id = ? AND message = ?";
        try (PreparedStatement statement1 = connection.prepareStatement(query_check)){
            statement1.setInt(1, msg.getIncident_id());
            statement1.setString(2, msg.getMessage());    
        
            try (ResultSet result = statement1.executeQuery()) {
                if (result.next() && result.getInt(1) > 0) {
                    return false; // The message already exists
                }
            }
        }
        
        String query_add = "INSERT INTO messages (incident_id, message, sender, recipient, date_time) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement2 = connection.prepareStatement(query_add)) {
            statement2.setInt(1, msg.getIncident_id());
            statement2.setString(2, msg.getMessage());
            statement2.setString(3, msg.getSender());
            statement2.setString(4, msg.getRecipient());
            statement2.setString(5, msg.getDate_time());
            
            int RowsInserted = statement2.executeUpdate();
            return RowsInserted > 0;
        }
    }
    
    public List<SpecialMessage> getMessageForAI() throws SQLException {
        String query = "SELECT m.message_id, m.incident_id, m.message, m.sender, m.recipient, m.date_time, " +
        "i.address, i.incident_type " +
        "FROM messages m " +
        "JOIN incidents i ON m.incident_id = i.incident_id " +
        "WHERE i.status = 'running' AND i.end_datetime IS NULL AND m.recipient = 'public'";

        List<SpecialMessage> Messages = new ArrayList<>();
        
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet Results = statement.executeQuery()) {
            
            while(Results.next()){
                SpecialMessage special = new SpecialMessage(
                    Results.getString("message"),
                    Results.getString("date_time"),
                    Results.getString("incident_type"),
                    Results.getString("address")   
                );
                
                Messages.add(special);
            }
        }
        
        return Messages;
    }
}
