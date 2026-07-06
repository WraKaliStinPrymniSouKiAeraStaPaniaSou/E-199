google.charts.load('current', { packages: ['corechart'] });
google.charts.setOnLoadCallback(LoadStatistics);

async function LoadStatistics() {
    const message = document.getElementById('message');
    message.textContent = "";
    
    try {
        const response = await fetch('/E-199/admin_ViewStatistics');
        if (!response.ok) {
            message.textContent = "Invalid response from the server!";
            message.style.color = "red"; 
        }
        
        const result = await response.json();
        console.log("result: ", result);
        if (result.success) {
            // ---------------------------------------------------------------------
            
            // incidents data
            const incidentData = google.visualization.arrayToDataTable([
                ['Incident Type', 'Count'],
                ['Flood', result.data.flood],
                ['Earthquake', result.data.earthquake],
                ['Fire', result.data.fire],
                ['Accident', result.data.accident]
            ]);
            
            const options = {
                title: 'Incidents by Type',
                pieHole: 0.4
            };
            
            const chart = new google.visualization.PieChart(document.getElementById('IncidentStatistics'));
            chart.draw(incidentData, options);
            
            // ---------------------------------------------------------------------
            
            // Firemen and Vehicles data
            const firemenVehiclesData = google.visualization.arrayToDataTable([
                ['Category', 'Count'],
                ['Firemen', Number(result.data.firemen) || 0],
                ['Vehicles', Number(result.data.vehicles) || 0]
            ]);
            
            const firemenVehiclesOptions = {
                title: 'Firemen and Vehicles',
                legend: { position: 'none' }
            };
            
            const firemenVehiclesChart = new google.visualization.BarChart(document.getElementById('FiremenVehiclesStatistics'));
            firemenVehiclesChart.draw(firemenVehiclesData, firemenVehiclesOptions);
            
            // ---------------------------------------------------------------------
            
            // User and Volunteer data
            const userVolunteerData = google.visualization.arrayToDataTable([
                ['Category', 'Count'],
                ['Users', result.data.users],
                ['Volunteers', result.data.volunteers]
            ]);
            
            const options2 = {
                title: 'Users and Volunteers',
                legend: { position: 'none' }
            };
            
            const chart2 = new google.visualization.BarChart(document.getElementById('UserVolunteerStatistics'));
            chart2.draw(userVolunteerData, options2);
        } else {
            message.textContent = "Failed to load statistics!";
            message.style.color = "red"; 
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
        message.textContent = "Unexpected error occured!";
        message.style.color = "red";
    }
}