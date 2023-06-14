export const validateTrenerForm = (trener)  => {
    
    if(trener.potvrda === ""){
        return {success: false, errorMsg: "You must upload a certificate."};
    }
    if(trener.motivacijsko === ""){
        return {success: false, errorMsg: "You must write a motivational letter."};
    }
    if(trener.motivacijsko.length > 500){
        return {success: false, errorMsg: "Motivational letter can't be longer than 500 characters."};
    }
    if (trener.potvrda.size > 1000000) {
        return { success: false, errorMsg: "Certificate can't be larger than 1MB." };
    }

    return {success: true, errorMsg: ""};
}