
export const validateLogin = (user) => {
    
    let success = true;
    let errorMsg = "";
    let problem = "";
    for (var key in user) {
        if (user[key].trim() === "" ) {
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



    if (user.password.length < 5) {
        success = false;
        errorMsg = "Password must be atleast 5 characters long.";
        problem = "password";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    if (user.password.length > 20) {
        success = false;
        errorMsg = "Password can't be longer than 20 characters.";
        problem = "password";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    return {success: success, errorMsg: errorMsg, problem: problem}; 
} 