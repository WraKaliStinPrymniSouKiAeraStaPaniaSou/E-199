package database.tables;

import database.DB_Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mainClasses.Incident;

public class incidents_DAO {
    private final Connection connection;

    public incidents_DAO() throws SQLException, ClassNotFoundException {
        this.connection = DB_Connection.getConnection();
    }

    public void InsertIncident(Incident incident) throws SQLException {
        String query = "INSERT INTO incidents (incident_type, description, user_phone, user_type, address, lat, lon, municipality, prefecture, start_datetime, end_datetime, danger, status, finalResult, vehicles, firemen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, incident.getIncident_type());
            statement.setString(2, incident.getDescription());
            statement.setString(3, incident.getUser_phone());
            statement.setString(4, incident.getUser_type());
            statement.setString(5, incident.getAddress());
            statement.setDouble(6, incident.getLat());
            statement.setDouble(7, incident.getLon());
            statement.setString(8, incident.getMunicipality());
            statement.setString(9, incident.getPrefecture());
            statement.setString(10, incident.getStart_datetime());
            statement.setString(11, null);
            statement.setString(12, incident.getDanger());
            statement.setString(13, incident.getStatus());
            statement.setString(14, incident.getFinalResult());
            statement.setInt(15, incident.getVehicles());
            statement.setInt(16, incident.getFiremen());
            statement.executeUpdate();
        }
    }

    public Map<String, String> GetIncidentByIncidentId(int incidentId) throws SQLException {
        String query = "SELECT incident_id, incident_type, address FROM incidents WHERE incident_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, incidentId);

            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    Map<String, String> Incident = new HashMap<>();
                    Incident.put("incident_id", String.valueOf(result.getInt("incident_id")));
                    Incident.put("incident_type", result.getString("incident_type"));
                    Incident.put("address", result.getString("address"));
                    return Incident;
                }
            }
        }
        
        return null;
    }

    public List<Incident> GetALLIncidents()throws SQLException {
        String query = "SELECT * FROM incidents WHERE status != 'fake'";
        List<Incident> ALLIncidents = new ArrayList<>();
        
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet results = statement.executeQuery()) {
            
            while (results.next()) {
                Incident incident = new Incident();
                incident.setIncident_id(results.getInt("incident_id"));
                incident.setIncident_type(results.getString("incident_type"));
                incident.setDescription(results.getString("description"));
                incident.setUser_phone(results.getString("user_phone"));
                incident.setUser_type(results.getString("user_type"));
                incident.setAddress(results.getString("address"));
                incident.setLat(results.getDouble("lat"));
                incident.setLon(results.getDouble("lon"));
                incident.setMunicipality(results.getString("municipality"));
                incident.setPrefecture(results.getString("prefecture"));
                incident.setExistedStartDatetime(results.getString("start_datetime"));
                //incident.setEnd_datetime(result.getString("end_datetime"));
                incident.setDanger(results.getString("danger"));
                incident.setStatus(results.getString("status"));
                incident.setFinalResult(results.getString("finalResult"));
                incident.setVehicles(results.getInt("vehicles"));
                incident.setFiremen(results.getInt("firemen"));
                ALLIncidents.add(incident);
            }
        }
        
        return ALLIncidents;
    }
    
    public boolean UpdateIncident(int incident_id, String status, String danger, int vehicles, int firemen) throws SQLException {
        String query = "UPDATE incidents SET status = ?, danger = ?, vehicles = ?, firemen = ?, end_datetime = ? WHERE incident_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, status);
            statement.setString(2, danger);
            statement.setInt(3, vehicles);
            statement.setInt(4, firemen);
            if ("finished".equals(status)) {
                Incident temp = new Incident();
                temp.setEnd_datetime();
                statement.setString(5, temp.getEnd_datetime());
            } else {
                statement.setString(5, null);
            }
            statement.setInt(6, incident_id);
            
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("The incident was updated successfully!");
                return true;
            } else {
                System.out.println("The incident was not updated!");
                return false;
            }
        }
    }
    
    // Calculates the distance between 2 addresses (using lat and lon)
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Returns all the active incidents in user's municipality where the distance from their address is <30km
    public List<Incident> getAllActiveIncidents(double UserLat, double UserLon, String municipality) throws SQLException {
        String query = "SELECT * FROM incidents WHERE end_datetime IS NULL AND status = 'running' AND (municipality = ? OR municipality IS NULL)";
        List<Incident> ActiveIncidents = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, municipality);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                // Checking the distance of the incident from the user
                double IncidentLat = result.getDouble("lat");
                double IncidentLon = result.getDouble("lon");
                double Distance = calculateDistance(UserLat, UserLon, IncidentLat, IncidentLon);

                if (Distance < 30.0) {
                    Incident incident = new Incident();
                    incident.setIncident_id(result.getInt("incident_id"));
                    incident.setIncident_type(result.getString("incident_type"));
                    incident.setDescription(result.getString("description"));
                    incident.setUser_phone(result.getString("user_phone"));
                    incident.setUser_type(result.getString("user_type"));
                    incident.setAddress(result.getString("address"));
                    incident.setLat(IncidentLat);
                    incident.setLon(IncidentLon);
                    incident.setMunicipality(result.getString("municipality"));
                    incident.setPrefecture(result.getString("prefecture"));
                    incident.setExistedStartDatetime(result.getString("start_datetime"));
                    //incident.setEnd_datetime(result.getString("end_datetime"));
                    incident.setDanger(result.getString("danger"));
                    incident.setStatus(result.getString("status"));
                    incident.setFinalResult(result.getString("finalResult"));
                    incident.setVehicles(result.getInt("vehicles"));
                    incident.setFiremen(result.getInt("firemen"));
                    ActiveIncidents.add(incident);
                }
            }
        }

        return ActiveIncidents;
    }

    // Returns all the active incidents
    public List<Incident> getActIncForMessages() throws SQLException {
        String query = "SELECT * FROM incidents WHERE end_datetime IS NULL AND status = 'running'";
        List<Incident> ActiveIncidents = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Incident incident = new Incident();
                incident.setIncident_id(result.getInt("incident_id"));
                incident.setIncident_type(result.getString("incident_type"));
                incident.setDescription(result.getString("description"));
                incident.setUser_phone(result.getString("user_phone"));
                incident.setUser_type(result.getString("user_type"));
                incident.setAddress(result.getString("address"));
                incident.setLat(result.getDouble("lat"));
                incident.setLon(result.getDouble("lon"));
                incident.setMunicipality(result.getString("municipality"));
                incident.setPrefecture(result.getString("prefecture"));
                incident.setExistedStartDatetime(result.getString("start_datetime"));
                //incident.setEnd_datetime(result.getString("end_datetime"));
                incident.setDanger(result.getString("danger"));
                incident.setStatus(result.getString("status"));
                incident.setFinalResult(result.getString("finalResult"));
                incident.setVehicles(result.getInt("vehicles"));
                incident.setFiremen(result.getInt("firemen"));
                ActiveIncidents.add(incident);
            }
        }

        return ActiveIncidents;
    }

    // Returns all the active incidents in Crete
    public List<Incident> getAllActiveIncidentsInCrete() throws SQLException {
        // We call this method to take all the active incidents
        List<Incident> ActiveIncidents = getActIncForMessages();
    
        // We copy the active incidents, which are in Crete to a new list
        List<Incident> ActiveIncidentsInCrete = new ArrayList<>();
        for (Incident incident : ActiveIncidents) {
            double lat = incident.getLat();
            double lon = incident.getLon();
        
            // Check if the lat and lon refer to an address inside Crete
            if (lat >= 34.800 && lat <= 35.694 && lon >= 23.400 && lon <= 26.290) {
                ActiveIncidentsInCrete.add(incident);
            }
        }
        return ActiveIncidentsInCrete;
    }
    
    public int EarthquakeCount() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM incidents WHERE incident_type = 'earthquake'";
         
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }
    
    public int GetFireCount() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM incidents WHERE incident_type = 'fire'";
    
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }
    
    public int GetAccidentCount() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM incidents WHERE incident_type = 'accident'";
    
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }
    
    public int GetFloodCount() throws SQLException {
        String query = "SELECT COUNT(*) as total FROM incidents WHERE incident_type = 'flood'";
    
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }
    
    public int GetVehiclesCount() throws SQLException {
        String query = "SELECT SUM(vehicles) as total FROM incidents WHERE status IN ('running', 'finished')";
        
        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }
    
    public int GetFiremenCount() throws SQLException {
        String query = "SELECT SUM(firemen) as total FROM incidents WHERE status IN ('running', 'finished')";

        try (PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0; 
    }
}