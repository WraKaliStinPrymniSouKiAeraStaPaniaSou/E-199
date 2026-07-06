let VolunteerSessionTimeout;

function StartTime() {
    VolunteerSessionTimeout = setTimeout(() => {
        DestroyUserSession();
    }, 1800000); // 30 minutes
}

function ResetTime() {
    clearTimeout(VolunteerSessionTimeout);
    StartTime();
}

async function DestroyUserSession() {
    try {
        const response = await fetch('/E-199/ConnectedVolunteer_DestroySession', {
           method: 'POST',
           headers: {'Content-Type': 'application/json'}
        });
        
        if (response.ok) {
            window.location.href = "/E-199/html/VolunteerLogin.html";
        } else {
            console.log("Volunteer session got destroyed!");
        }
    } catch (error) {
        console.error("Destroying volunteer's session caused an error: ", error.message);
    }
}

// Event Listeners
window.addEventListener('mousemove', ResetTime);
window.addEventListener('keypress', ResetTime);
window.addEventListener('click', ResetTime);
window.addEventListener('scroll', ResetTime);
StartTime();
