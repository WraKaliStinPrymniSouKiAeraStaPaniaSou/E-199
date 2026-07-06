let UserSessionTimeout;

function StartTime() {
    UserSessionTimeout = setTimeout(() => {
        DestroyUserSession();
    }, 1800000); // 30 minutes
}

function ResetTime() {
    clearTimeout(UserSessionTimeout);
    StartTime();
}

async function DestroyUserSession() {
    try {
        const response = await fetch('/E-199/ConnectedUser_DestroySession', {
           method: 'POST',
           headers: {'Content-Type': 'application/json'}
        });
        
        if (response.ok) {
            window.location.href = "/E-199/html/UserLogin.html";
        } else {
            console.log("User session got destroyed!");
        }
    } catch (error) {
        console.error("Destroying user's session caused an error: ", error.message);
    }
}

// Event Listeners
window.addEventListener('mousemove', ResetTime);
window.addEventListener('keypress', ResetTime);
window.addEventListener('click', ResetTime);
window.addEventListener('scroll', ResetTime);
StartTime();
