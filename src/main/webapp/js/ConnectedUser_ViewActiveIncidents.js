async function initializeMap() {
    let ErrorMessage = document.getElementById('ErrorMessage');
    
    try {
        const response = await fetch('/E-199/ConnectedUser_GetInfo');
        if (!response.ok) {
            ErrorMessage.textContent = "Error: Could not get user's information :(";
            ErrorMessage.style.display = "block";
            return ;
        }
        
        const result = await response.json();
        console.log(result);
        if (result.success) {
            const UserLat = result.data.lat;
            const UserLon = result.data.lon;
            
            // Create the map (centre of the map, user's address)
            const map = L.map('ActiveIncidentsMap').setView([UserLat, UserLon], 12);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo(map);
            
            return map;
        } else {
            ErrorMessage.textContent = "Error: user's data retrieval failed!";
            ErrorMessage.style.display = "block";
        }
    } catch (error) {
        console.error("Error in catch: initializeMap()" + error.message);
        alert("Error initializing map occured!");    
    }
}

async function GetActiveIncidents(map) {
    let ErrorMessage = document.getElementById('ErrorMessage');
    
    try {
        const response = await fetch('/E-199/ConnectedUser_ViewALLIncidents');
        if (!response.ok) {
            ErrorMessage.textContent = "Error finding the active incidents!";
            ErrorMessage.style.display = "block";
            return ;
        }
        
        const result = await response.json();
        console.log(result);
        if (result.success) {
            let ActiveIncidentsList = document.getElementById("ActiveIncidentsList");
            ActiveIncidentsList.innerHTML = ""; // Clear previous data
            
            /* Create the elements for the list and insert them to it
            Add a marker to the map */
            result.data.forEach(Incident => {
                let li = document.createElement("li");
                li.textContent = `Type: ${Incident.incident_type}, Address: ${Incident.address}, Danger: ${Incident.danger}`;
                ActiveIncidentsList.appendChild(li);
            
                // Add a marker to the map
                L.marker([Incident.lat, Incident.lon])
                .addTo(map)
                .bindPopup(`Incident Type: ${Incident.incident_type}<br>Address: ${Incident.address}<br>Danger: ${Incident.danger}`);
            });   
        } else { 
            ErrorMessage.textContent = "Error: failed to load the active incidents.";
            ErrorMessage.style.display = "block";
            console.error("Result failed");
        }
    } catch (error) {
        console.error("Error in catch: GetActiveIncidents(map)" + error.message);
        alert("Error Occured!");    
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", async () => {
    const map = await initializeMap();
    if (!map) {
        console.error("Map failed to initialize.");
        return ;
    }
    await GetActiveIncidents(map);
});
