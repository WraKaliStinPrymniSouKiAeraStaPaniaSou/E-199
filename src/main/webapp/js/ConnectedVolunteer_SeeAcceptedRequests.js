async function LoadAcceptedMessage() {
    const message = document.getElementById("message");
    message.textContent = "";
    
    try {
        const response = await fetch('/E-199/ConnectedVolunteer_CheckRequest');
        if (!response.ok) {
            message.textContent = "Invalid response from server!";
            message.style.color = "red";
            return ;
        }
        
        const result = await response.json();
        if (result.success) {
            message.textContent = `You have been accepted for Incident ID: ${result.incident_id}`;
            message.style.color = "green";
        } else {
            message.textContent = "You have not been accepted! Make sure you have requested for an incident first!";
            message.style.color = "yellow";
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
        message.textContent = "Unexpected error occured!";
        message.style.color = "red";
    }
}

// Event Listener
document.addEventListener("DOMContentLoaded", LoadAcceptedMessage);