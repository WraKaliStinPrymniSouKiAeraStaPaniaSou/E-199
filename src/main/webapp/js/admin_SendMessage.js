async function LoadMessages() {
    let RunningIncidentsList = document.getElementById('RunningIncidentsList');
    RunningIncidentsList.innerHTML = "";
    
    try {
        const response = await fetch('/E-199/ConnectedUser_GetActiveIncidentsAndExtra');
        if (!response.ok) {
            alert("Failed to load the messages!");
            console.error("Error: failed to load the messages");
            return ;
        }
        
        const result = await response.json();
        console.log(result);
        if (result.success) {
            result.data.forEach((item) => {
                const message = item.message;
                const address = item.address;
                const incident_type = item.incident_type;
                const date_time = item.date_time;
                const li = document.createElement('li');
                li.textContent = `Message: ${message}, Address: ${address}, Incident Type: ${incident_type}, Date/Time: ${date_time}`;
                RunningIncidentsList.appendChild(li);
            });
        } else {
            console.error("Error making the list.");
        }
    } catch (error) {
        console.error("Error in catch1: ", error.message);
    }
}

async function SelectActiveIncidents() {
    let select = document.getElementById('RunningIncidents');
    select.innerHTML = "";
    
    const placeholder = document.createElement("option");
    placeholder.value = "";
    placeholder.textContent = "Choose an incident";
    placeholder.selected = true;
    placeholder.hidden = true;
    select.appendChild(placeholder);
    
    try {
        const response = await fetch('/E-199/ConnectedUser_GetActiveIncidents');
        if (!response.ok) {
            alert("Error: failed to load the incidents for the select!");
            console.error("Failed to load the incidents for the select!");
            return ;  
        }
        
        const result = await response.json();
        console.log(result);
        if (result.success) {
            result.data.forEach((Incident) => {
                const option = document.createElement("option");
                option.value = Incident.incident_id;
                option.textContent = `Type: ${Incident.incident_type}, Address: ${Incident.address}`;
                select.appendChild(option);
            });
        } else {
            console.error("Error making the select!");
        }
    } catch (error) {
        console.error("Error in catch2: ", error.message);
    }
}

async function AddMessageToDatabase() {
    event.preventDefault();
    
    const submit_message = document.getElementById("submit_message");
    submit_message.textContent = "";
    
    const incident_id = document.getElementById("RunningIncidents").value;
    const message = document.getElementById("description").value;
    
    const data =  {
        incident_id: incident_id,
        message: message
    };
    
    try {
        const response = await fetch('/E-199/admin_SendMessage', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        console.log(response);
        if (response.ok) {
            submit_message.textContent = "message uploaded successfully!";
            submit_message.style.color = "green";
            document.getElementById("form").reset();
        } else {
            submit_message.textContent = "Unexpected error occured!";
            submit_message.style.color = "red";
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
        submit_message.textContent = "Unexpected error occured!!";
        submit_message.style.color = "red";
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", async () => {
    await LoadMessages();
    await SelectActiveIncidents();
});
document.getElementById("form").addEventListener("submit", AddMessageToDatabase);