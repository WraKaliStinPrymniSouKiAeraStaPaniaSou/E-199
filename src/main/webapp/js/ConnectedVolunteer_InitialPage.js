async function WelcomeVolunteer() {
    const header = document.querySelector("h1");
    
    try {
        const response = await fetch('/E-199/ConnectedVolunteer_GetUsername', {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' }
        });
        
        const result = await response.json();
        if (result.success) {
            header.textContent = `Welcome ${result.username}!`;
        } else {
            header.textContent = "Error!";
        }
    } catch (error) {
        console.error("Error");
    }
}

async function logout() {
    try {
        const response = await fetch('/E-199/ConnectedVolunteer_Logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (response.ok) {
            window.location.href = '/E-199/html/Initial_Page.html';
        } else {
            console.error("Logout failed!");
            alert("Logout failed. Try again later!");
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
    }
}

// Event Listeners
document.addEventListener("DOMContentLoaded", WelcomeVolunteer);
document.getElementById("LogoutButton").addEventListener("click", logout);