async function LoadRequests() {
    const RequestsContainer = document.getElementById("RequestsContainer");
    RequestsContainer.innerHTML = "";
    
    const accept_buttons = document.getElementById("accept_buttons");
    accept_buttons.innerHTML = "";
    
    const accept_message = document.getElementById("accept_message");
    accept_message.textContent = "";
    
    try {
        const response = await fetch('/E-199/admin_GetVolunteersRequests');
        if (!response.ok) {
            console.error("Error in response!");
            accept_message.textContent = "Invalid reesponse from the server!";
            accept_message.style.color = "red";
            return ;
        }
        
        const result = await response.json();
        if (result.success) {
            if (result.data.length === 0 ) {
                accept_message.textContent = "No requests from the volunteers!";
                accept_message.style.color = "yellow";
            }
            
            const ul = document.createElement("ul");
            RequestsContainer.appendChild(ul);
            
            result.data.forEach((request) => {
                // create the element of the list
                const li = document.createElement("li");
                li.textContent = `Incident_id: ${request.incident_id}, Username: ${request.volunteer_username}, Type: ${request.volunteer_type}`;
                ul.appendChild(li);
               
                // create the button
                const button  = document.createElement("button");
                button.textContent = `Accept ${request.volunteer_username}`;
                
                // Event Listener for the button
                button.addEventListener("click", () => {
                   AcceptRequests(request.volunteer_username); 
                });
                
                accept_buttons.appendChild(button);
            });
        } else {
            console.error("Failed to load the requests!");
            accept_message.textContent = "Failed to load the requests!";
            accept_message.style.color = "red";
        }
    } catch (error) {
        console.error("Error in catch:", error.message);
        accept_message.textContent = "Unexpected error occurred!";
        accept_message.style.color = "red";
    }
}

async function AcceptRequests(volunteer_username) {
    const accept_message = document.getElementById("accept_message");
    accept_message.textContent = "";
    
    try {
        const response = await fetch('/E-199/admin_AcceptVolunteersRequest', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ volunteer_username: volunteer_username })
        });
        
        if (!response.ok) {
            console.log("Invalid response from the server!");
            accept_message.textContent = "Invalid response from the server.";
            accept_message.style.color = "red";
        } else {
            console.log("Participant was accepted!");
            accept_message.textContent = `You accepted volunteer's ${volunteer_username} request!`;
            accept_message.style.color = "green";
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
        accept_message.textContent = "Unexpected error occurred!!";
        accept_message.style.color = "red";
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", LoadRequests);
