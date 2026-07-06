async function GetUserInfo() {
    let LoadingMessage = document.getElementById('LoadingMessage');
    LoadingMessage.textContent = "";
    
    try {
        const response = await fetch('/E-199/ConnectedUser_GetInfo', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
        
        if (!response.ok) {
            LoadingMessage.textContent = "There was an error!";
            LoadingMessage.style.color = "red";
            return null;
        }
        
        const result = await response.json();
        console.log("result: ", result);
        if (result.success) {
            document.getElementById("passwordDisplay").textContent = "**************************"; // Remains hidden for security reasons
            document.getElementById("firstnameDisplay").textContent = result.data.firstname;
            document.getElementById("lastnameDisplay").textContent = result.data.lastname;
            document.getElementById("birthdateDisplay").textContent = result.data.birthdate;
            document.getElementById("genderDisplay").textContent = result.data.gender;
            document.getElementById("afmDisplay").textContent = result.data.afm;
            document.getElementById("countryDisplay").textContent = result.data.country;
            document.getElementById("addressDisplay").textContent = result.data.address;
            document.getElementById("municipalityDisplay").textContent = result.data.municipality;
            document.getElementById("prefectureDisplay").textContent = result.data.prefecture;
            document.getElementById("jobDisplay").textContent = result.data.job;
            document.getElementById("telephoneDisplay").textContent = result.data.telephone;
            return result;
        } else {
            alert("Error with the load of the data!!");
            return null;
        }
    } catch (error) {
        console.error("Error", error.message);
        alert("Error with the load of the data!");
        return null;
    }
}

function EnableDisableEdit(event) {
    const button = event.target;
    const TargetData = button.getAttribute("data-target");
    
    let spanMessage = document.getElementById(TargetData + "Display");
    let input = document.getElementById(TargetData);
    
    // let PreviousValue = spanMessage.textConent;
    
    if (button.textContent === "Edit") {
        // Εnable edit
        spanMessage.style.display = "none";
        input.style.display = "inline-block";
        button.textContent = "Disable Edit";
    } else {
        // Disable edit
        spanMessage.style.display = "inline";
        input.style.display = "none";
        
        // We reset the options for the user
        if (input.tagName === "INPUT" || input.tagName === "TEXTAREA") {
            input.value = ""; // Whatever the user typed, it no longer exists
        } else if (input.tagName === "SELECT") {
            input.selectedIndex = -1;
            input.value = "";
        }
        button.textContent = "Edit";
    } 
}

async function IsAddressReal(FullAddress) {
    // event.preventDefault();
    
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
            const address = result[0].display_name;
            return { success: true, lat: result[0].lat, lon: result[0].lon};
        } else {
            return { success: false, message: "Could not find the address!" };
        }
    } catch (error) {
        console.error("Error checking address:", error);
        return { success: false, message: "An error occurred verifying the address!" };
    }
}

// Check if the phone is unique.
async function checkPhone() {
    const telephone = document.getElementById('telephone').value;
    let check_telephone_message = document.getElementById('check_telephone_message');
    // let submit_button = document.getElementById('submit');
    
    if (telephone) {
        // Send GET requerst to server.
        const response = await fetch('/E-199/User_CheckTelephone?telephone=' + encodeURIComponent(telephone));
        
        if (!response.ok) {
            console.error("Error in response");
            check_telephone_message.textContent = "Error! Type the email again!";
            check_telephone_message.style.color = "red";
            // submit_button.disabled = true;
            return;
        } 
        
        const JSON_result = await response.json();
        if (!JSON_result.available) {
            check_telephone_message.textContent = "The phone number is not available!";
            check_telephone_message.style.color = "red";
            // submit_button.disabled = true;
        } else {
            check_telephone_message.textContent = "The phone number is unique!";
            check_telephone_message.style.color = "green";
            // submit_button.disabled = false;
        }
    } else {
        check_telephone_message.textContent = "Chooce a phone number, please.";
        check_telephone_message.style.color = "red";
        // submit_button.disabled = true;
    }
}

async function UpdateUserInfo(event) {
    event.preventDefault();
    
    let UpdateMessage = document.getElementById('UpdateMessage');
    UpdateMessage.textContent = "";
    
    const old_data = await GetUserInfo();
    console.log(old_data);
    if (!old_data) {
        alert("Error in loading the old data of the user!");
        return ;
    }
    const form_data = new FormData(document.getElementById("updateForm"));
    let edited_data = {};
    
    // In the new_data we change only the fields the user edited
    form_data.forEach((value, key) => {
        if (value.trim() !== "" && value.trim() !== old_data.data[key]) {
            edited_data[key] = value.trim();
        }
    });
    
    console.log(edited_data);
    
    // if the address is edited check if it is valid
    if (edited_data.address) {
        const address = document.getElementById("address").value.trim();
        const municipality = edited_data.municipality || document.getElementById("municipality").value.trim();
        const country = edited_data.country || document.getElementById("country").value.trim();
        const FullAddress = `${address} ${municipality} ${country}`;
        console.log("Address:", FullAddress);
        
        const addressCheck = await IsAddressReal(FullAddress);
        if (!addressCheck.success) {
            UpdateMessage.textContent = "Invalid address";
            UpdateMessage.style.color = "red";
            return ;
        } else {
            // if the address is valid, we add the lat and lon fields
            edited_data.lat = addressCheck.lat;
            edited_data.lon = addressCheck.lon;
        }
    }
    
    // Check if the user edited something
    if (Object.keys(edited_data).length === 0) {
        alert("You did not edit something!");
        return ;
    }
    
    try {
        const response = await fetch('/E-199/ConnectedUser_UpdateInfo', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(edited_data)
        });
        
        if (!response.ok) {
            UpdateMessage.textContent = "There was an error!";
            UpdateMessage.style.color = "red";
            return ;
        }
        
        const result = await response.json();
        console.log(result);
        if (result.success) {
            UpdateMessage.textContent = "The data updated successfully!";
            UpdateMessage.style.color = "green";
        } else {
            alert("Could not update the new data to the database!");
        }
    } catch (error) {
        console.error("Error: " + error.message);
        alert("Error updating the new data!");
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", GetUserInfo);
document.querySelectorAll(".EditButtons").forEach(button => {
   button.addEventListener("click", EnableDisableEdit); 
});
document.getElementById('telephone').addEventListener('blur', checkPhone);
document.getElementById('updateForm').addEventListener("submit", UpdateUserInfo);

