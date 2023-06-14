export const validateNewCourse = (state) => {
    if (!state.courseDescription) {
        return {
            success: false,
            errorMsg: "Course description is required"
        };
    }
    if (!state.deadline) {
        return {
            success: false,
            errorMsg: "Deadline is required"
        };
    }
    if (!state.capacity) {
        return {
            success: false,
            errorMsg: "Capacity is required"
        };
    }
    if (!state.plesId) {
        return {
            success: false,
            errorMsg: "Dance type is required"
        };
    }
    if (!state.trenerId) {
        return {
            success: false,
            errorMsg: "Trainer is required"
        };
    }
    if(state.courseDescription.length>500){
        return {
            success: false,
            errorMsg: "Description is too long"
        };
    }
    if(state.courseLimitation.length>500){
        return {
            success: false,
            errorMsg: "Limitation is too long"
        };
    }
    return {
        success: true
    };
}

export const validateCourse = (state) => {
    if (!state.opis) {
        return {
            success: false,
            errorMsg: "Course description is required"
        };
    }
    if (!state.rokPrijave) {
        return {
            success: false,
            errorMsg: "Deadline is required"
        };
    }
    if (!state.kapacitetGrupe) {
        return {
            success: false,
            errorMsg: "Capacity is required"
        };
    }
    if (!state.tipPlesa.tipPlesaId) {
        return {
            success: false,
            errorMsg: "Dance type is required"
        };
    }
    if (!state.trener.klijentId) {
        return {
            success: false,
            errorMsg: "Trainer is required"
        };
    }
    if(state.opis.length>500){
        return {
            success: false,
            errorMsg: "Description is too long"
        };
    }
    if(state.ogranicenja.length>500){
        return {
            success: false,
            errorMsg: "Limitation is too long"
        };
    }
    return {
        success: true
    };
}