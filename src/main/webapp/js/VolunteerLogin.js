async function checkLogin() {
    event.preventDefault();
    let LoginMessage = document.getElementById('LoginMessage');
    LoginMessage.textContent = "";
    
    const form_data = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value
    };
    
    try {
        // Send GET request to server.
        const response = await fetch(`/E-199/Volunteer_Login?username=${form_data.username}&password=${form_data.password}`, {
           method: 'GET',
           headers: {'Content-Type': 'application/json'}
        });
        
        if (!response.ok) {
            console.error("Error in response");
            document.getElementById('username').value = "";
            document.getElementById('password').value = "";
            if (response.status === 409) {
                LoginMessage.textContent = "You are already logged in!";
                LoginMessage.style.color = "yellow";
            } else if (response.status === 401) {
                LoginMessage.textContent = "Invalid username or password!";
                LoginMessage.style.color = "red";
            } else {
                LoginMessage.textContent = "There was an error with the Login!";
                LoginMessage.style.color = "red";
            }
            return ;
        } 
        
        const result = await response.json();
        if (result.success) {
            window.location.href = "/E-199/html/ConnectedVolunteer_InitialPage.html";
        } else {
            LoginMessage.textContent = "There was an error in login!";
            LoginMessage.style.color = "red";
        }    
    } catch (error) {
        alert("There was an error in catch!!");
    }
}

// Event Listeners
document.getElementById('form').addEventListener('submit', checkLogin);
