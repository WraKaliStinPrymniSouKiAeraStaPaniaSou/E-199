package database.tables;

import database.DB_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mainClasses.Volunteer;

public class volunteers_DAO {
    private final Connection connection;
    
    public volunteers_DAO() throws SQLException, ClassNotFoundException {
        this.connection = DB_Connection.getConnection();
    }
    
    public boolean InsertVolunteer(Volunteer volunteer) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO volunteers (username, email, password, firstname, lastname, birthdate, gender, afm, " +
        "country, address, municipality, prefecture, job, telephone, lat, lon, volunteer_type, height, weight) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, volunteer.getUsername());
            statement.setString(2, volunteer.getEmail());
            statement.setString(3, volunteer.getPassword());
            statement.setString(4, volunteer.getFirstname());
            statement.setString(5, volunteer.getLastname());
            statement.setString(6, volunteer.getBirthdate());
            statement.setString(7, volunteer.getGender());
            statement.setString(8, volunteer.getAfm());
            statement.setString(9, volunteer.getCountry());
            statement.setString(10, volunteer.getAddress());
            statement.setString(11, volunteer.getMunicipality());
            statement.setString(12, volunteer.getPrefecture());
            statement.setString(13, volunteer.getJob());
            statement.setString(14, volunteer.getTelephone());
            statement.setDouble(15, volunteer.getLat());
            statement.setDouble(16, volunteer.getLon());
            statement.setString(17, volunteer.getVolunteer_type());
            statement.setDouble(18, volunteer.getHeight());
            statement.setDouble(19, volunteer.getWeight());

            int RowsInserted = statement.executeUpdate();
            if (RowsInserted > 0) {
                System.out.println("Volunteer inserted successfully!");
                return true;
            } else {
                System.out.println("Volunteer failed to insert!");
                return false;
            }
        }
    }
    
    public Volunteer GetVolunteerByUsername(String username) throws SQLException {
        String query = "SELECT * FROM volunteers WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    Volunteer volunteer = new Volunteer();
                    volunteer.setUsername(results.getString("username"));
                    volunteer.setPassword(results.getString("password"));
                    volunteer.setFirstname(results.getString("firstname"));
                    volunteer.setLastname(results.getString("lastname"));
                    volunteer.setBirthdate(results.getString("birthdate"));
                    volunteer.setGender(results.getString("gender"));
                    volunteer.setAfm(results.getString("afm"));
                    volunteer.setCountry(results.getString("country"));
                    volunteer.setAddress(results.getString("address"));
                    volunteer.setMunicipality(results.getString("municipality"));
                    volunteer.setPrefecture(results.getString("prefecture"));
                    volunteer.setJob(results.getString("job"));
                    volunteer.setTelephone(results.getString("telephone"));
                    volunteer.setLat(results.getDouble("lat"));
                    volunteer.setLon(results.getDouble("lon"));
                    volunteer.setVolunteer_type(results.getString("volunteer_type"));
                    volunteer.setHeight(results.getDouble("height"));
                    volunteer.setWeight(results.getDouble("weight"));
                    
                    return volunteer;  
                } else {
                    return null;
                }   
            }
        }
    }
    
    public void UpdatePassword(String username, String password) throws SQLException {
        String query = "UPDATE volunteers SET password = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, password);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
    
    public void UpdateFirstname(String username, String firstname) throws SQLException {
        String query = "UPDATE volunteers SET firstname = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstname);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } 
    }

    public void UpdateLastname(String username, String lastname) throws SQLException {
        String query = "UPDATE volunteers SET lastname = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, lastname);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateBirthdate(String username, String birthdate) throws SQLException {
        String query = "UPDATE volunteers SET birthdate = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, birthdate);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateGender(String username, String gender) throws SQLException {
        String query = "UPDATE volunteers SET gender = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gender);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateAfm(String username, String afm) throws SQLException {
        String query = "UPDATE volunteers SET afm = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, afm);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateCountry(String username, String country) throws SQLException {
        String query = "UPDATE volunteers SET country = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, country);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateAddress(String username, String address) throws SQLException {
        String query = "UPDATE volunteers SET address = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, address);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
    
    public void UpdateLat(String username, double lat) throws SQLException {
        String query = "UPDATE users SET lat = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, lat);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
    
    public void UpdateLon(String username, double lon) throws SQLException {
        String query = "UPDATE users SET lon = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, lon);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateMunicipality(String username, String municipality) throws SQLException {
        String query = "UPDATE volunteers SET municipality = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, municipality);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdatePrefecture(String username, String prefecture) throws SQLException {
        String query = "UPDATE volunteers SET prefecture = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, prefecture);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateJob(String username, String job) throws SQLException {
        String query = "UPDATE volunteers SET job = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, job);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateTelephone(String username, String telephone) throws SQLException {
        String query = "UPDATE volunteers SET telephone = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, telephone);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateVolunteer_Type(String username, String volunteer_type) throws SQLException {
        System.out.println("volunteer_type: " + volunteer_type);
        System.out.println("username: " + username);
        
        if (connection == null || connection.isClosed()) {
            System.out.println("Connection is not available or is closed.");
            return;
        }
        
        String query = "UPDATE volunteers SET volunteer_type = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, volunteer_type);
            stmt.setString(2, username);
            int RowsUpdated = stmt.executeUpdate();
            System.out.println("Rows updated: " +RowsUpdated);
        }
    }
    
    public void UpdateHeight(String username, double height) throws SQLException {
        String query = "UPDATE volunteers SET height = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, height);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
    
    public void UpdateWeight(String username, double weight) throws SQLException {
        String query = "UPDATE volunteers SET weight = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, weight);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
    
    public int GetVolunteerCount() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM volunteers";

        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }
}
