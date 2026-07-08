async function IsAddressReal(FullAddress, fallback) {
    event.preventDefault();
    
    async function trySearch(query) {
        const URL = `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(query)}&format=json&limit=1`;
        const resp = await fetch(URL);
        if (!resp.ok) return null;
        const data = await resp.json();
        return (data && data.length > 0) ? data[0] : null;
    }
    
    try {
        let result = await trySearch(FullAddress);
        if (!result && fallback) {
            result = await trySearch(fallback);
        }
        if (result) {
            return { success: true, lat: parseFloat(result.lat), lon: parseFloat(result.lon) };
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
    const user_data = Object.fromEntries(form_data.entries());
 
    // Check the address
    const address = document.getElementById("address").value.trim();
    const municipality = document.getElementById("municipality").value.trim();
    const country = "Ελλάδα";
    const FullAddress = `${address} ${municipality} ${country}`;
    const fallback = `${municipality} ${country}`;
    
    const VerifyAddress = await IsAddressReal(FullAddress, fallback);
    console.log(VerifyAddress);
    if (!VerifyAddress.success) {
        console.log("Error in verifying address!");
        submit_message.textContent = "Error verifying the address! If your address is the correct one, try to refresh the page!";
        submit_message.style.color = "red";
        document.getElementById("submit").disabled = true;
        return ;
    }
    
    user_data.lat = VerifyAddress.lat;
    user_data.lon = VerifyAddress.lon;
    console.log("data", user_data);
    
    try {
        const response = await fetch('/E-199/ReportAnIncident', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user_data)
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
