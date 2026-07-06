package mainClasses;

public class SpecialMessage {
    private String message;
    private String date_time;
    private String incident_type;
    private String address;
    
    public SpecialMessage(String message, String dateTime, String incidentType, String address) {
        this.message = message;
        this.date_time = dateTime;
        this.incident_type = incidentType;
        this.address = address;
    }
}
