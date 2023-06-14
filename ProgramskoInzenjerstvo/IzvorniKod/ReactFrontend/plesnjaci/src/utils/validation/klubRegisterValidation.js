
export const validateKlubRegistration = (club) => {
    
    let success = true;
    let errorMsg = "";
    let problem = "";
    for (var key in club) {
        if ((key==="clubName" || key==="telephone"  || key==="email" || key==="description") && club[key].trim() === "") {
            success = false;
            errorMsg = "Fill out all required fields.";
            problem = key;
            return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }

    if (club.clubName.length > 50) {
        success = false;
        errorMsg = "Club name can't be longer than 20 characters.";
        problem = "clubName";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }
    //kakav je format imena kluba???
    for (var i = 0; i < club.clubName.length; i++) {
        let char = club.clubName.charAt(i);
        if ((char<'a' || char>'z') &&  (char<'A' || char>'Z') &&(char<'0' || char>'9') && char !== '_' && char !== ' ') {
            success = false;
            errorMsg = "Club name can only contain lowercase letters, numbers or underscores(_).";
            problem = "clubName";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }

    if (club.telephone.length < 5) {
        success = false;
        errorMsg = "Phone number must be atleast 5 characters long.";
        problem = "telephone";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    if (club.telephone.length > 20) {
        success = false;
        errorMsg = "Phone number can't be longer than 20 characters.";
        problem = "telephone";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }
    for (i = 0; i < club.telephone.length; i++) {
        let char = club.telephone.charAt(i);
        if ((char<'0' || char>'9') && !(char === '+' &&  i === 0)) {
            success = false;
            errorMsg = "Phone number can only contain numbers (or + at start).";
            problem = "telephone";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
        }
    }

    var atposition=club.email.indexOf("@");  
    var dotposition=club.email.lastIndexOf(".");  
    if (atposition < 1 || dotposition < atposition + 2 || dotposition + 2 >= club.email.length) {  
        success = false;
        errorMsg = "Please enter a valid e-mail address.";
        problem = "email";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    //ne znam kakva ce bit validacija, a i ne zna se jos jel ce taj atribut uopce ostat

    if (club.description.length > 100) {
        success = false;
        errorMsg = "Club description can't be longer than 100 characters.";
        problem = "description";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    return {success: success, errorMsg: errorMsg, problem: problem}; 
} 