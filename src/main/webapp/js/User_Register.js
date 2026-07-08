// Check if the passwords are the same.
function correct_password() {
    let password = document.getElementById('password').value;
    let passwordc = document.getElementById('passwordc').value;
    let error = document.getElementById('code-error');

   
    if (passwordc.length > 0) {
        if (password !== passwordc) {
            error.textContent = "The passwords must be the same!";
            error.style.color = "red";
        } else {
            error.textContent = "The passwords match!";
            error.style.color = "green";
        }
    } else {
        error.textContent = "";
    }
    
}

// Hide and Reveal the passwords.
function Reveal_Hide_password(caller_id, current_button) {
    let temp = document.getElementById(caller_id);

    if (temp.value.length>0) {
        if (temp.type === "text") {
            temp.type = "password";
            current_button.innerHTML = "show password";
        } else {
            temp.type = "text";
            current_button.innerHTML = "hide password";
        }
    }
}

// Secure password.
function secure_password() {
    const forbidden_words = ["fire", "fotia", "ethelontis", "volunteer"];

    let password = document.getElementById('password').value;
    password = password.toLowerCase();

    for (let i=0; i<forbidden_words.length; i++) {
        if (password.includes(forbidden_words[i])) {
            alert("The code you provided contains forbidden words. Please, type a valid code!")
            return false;
        }
    }
    return true;
}

// Counts how many times each character appears and returns the max.
function count_characters_return_max(password) {
    // Object to count the characters.
    let count_char = {};
    let max = 0;

    /* if the current char is undefined it adds 1 to the count_char.
    Find the max appearances of a single character. */
    for (let i=0; i<password.length; i++) {
        count_char[password[i]] = (count_char[password[i]] || 0) + 1;
        max = Math.max(max, count_char[password[i]]);
    }

    return max;
}

// Finds if the password has at least 1 symbol, 1 capital letter, 1 number and 1 small letter.
function is_password_strong(password) {
    let condition1 = 0;
    let condition2 = 0;
    let condition3 = 0;
    let condition4 = 0;
    for (let i=0; i<password.length; i++) {
        if (condition1 === 1 && condition2 === 1 && condition3 === 1 && condition4 === 1) {
            return true;
        } else if (/[A-ZΑ-Ω]/.test(password[i])) {
            condition1 = 1;
        } else if (/[^A-Za-zΑ-Ωα-ω0-9]/.test(password[i])) {
            condition2 = 1;
        } else if (/[0-9]/.test(password[i])) {
            condition3 = 1;
        } else if (/[a-zα-ω]/.test(password[i])) {
            condition4 = 1;
        }
    }

    if (condition1 === 1 && condition2 === 1 && condition3 === 1 && condition4 === 1) {
        return true;
    } else {
        return false;
    }
}

// Don't let submit.
function no_submit() {
    document.getElementById('submit').disabled = true;
}

// Makes submit able again.
function yes_submit() {
   document.getElementById('submit').disabled = false;
}

// Changes message before submit.
function password_message(message) {
    const submit_message = document.getElementById('submit_cannot_happen');
    submit_message.textContent = message;
}

// Password's strength.
function password_strength() {
    let password = document.getElementById('password').value;
    let password_numbers = password.match(/[0-9]/g);
    if (password_numbers === null) {
        password_numbers = [];
    }
    const code_strength = document.getElementById('code-strength');

    // remove all the previous classes we created.
    code_strength.classList.remove('weak-password', 'medium-password', 'strong-password');

    if (password.length>0) {
        if(password_numbers.length/password.length*100 >= 50 || count_characters_return_max(password)/password.length*100 >= 50) {
            code_strength.textContent = "weak password";
            code_strength.classList.add('weak-password');
            no_submit();
            password_message("The password is weak, the submit cannot happen!");
        } else if (is_password_strong(password)){
            code_strength.textContent = "strong password";
            code_strength.classList.add('strong-password');
            yes_submit();
            password_message("");
        } else {
            code_strength.textContent = "medium password";
            code_strength.classList.add('medium-password');
            yes_submit();
            password_message("");
        }
    }
    return code_strength.textContent;
}

// Finds someone's age.
function find_age() {
    const message_for_firefighter = document.getElementById('age_limitation_message');
    
    const birth_date = new Date(document.getElementById('birthdate').value);
    const today = new Date();

    const current_year = today.getFullYear();
    const current_month = today.getMonth();
    const current_day = today.getDate();

    const birth_year = birth_date.getFullYear();
    const birth_month = birth_date.getMonth();
    const birth_day = birth_date.getDate();

    let age = current_year -  birth_year;
    if (current_month < birth_month || (current_month === birth_month && current_day < birth_day)) {
        age -= 1;
    }
    
    if (age < 18 || age > 55) {
        no_submit();
        message_for_firefighter.textContent = "Your age is <18 or >55, submit cannot happen.";
        message_for_firefighter.classList.add('weak_age');
    } else {
        yes_submit();
    }
}

// JSON form.
function print_form(event) {
    event.preventDefault();

    const form_data = {
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        passwordc: document.getElementById('passwordc').value,
        firstname: document.getElementById('firstname').value,
        lastname: document.getElementById('lastname').value,
        birthdate: document.getElementById('birthdate').value,
        gender: document.getElementById('gender').value,
        afm: document.getElementById('afm').value,
        country: document.getElementById('country').value,
        prefecture: document.getElementById('prefecture').value,
        municipality: document.getElementById('municipality').value,
        address: document.getElementById('address').value,
        occupation: document.getElementById('job').value,
        telephone: document.getElementById('telephone').value
    };

    const print_data = document.getElementById('form_results');
    print_data.innerText = JSON.stringify(form_data, null, 2);
    document.getElementById('JSON_results').style.display = 'block';
}

// Check if the username is unique.
async function checkUsername() {
    const username = document.getElementById('username').value;
    let check_Username_Message = document.getElementById('check_Username_Message');
    let submit_button = document.getElementById('submit');
        
    if (username) {
        try {
            // Send GET requerst to server.
            const response = await fetch(`/E-199/User_CheckName?username=` + encodeURIComponent(username));
     
            // Check if there was an error with the response.
            if (!response.ok) {
                console.log("There was an error in response.");
                check_Username_Message.textContent = "Error! Type the username again!";
                check_Username_Message.style.color = "red";
            }
        
            const JSON_result = await response.json();
            if (JSON_result) {
                if (!JSON_result.available) {
                    check_Username_Message.textContent = "The username is not available!";
                    check_Username_Message.style.color = "red";
                    submit_button.disabled = true;            
                } else {
                    check_Username_Message.textContent = "The username is unique!";
                    check_Username_Message.style.color = "green";
                    submit_button.disabled = false;
                }
            } else {
                console.log("There was an error in JSON.");
                check_Username_Message.textContent = "Error! Type the username again!";
                check_Username_Message.style.color = "red";
                submit_button.disabled = true;
            }
        } catch (error) {
            console.log("There was an error in catch.");
            check_Username_Message.textContent = "Error! Type the username again!";
            check_Username_Message.style.color = "red";
            submit_button.disabled = true;
        }  
    } else {
        check_Username_Message.textContent = "Chooce a username please!";
        check_Username_Message.style.color = "red";
        submit_button.disabled = true;
    }  
}

// Check if the email is unique.
async function checkEmail() {
    const email = document.getElementById('email').value;
    let check_email_message = document.getElementById('check_email_message');
    let submit_button = document.getElementById('submit');
    
    if (email) {
        // Send GET requerst to server.
        const response = await fetch('/E-199/User_CheckEmail?email=' + encodeURIComponent(email)) ;
          
        if (!response.ok) {
            console.error("Error in response");
            check_email_message.textContent = "Error! Type the email again!";
            check_email_message.style.color = "red";
            submit_button.disabled = true;
            return;
        } 
        
        const JSON_result = await response.json();
        if (!JSON_result.available) {
            check_email_message.textContent = "The email is not available!";
            check_email_message.style.color = "red";
            submit_button.disabled = true;
        } else {
            check_email_message.textContent = "The email is unique!";
            check_email_message.style.color = "green";
            submit_button.disabled = false;
        }
    } else {
        check_email_message.textContent = "Chooce an email, please.";
        check_email_message.style.color = "red";
        submit_button.disabled = true;
    }
}

// Check if the phone is unique.
async function checkPhone() {
    const telephone = document.getElementById('telephone').value;
    let check_telephone_message = document.getElementById('check_telephone_message');
    let submit_button = document.getElementById('submit');
    
    if (telephone) {
        // Send GET requerst to server.
        const response = await fetch('/E-199/User_CheckTelephone?telephone=' + encodeURIComponent(telephone));
        
        if (!response.ok) {
            console.error("Error in response");
            check_telephone_message.textContent = "Error! Type the email again!";
            check_telephone_message.style.color = "red";
            submit_button.disabled = true;
            return;
        } 
        
        const JSON_result = await response.json();
        if (!JSON_result.available) {
            check_telephone_message.textContent = "The phone number is not available!";
            check_telephone_message.style.color = "red";
            submit_button.disabled = true;
        } else {
            check_telephone_message.textContent = "The phone number is unique!";
            check_telephone_message.style.color = "green";
            submit_button.disabled = false;
        }
    } else {
        check_telephone_message.textContent = "Chooce a phone number, please.";
        check_telephone_message.style.color = "red";
        submit_button.disabled = true;
    }
}

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
            return { success: true, lat: result.lat, lon: result.lon };
        } else {
            return { success: false, message: "Could not find the address!" };
        }
    } catch (error) {
        console.error("Error checking address:", error);
        return { success: false, message: "An error occurred verifying the address!" };
    }
}

// Register user.
async function register() {
    event.preventDefault();
    
    let submit_message = document.getElementById("submit_message");
    submit_message.textContent = "";
    
    const address = document.getElementById("address").value.trim();
    const municipality = document.getElementById("municipality").value.trim();
    const country = document.getElementById("country").value.trim();
    const FullAddress = `${address} ${municipality} ${country}`;
    const fallback = `${municipality} ${country}`;
    console.log("Address:", FullAddress);
    
    const VerifyAddress = await IsAddressReal(FullAddress, fallback);
    console.log(VerifyAddress);
    if (!VerifyAddress.success) {
        console.log("Error in verifying address!");
        submit_message.textContent = "Error verifying the address";
        submit_message.style.color = "red";
        return ;
    } else {
        console.log("Valid address!");
        console.log("Latitude:", VerifyAddress.lat);
        console.log("Longitude:", VerifyAddress.lon);
        submit_message.textContent = "Address verified!";
        submit_message.style.color = "green";
    }
    
    const form_data = {
        username: document.getElementById('username').value,
        email: document.getElementById('email').value,
        password: document.getElementById('password').value,
        // passwordConfirmation: document.getElementById('passwordc').value,
        firstname: document.getElementById('firstname').value,
        lastname: document.getElementById('lastname').value,
        birthdate: document.getElementById('birthdate').value,
        gender: document.getElementById('gender').value,
        afm: document.getElementById('afm').value,
        country: document.getElementById('country').value,
        prefecture: document.getElementById('prefecture').value,
        municipality: document.getElementById('municipality').value,
        address: document.getElementById('address').value,
        job: document.getElementById('job').value,
        telephone: document.getElementById('telephone').value,
        lat: VerifyAddress.lat,
        lon: VerifyAddress.lon
    };
    console.log("form_data: ", form_data);
     
//    // Check if all the fields are filled.
//    if (Object.entries(form_data).some(([key, value]) => {
//        const type = form_data.type;
//        const optional_fields_for_user = ['volunteer_type', 'height', 'weight'];
//        
//        // If the type is user, we ignore the optional fields.
//        if (type === 'user' && optional_fields_for_user.includes(key)) {
//            return false;
//        } else {
//            return value === "";
//        }
//    })) {
//        alert('You need to fill all the fields!');
//        return;
//    }

    try {
        const response = await fetch('/E-199/UserRegistration', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(form_data)
        }); 
        if (!response.ok) {
            console.error("Error in response");
            submit_message.textContent = "There was an error with the registration!";
            submit_message.style.color = "red";
        } else {
            submit_message.textContent = "Registration complete!";
            submit_message.style.color = "green";
        }   
    } catch (error) {
        console.log("Error in catch ", error.message);
        alert("There was an error! Try again later!");
    }
}

// Event listeners.
document.getElementById('username').addEventListener('blur', checkUsername);
document.getElementById('email').addEventListener('blur', checkEmail);
document.getElementById('telephone').addEventListener('blur', checkPhone);
document.getElementById('form').addEventListener('submit', register);
document.getElementById('password').addEventListener('input', correct_password);
document.getElementById('passwordc').addEventListener('input', correct_password);
document.getElementById('form').addEventListener('submit', secure_password);
document.getElementById('form').addEventListener('submit', print_form);