async function initializeMap() {
    // The map initializes to the centre of Crete
    const map = L.map('map').setView([35.240, 24.809], 8);
    
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 20,
        ttribution: '© OpenStreetMap contributors'
    }).addTo(map);

    const marker = L.marker([35.240, 24.809]).addTo(map)
        .bindPopup("Center of Crete")
        .openPopup();

    await LoadIncidentsToMap(map);
}

async function LoadIncidentsToMap(map) {
    const error_message = document.getElementById("error_message");
    error_message.textContent = "";
    
    try {
        const response = await fetch('/E-199/Guest_ViewActiveIncidents');
        if (!response.ok) {
            console.error("Error receiving response!");
            error_message.textContent = "Error receiving the response from the server!";
            error_message.style.color = "red";
            return ;
        }
        
        const result = await response.json();
        console.log(result);
        if (result.success) {
            result.data.forEach(incident => {
                const { lat, lon, incident_type, address, description } = incident;
                
                L.marker([lat, lon])
                    .addTo(map)
                    .bindPopup(`
                        <b>Type:</b> ${incident_type} <br>
                        <b>Address:</b> ${address} <br>
                        <b>Description:</b> ${description}
                    `);
            });
        }
    } catch (error) {
        console.log("error in catch", error.message);
        error_message.textContent = "There was an error. Try again later!";
        error_message.style.color = "red";
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", async() => {
   await initializeMap(); 
});
