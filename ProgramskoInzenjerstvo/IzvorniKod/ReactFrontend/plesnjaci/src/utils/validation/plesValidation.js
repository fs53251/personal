function isValidUrl(string) {
    let url;
    try {
      url = new URL(string);
    } catch (_) {
      return false;
    }
    return true;
}
export const validatePles = (ples) => {
    
    let success = true;
    let errorMsg = "";
    let problem = "";
    if(!ples.naziv){
        success = false;
        errorMsg = "Dance name must not be empty.";
        problem = "naziv";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }
    if(!isValidUrl(ples.link)){
        success = false;
        errorMsg = "Dance link must be valid.";
        problem = "link";
        return {success: success, errorMsg: errorMsg, problem: problem}; 
    }

    return {success: success, errorMsg: errorMsg, problem: problem}; 
} 