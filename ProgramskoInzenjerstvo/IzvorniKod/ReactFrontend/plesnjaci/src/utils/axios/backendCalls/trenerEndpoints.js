import axios from "../apiClient";

export const becomeTrainer = async (vlasnik, imeKluba, motivacijskoPismo, fileName, trener) => {
    const response = await axios.post("/spring/klub/register-prijava", {
        motivacijskoPismo: motivacijskoPismo,
        potvrda: fileName,
    }, {
        params: {
            korisnickoImeTrenera: trener,
            imeKluba: imeKluba,
            korisnickoImeVlasnika: vlasnik
        }
    }
    );
    return response.data; 
};


export const getPotvrdeniTreneriOdKluba = async (klubId) => {
    const response = await axios.get("/spring/klub/potvrdeni-treneri",{
        params: {
            klubId: klubId
        }
    }
    );
    return response.data; 
};

export const getNepotvrdeniTreneriOdKluba = async (klubId) => {
    const response = await axios.get("/spring/klub/nepotvrdeni-treneri",{
        params: {
            klubId: klubId
        }
    }
    );
    return response.data; 
};


export const fetchPrijava = async (vlasnik, klub, trener) => {
    const response = await axios.get("/spring/klub/fetch-prijava", {
        params: {
            korisnickoImeTrenera: trener,
            imeKluba: klub,
            korisnickoImeVlasnika: vlasnik
        }
    });
    return response.data;
}

export const prihvatiPrijavu = async (vlasnik, klub, trener) => {
    const response = await axios.put("/spring/klub/potvrdi-prijavu", {}, {
        params: {
            korisnickoImeTrenera: trener,
            imeKluba: klub,
            korisnickoImeVlasnika: vlasnik
        }
    });
    return response.data;
}

export const odbijPrijavu = async (vlasnik, klub, trener) => {
    const response = await axios.delete("/spring/klub/odbij-prijavu",{
        params: {
            korisnickoImeTrenera: trener,
            imeKluba: klub,
            korisnickoImeVlasnika: vlasnik
        }
    });
    return response.data;
}

export const dohvatiKluboveOdTrenera = async (trener) => {
    const response = await axios.get("/spring/klub/filterTrener", {
        params: {
            korisnickoIme: trener
        }
    });
    return response.data;
}