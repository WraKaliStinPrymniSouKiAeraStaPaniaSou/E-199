async function LoadFooter() {
    console.log("Loading footer!");
    try {
        const response = await fetch('/E-199/html/footer.html');
        if (!response.ok) {
            console.error("response error for footer!");
        } else {
            document.getElementById("footer").innerHTML = await response.text();
        }
    } catch (error) {
        console.error("Error in catch: ", error.message);
    }
}

// Event Listener
document.addEventListener("DOMContentLoaded", LoadFooter);