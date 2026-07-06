async function isVolunteerBusy() {
    const accept_message = document.getElementById("accept_message");
    accept_message.textContent = "";
    
    try {
        const response = await fetch('/E-199/ConnectedVolunteer_IsVolunteerBusy');
        if (!response.ok) {
            accept_message.textContent = "Error: Could not load the incidents";
            accept_message.style.color = "red";
            return ;
        } else {
            const result = await response.json();
            if (result.success) {
                accept_message.textContent = "You are in another emergency or you have applied for another emergency. You cannot chooce a second one!";     
                accept_message.style.color = "yellow";
            } else {
                LoadAvailableEmergencies();
            }
        }
    } catch (error) {
        console.log("error in catch: ", error.message);
        accept_message.textContent = "Error: Could not load the incidents";
        accept_message.style.color = "red";
    }
}

async function LoadAvailableEmergencies() {
    const EmergenciesContainer = document.getElementById("EmergenciesContainer");
    EmergenciesContainer.innerHTML = "";
   
    const accept_buttons = document.getElementById("accept_buttons");
    accept_buttons.innerHTML = "";
    
    const accept_message = document.getElementById("accept_message");
    accept_message.textContent = "";
    
    try {
        const response = await fetch('/E-199/ConnectedVolunteer_GetAvailableEmergencies');
        if (!response.ok) {
            console.error("Failed contacting the server!");
            accept_message.textContent = "Error: Could not load the incidents";
            accept_message.style.color = "red";
            return ;
        }
        
        const result = await response.json();
        if (result.success) {
            // Case we found no emergencies
            if (result.data.length === 0) {
                accept_message.textContent = "No emergencies found!";
                accept_message.style.color = "red";
                return ;
            }
            
            // Create the list
            const ul = document.createElement("ul");
            for (const incident_id of result.data) {
                try {
                    const response2 = await fetch(`/E-199/ConnectedVolunteer_GetIncidentByid?incident_id=${incident_id}`);
                    if (!response2.ok) {
                        console.error("Fail in response!");
                        return ;
                    } 
                    
                    const result2 = await response2.json();
                    if (result2.success) {
                        // Create the li
                        const li = document.createElement("li");
                        li.textContent = `ID: ${incident_id}, Type: ${result2.data.incident_type}, Address: ${result2.data.address}`;
                        ul.appendChild(li);
                        
                        // Create the accept button
                        const button = document.createElement("button");
                        button.textContent = `Accept Incident ID: ${incident_id}`;
                        button.addEventListener("click", () => {
                            handleAcceptIncident(incident_id);
                        });
                        accept_buttons.appendChild(button);
                    } else {
                        console.error("Something went wrong!");
                        accept_message.textContent = "Something went wrong";
                        accept_message.style.color = "red";
                    }
                } catch (error) {
                    console.log("error in inner catch: ", error.message);
                    accept_message.textContent = "Error with loading. Try again later!";
                    accept_message.style.color = "red";
                }
            }
            
            EmergenciesContainer.appendChild(ul);
        } else {
            console.error("Error in result 1");
            accept_message.textContent = "Unexpected error occured";
            accept_message.style.color = "red";
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
    }
}

async function handleAcceptIncident(incident_id) {
    const accept_message = document.getElementById("accept_message");
    accept_message.textContent = "";
    
    try {
        const response = await fetch('/E-199/ConnectedVolunteer_AcceptEmergency', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ incident_id: incident_id })
        });
        
        if (!response.ok) {
            console.error("The response from the server was invalid!");
            accept_message.textContent = "Error updating your acceptance. Please try again later!";
            accept_message.style.color = "red";
        } else {
            accept_message.textContent = "Your request uploaded successfully";
            accept_message.style.color = "green";
            setTimeout(() => location.reload(), 1000); // Refresh after 1 second
        }
    } catch(error) {
        console.log("error in catch: ", error.message);
        accept_message.textContent = "Error updating your acceptance. Please try again later!";
        accept_message.style.color = "red";
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", isVolunteerBusy);