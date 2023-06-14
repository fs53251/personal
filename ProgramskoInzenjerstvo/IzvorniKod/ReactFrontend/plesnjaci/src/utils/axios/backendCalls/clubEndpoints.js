import axios from "../apiClient";

export const klubRegister = async (club) => {
    const response = await axios.post("/spring/klub/register", {
        //korisnickoImeVlasnika: club.owner,
        imeKluba: club.clubName,
        telefon: club.telephone,
        email: club.email,
        poveznica: club.link,
        opis: club.description,
        jePotvrden: false,
        //dvorana: null
    }, {
        params: {
            korisnickoImeVlasnika: club.owner,
            dvorana: club.location
        }
    });
    return response.data; 
};


export const getKlub = async (imeKluba, korisnickoImeVlasnika) => {
    const response = await axios.get("/spring/klub/fetch", {
        params: {
            imeKluba: imeKluba,
            korisnickoImeVlasnika: korisnickoImeVlasnika
        }
    });
    return response.data;
}

export const editKlub = async (club) => {
    const response = await axios.put("/spring/klub/update", {
        klubId: club.id,
        imeKluba: club.clubName,
        telefon: club.telephone,
        email: club.email,
        poveznica: club.link,
        opis: club.description,
        //jePotvrden: club.isConfirmed,
        jePotvrden: true,
        dvorana: club.location,
        //korisnickoImeVlasnika: club.owner
    }, {
        params: {
            korisnickoImeVlasnika: club.owner,
            imeKluba: club.clubName
        }
    });
    return response.data;
}

export const getAllKlubovi = async () => {
    const response = await axios.get("/spring/klub/all");
    return response.data;
}

export const getAllPotvrdeniKlubovi = async () => {
    const response = await axios.get("/spring/klub/potvrdeni");
    return response.data;
}

export const getAllNepotvrdeniKlubovi = async () => {
    const response = await axios.get("/spring/klub/nepotvrdeni");
    return response.data;
}

export const potvrdiKlub = async (imeKluba, korisnickoImeVlasnika) => {
    const response = await axios.put("/spring/klub/potvrdi", null, {
        params: {
            imeKluba: imeKluba,
            korisnickoImeVlasnika: korisnickoImeVlasnika
        }
    });
    return response.data;
}


export const deleteKlub = async (imeKluba, korisnickoImeVlasnika) => {
    const response = await axios.delete("/spring/klub/delete", {
        params: {
            imeKluba: imeKluba,
            korisnickoImeVlasnika: korisnickoImeVlasnika
        }
    });
    return response.data;
}
export const klubFilter = async (korisnickoImeVlasnika) => {
    const response = await axios.get("/spring/klub/filterKorisnik", {
        params: {
            korisnickoIme: korisnickoImeVlasnika
        }
    });
    return response.data;
}

export const getPlesovi = async (klubId) => {
    const response = await axios.get("/spring/ples/klub/tecajevi", {
        params: {
            klubId: klubId
        }
    });
    return response.data;
}