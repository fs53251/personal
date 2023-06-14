import axios from "../apiClient";


export const login = async (user) => {
    const response = await axios.post("/spring/user/login", {
        username: user.username,
        password: user.hashPassword,
    })
    return response.data; 
} 


export const register = async (user) => {
    const response = await axios.post("/spring/user/register", {
        korisnicko_ime: user.username,
        lozinka: user.hashPassword,
        ime: user.firstName,
        prezime: user.lastName,
        spol: user.gender,
        datumRodenja: user.DOB,
        brojMobitela: user.telephone,
        email: user.email,
        plesnoIskustvo: user.experience,
        fotografija: user.photo,
        tipKorisnika: "Klijent"

    });
    return response.data; 
};


export const editProfile= async(user) => {
    const response = await axios.put("/spring/user/update", {
        klijentId: user.id,
        korisnicko_ime: user.username,
        ime: user.firstName,
        prezime: user.lastName,
        spol: user.gender,
        datumRodenja: user.DOB,
        brojMobitela: user.telephone,
        email: user.email,
        plesnoIskustvo: user.experience,
        fotografija: user.photo,
        tipKorisnika: user.type

    });
    return response.data;
}


export const changePassword = async (username, oldPassword, newPassword) => {
    const response = await axios.post("/spring/user/changePassword",{
            korisnicko_ime: username,
            stara_lozinka: oldPassword,
            nova_lozinka: newPassword
    });
    return response.data; 
};


export const deleteUser = async (username) => {
    const response = await axios.delete("/spring/user/delete/" + username);
    return response.data;
};

export const getAllUsers = async () => {
    const response = await axios.get("/spring/user/all");
    return response.data;
}

export const getUser = async (username) => {
    const response = await axios.get("/spring/user/fetch/" + username);
    return response.data;
}

export const toggleAdministrator = async (username) => {
    const response = await axios.get("/spring/user/tipKorisnika/izmjena", {
        params: {
            username: username,
        }
    });
    return response.data;
}