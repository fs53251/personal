
export const validateRegistration = (user,mode) => {
    
    let success = true;
    let errorMsg = "";
    let problem = "";
    for (var key in user) {
        if (key !== "id" && user[key].trim() === "" && !(key === "experience" || key === "slika" || key === "photo") && !(key === "lozinka" && mode === "edit")) {
            success = false;
            errorMsg = "Fill out all required fields.";
            problem = key;
            return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }

    if (user.username.length < 5) {
        success = false;
        errorMsg = "Username must be atleast 5 characters long.";
        problem = "username";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    if (user.username.length > 20) {
        success = false;
        errorMsg = "Username can't be longer than 20 characters.";
        problem = "username";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }
    for (var i = 0; i < user.username.length; i++) {
        let char = user.username.charAt(i);
        if ((char<'a' || char>'z') && (char<'0' || char>'9') && char !== '_') {
            success = false;
            errorMsg = "Username can only contain lowercase letters, numbers or underscores(_).";
            problem = "username";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }


    if(mode==="register"){
        if (user.password.length < 5) {
            success = false;
            errorMsg = "Password must be atleast 5 characters long.";
            problem = "password";
            return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    
        if (user.password.length > 20) {
            success = false;
            errorMsg = "Password can't be longer then 20 characters.";
            problem = "password";
            return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }



    
    if (user.firstName.length > 20) {
        success = false;
        errorMsg = "First name can't be longer than 20 characters.";
        problem = "firstName";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }
    for (i = 0; i < user.firstName.length; i++) {
        let char = user.firstName.charAt(i);
        if ((char<'a' || char>'z') && (char<'A' || char>'Z')  && char !== '-' && char !== ' ') {
            success = false;
            errorMsg = "First name can only contain letters, spaces or dashes(-).";
            problem = "firstName";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }



    if (user.lastName.length > 20) {
        success = false;
        errorMsg = "Last name can't be longer than 20 characters.";
        problem = "lastName";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }
    for (i = 0; i < user.lastName.length; i++) {
        let char = user.lastName.charAt(i);
        if ((char<'a' || char>'z') && (char<'A' || char>'Z')  && char !== '-' && char !== ' ') {
            success = false;
            errorMsg = "Last name can only contain letters, spaces or dashes(-).";
            problem = "lastName";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }

    if (user.DOB.split('-')[0] < 1900 || user.DOB.split('-')[0] > new Date().getFullYear()) {
        success = false;
        errorMsg = "Invalid year of birth.";
        problem = "DOB";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }


    if (user.telephone.length < 5) {
        success = false;
        errorMsg = "Phone number must be atleast 5 characters long.";
        problem = "telephone";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    if (user.telephone.length > 20) {
        success = false;
        errorMsg = "Phone number can't be longer than 20 characters.";
        problem = "telephone";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }
    for (i = 0; i < user.telephone.length; i++) {
        let char = user.telephone.charAt(i);
        if ((char<'0' || char>'9') && !(char === '+' &&  i === 0)) {
            success = false;
            errorMsg = "Phone number can only contain numbers (or + at start).";
            problem = "telephone";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }



    var atposition=user.email.indexOf("@");  
    var dotposition=user.email.lastIndexOf(".");  
    if (atposition < 1 || dotposition < atposition + 2 || dotposition + 2 >= user.email.length) {  
        success = false;
        errorMsg = "Please enter a valid e-mail address.";
        problem = "email";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }  


    if (user.experience.length > 100) {
        success = false;
        errorMsg = "Experience description can't be longer than 100 characters.";
        problem = "experience";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    return {success: success, errorMsg: errorMsg, problem: problem}; 
} 