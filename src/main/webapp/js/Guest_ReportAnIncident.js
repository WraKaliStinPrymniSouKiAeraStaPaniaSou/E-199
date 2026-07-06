async function IsAddressReal(FullAddress) {
    event.preventDefault();
    
    const URL = `https://forward-reverse-geocoding.p.rapidapi.com/v1/search?q=${encodeURIComponent(FullAddress)}&acceptlanguage=en&polygon_threshold=0.0`;
    
    try {
        const response = await fetch(URL, {
            method: "GET",
            headers: {
                "x-rapidapi-host": "forward-reverse-geocoding.p.rapidapi.com",
                "x-rapidapi-key": "5e54dc8cfdmsh76317abe7f63e43p1dfe78jsn5275c0429be6"
            }
        });

        if (!response.ok) {
            throw new Error("Address check failed!");
        }
        
        const result = await response.json();
        if (result && result.length > 0) {
            return { success: true, lat: parseFloat(result[0].lat), lon: parseFloat(result[0].lon) };
        } else {
            return { success: false, message: "Could not find the address!" };
        }
    } catch (error) {
        console.error("Error checking address:", error);
        return { success: false, message: "An error occurred verifying the address!" };
    }
}

async function InsertIncident(event) {
    event.preventDefault();

    let submit_message = document.getElementById("submit_message");
    submit_message.textContent = "";

    const ReportIncidentForm = document.getElementById("ReportIncidentForm");
    const form_data = new FormData(ReportIncidentForm);
    const data = Object.fromEntries(form_data.entries());

    const address = document.getElementById("address").value.trim();
    const municipality = document.getElementById("municipality").value.trim();
    const country = "Ελλάδα";
    const FullAddress = `${address} ${municipality} ${country}`;
    
    const VerifyAddress = await IsAddressReal(FullAddress);
    console.log(VerifyAddress);
    if (!VerifyAddress.success) {
        console.log("Error in verifying address!");
        submit_message.textContent = "Error verifying the address! If your address is the correct one, try to refresh the page!";
        submit_message.style.color = "red";
        document.getElementById("submit").disabled = true;
        return ;
    }
    
    // add lat and lon to the data
    data.lat = VerifyAddress.lat;
    data.lon = VerifyAddress.lon;
    console.log("data: ", data);
    
    try {
        const response = await fetch('/E-199/ReportAnIncident', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) {
            submit_message.textContent = "There was an error!";
            submit_message.style.color = "red";
        } else {
            submit_message.textContent = "The incident reported successfully!";
            submit_message.style.color = "green";
            ReportIncidentForm.reset();
        }
    } catch (error) {
        console.error("Error:", error);
        alert("Error contacting the server!");
    }
}

// Event Listener
document.getElementById("ReportIncidentForm").addEventListener("submit", InsertIncident);