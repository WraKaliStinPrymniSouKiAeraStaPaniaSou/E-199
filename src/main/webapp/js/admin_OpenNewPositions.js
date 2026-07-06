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
        const response = await fetch('/E-199/admin_GetALLIncidents');
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

async function OpenNewPositions(event) {
    event.preventDefault();
    
    const submit_message = document.getElementById("submit_message");
    submit_message.textContent = "";
    
    const incident_id = document.getElementById("RunningIncidents").value;
    if (!incident_id) {
        console.log("No incident_id selected!");
        return ;
    }
    
    const positions = document.getElementById("positions").value;
    
    const form_data = {
        incident_id: incident_id,
        positions: positions
    };
    console.log("form_data: ", form_data);
    
    try {
        const response = await fetch('/E-199/admin_OpenNewPositions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(form_data)
        });
        
        console.log(response);
        if (!response.ok) {
            console.error("The response from the server was not valid!");
            submit_message.textContent = "The response from the server was not valid!";
            submit_message.style.color = "red";
        } else {
            submit_message.textContent = "The participants' jobs were inserted successfully!";
            submit_message.style.color = "green";
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
        submit_message.textContent = "Unexpected error occurred!";
        submit_message.style.color = "red";
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", async () => {
    await SelectActiveIncidents();
});
document.getElementById("form").addEventListener("submit", OpenNewPositions);

