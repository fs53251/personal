import axios from "../apiClient";

export const addPlesnjak = async (plesnjak, vlasnik, imeKluba) => {
    if (plesnjak.startingTime.split(" ")[1].length === 5) {
        plesnjak.startingTime += ":00";
    }
    const response = await axios.post("/spring/plesnjak/add", {
        newPlesnjak: {
            naziv: plesnjak.eventName,
            slika: plesnjak.photo,
            opis: plesnjak.description,
            vrijeme: plesnjak.startingTime,
        },
        tipoviPlesa: plesnjak.dances
    }, {
        params: {
            imeKluba: imeKluba,
            korisnickoImeVlasnika: vlasnik,
            dvorana: plesnjak.location
        }
    });
    return response.data;
}

export const editPlesnjak = async (plesnjak) => {
    if (plesnjak.vrijeme) {
        if (plesnjak.vrijeme.split(" ")[1].length === 5) {
            plesnjak.vrijeme += ":00";
        }
    }
    const response = await axios.put("/spring/plesnjak/update", {
        ...plesnjak,
    }, {
        params: {
            adresa: plesnjak.dvorana.adresa,
        }
    });
    return response.data;
}

export const deletePlesnjak = async (plesnjakId) => {
    const response = await axios.delete("/spring/plesnjak/delete", {
        params: {
            plesnjakId: plesnjakId,
        }
    });
    return response.data;
}

export const getAllPlesnjaci = async () => {
    const response = await axios.get("/spring/plesnjak/all");
    return response.data;
}

export const getPlesnjaciOdKluba = async (imeKluba, vlasnik) => {
    const response = await axios.get("/spring/plesnjak/filterKlub", {
        params: {
            imeKluba: imeKluba,
            korisnickoImeVlasnika: vlasnik
        }
    });
    return response.data;
}

export const getPlesnjak = async (idPlesnjaka) => {
    const response = await axios.get("/spring/plesnjak/fetch", {
        params: {
            plesnjakId: idPlesnjaka,
        }
    });
    return response.data;
}

export const getPlesnjaciPoPlesu= async (imePlesa) => {
    const response = await axios.get("/spring/plesnjak/filterPles", {
        params: {
            nazivPlesa: imePlesa
        }
    });
    return response.data;
}

export const getPlesoveOdPlesnjaka = async (idPlesnjaka) => {
    const response = await axios.get("/spring/ples/ples/plesnjaci", {
        params: {
            plesnjakId: idPlesnjaka
        }
    });
    return response.data;
}