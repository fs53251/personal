export const validateNewEvent = (state) => {
    if (!state.eventName) {
        return {
            success: false,
            errorMsg: "Event name is required"
        };
    }
    if (!state.description) {
        return {
            success: false,
            errorMsg: "Description is required"
        };
    }
    if((state.description).length>500){
        return {
            success: false,
            errorMsg: "Description is too long"
        };
    }
    if (!state.slika) {
        return {
            success: false,
            errorMsg: "Photo is required"
        };
    }
    if (!state.startingTime) {
        return {
            success: false,
            errorMsg: "Starting time is required"
        };
    }
    return {
        success: true
    };
}

export const validateEvent = (state) => {
    if (!state.naziv) {
        return {
            success: false,
            errorMsg: "Event name is required"
        };
    }
    if (!state.opis) {
        return {
            success: false,
            errorMsg: "Description is required"
        };
    }
    if((state.opis).length>500){
        return {
            success: false,
            errorMsg: "Description is too long"
        };
    }
    if (!state.slika) {
        return {
            success: false,
            errorMsg: "Photo is required"
        };
    }
    if (!state.vrijeme) {
        return {
            success: false,
            errorMsg: "Starting time is required"
        };
    }
    return {
        success: true
    };
}