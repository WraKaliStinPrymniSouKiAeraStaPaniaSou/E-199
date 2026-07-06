function initDB() {
    const DatabaseMessage = document.getElementById("DatabaseMessage");
    DatabaseMessage.textContent = "";
    
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            DatabaseMessage.textContent = "Database created successfully!";
        } else if (xhr.status !== 200) {
            DatabaseMessage.textContent = "Error in creating database!";
        }
    };

    xhr.open('GET', '/E-199/InitDB', true);
    // xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

function deleteDB() {
    const DatabaseMessage = document.getElementById("DatabaseMessage");
    DatabaseMessage.textContent = "";
    
    var xhr = new XMLHttpRequest();
    xhr.onload = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            DatabaseMessage.textContent = "Database deleted successfully!";   
        } else if (xhr.status !== 200) {
            DatabaseMessage.textContent = "Error in deleting database!";
        }
    };

    xhr.open('GET', '/E-199/DeleteDB', true);
    // xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
    xhr.send();
}

async function logout() {
    try {
        const response = await fetch('/E-199/admin_Logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        if (response.ok) {
            window.location.href = '/E-199/html/admin_RealInitialPage.html';
        } else {
            console.error("Logout failed!");
            alert("Logout failed. Try again later!");
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
    }
}

// Event Listeners
document.getElementById("CreateDatabase").addEventListener("click", initDB);
document.getElementById("DeleteDatabase").addEventListener("click", deleteDB);
document.getElementById("LogoutButton").addEventListener("click", logout);
