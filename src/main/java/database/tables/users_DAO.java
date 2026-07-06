package database.tables;

import database.DB_Connection;
import java.sql.SQLException;
import java.sql.*;
import mainClasses.User;

public class users_DAO {
    private final Connection connection;
    
    public users_DAO() throws SQLException, ClassNotFoundException {
        this.connection = DB_Connection.getConnection();
    }
    
    public boolean InsertUser(User user) throws SQLException, ClassNotFoundException {
        String query = "INSERT INTO users (username, email, password, firstname, lastname, birthdate, gender, afm, " +
        "country, address, municipality, prefecture, job, telephone, lat, lon) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getFirstname());
            statement.setString(5, user.getLastname());
            statement.setString(6, user.getBirthdate());
            statement.setString(7, user.getGender());
            statement.setString(8, user.getAfm());
            statement.setString(9, user.getCountry());
            statement.setString(10, user.getAddress());
            statement.setString(11, user.getMunicipality());
            statement.setString(12, user.getPrefecture());
            statement.setString(13, user.getJob());
            statement.setString(14, user.getTelephone());
            statement.setDouble(15, user.getLat());
            statement.setDouble(16, user.getLon());
            
            int RowsInserted = statement.executeUpdate();
            if (RowsInserted > 0) {
                System.out.println("User inserted successfully!");
                return true;
            } else {
                System.out.println("User failed to insert!");
                return false;
            }
        }
    }
    
    public User GetUserByUsername(String username) throws SQLException {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet results = statement.executeQuery()) {
                if (results.next()) {
                    User user = new User();
                    user.setUsername(results.getString("username"));
                    user.setPassword(results.getString("password"));
                    user.setFirstname(results.getString("firstname"));
                    user.setLastname(results.getString("lastname"));
                    user.setBirthdate(results.getString("birthdate"));
                    user.setGender(results.getString("gender"));
                    user.setAfm(results.getString("afm"));
                    user.setCountry(results.getString("country"));
                    user.setAddress(results.getString("address"));
                    user.setMunicipality(results.getString("municipality"));
                    user.setPrefecture(results.getString("prefecture"));
                    user.setJob(results.getString("job"));
                    user.setTelephone(results.getString("telephone"));
                    user.setLat(results.getDouble("lat"));
                    user.setLon(results.getDouble("lon"));
                    return user;  
                } else {
                    return null;
                }   
            }
        }
    }
    
    public void UpdatePassword(String username, String password) throws SQLException {
        String query = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, password);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }
    
    public void UpdateFirstname(String username, String firstname) throws SQLException {
        String query = "UPDATE users SET firstname = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstname);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } 
    }

    public void UpdateLastname(String username, String lastname) throws SQLException {
        String query = "UPDATE users SET lastname = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, lastname);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateBirthdate(String username, String birthdate) throws SQLException {
        String query = "UPDATE users SET birthdate = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, birthdate);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateGender(String username, String gender) throws SQLException {
        String query = "UPDATE users SET gender = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, gender);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateAfm(String username, String afm) throws SQLException {
        String query = "UPDATE users SET afm = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, afm);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateCountry(String username, String country) throws SQLException {
        String query = "UPDATE users SET country = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, country);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateAddress(String username, String address) throws SQLException {
        String query = "UPDATE users SET address = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, address);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateMunicipality(String username, String municipality) throws SQLException {
        String query = "UPDATE users SET municipality = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, municipality);
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
    
    public void UpdatePrefecture(String username, String prefecture) throws SQLException {
        String query = "UPDATE users SET prefecture = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, prefecture);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateJob(String username, String job) throws SQLException {
        String query = "UPDATE users SET job = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, job);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    }

    public void UpdateTelephone(String username, String telephone) throws SQLException {
        String query = "UPDATE users SET telephone = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, telephone);
            stmt.setString(2, username);
            stmt.executeUpdate();
        }
    } 
    
    public int UsersCount () throws SQLException {
        String query = "SELECT COUNT(*) as total FROM users";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }
}

 

