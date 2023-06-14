import axios from "../apiClient";

export const addTecaj = async (tecaj, termini, adresa) => {
    if (tecaj.deadline.split(" ")[1].length === 5) {
        tecaj.deadline += ":00";
    }
    const response = await axios.post("/spring/tecaj/add", {
        tecaj: {
            rokPrijave: tecaj.deadline,
            kapacitetGrupe: tecaj.capacity,
            opis: tecaj.courseDescription,
            ogranicenja: tecaj.courseLimitation,
        },
        termini: termini.map(termin => (
            {
                vrijeme: termin,
                adresaDvorane: adresa
            }
            )
        )
    }, {
        params: {
            trenerId: tecaj.trenerId,
            klubId: tecaj.klubId,
            plesId: tecaj.plesId
        }
    });
    return response.data;
}


export const getTecajeviOdKluba = async (imeKluba, vlasnik) => {
    const response = await axios.get("/spring/tecaj/filterKlub", {
        params: {
            korisnickoImeVlasnika: vlasnik,
            imeKluba: imeKluba
        }
    });
    return response.data;
}


export const getTecaj = async (id) => {
    const response = await axios.get("/spring/tecaj/fetch", {
        params: {
            tecajId: id,
        }
    });
    return response.data;
}


export const deleteTecaj = async (id) => {
    const response = await axios.delete("/spring/tecaj/delete", {
        params: {
            tecajId: id
        }
    });
    return response.data;
}

export const editTecaj = async (tecaj, termini) => {
    if (tecaj.rokPrijave.split(" ")[1].length === 5) {
        tecaj.rokPrijave += ":00";
    }
    const response = await axios.put("/spring/tecaj/update", {
        tecaj: {
            tecajId: tecaj.tecajId,
            rokPrijave: tecaj.rokPrijave,
            kapacitetGrupe: tecaj.kapacitetGrupe,
            opis: tecaj.opis,
            ogranicenja: tecaj.ogranicenja,
            klub: tecaj.klub,
        },
        termini: termini.map(termin => (
            {
                vrijeme: termin,
                adresaDvorane: tecaj.klub.dvorana.adresa
            }
            )
        )
    }, {
        params: {
            plesId: tecaj.tipPlesa.tipPlesaId,
            trenerId: tecaj.trener.klijentId,
        }
    });
    return response.data;
}

export const filterTecajByPles = async (idPlesa)=> {
    const response= await axios.get("/spring/tecaj/filterTipPlesa", {
        params: {
            tipPlesaId:idPlesa
        }
    });
    return response.data;
}

export const dohvatiTermineTecaja = async (idTecaja) => {
    const response = await axios.get("/spring/termin/filterTecaj", {
        params: {
            tecajId: idTecaja
        }
    });
    return response.data;
}


export const upisiTecaj = async (idKorisnika, idTecaja) => {
    const response = await axios.get("/spring/tecaj/upisiTecaj", {
        params: {
            tecajId: idTecaja,
            korisnikId: idKorisnika
        }
    });
    return response.data;
}


export const sviUpisiZaTecaj = async (tecajId) => {
    const response = await axios.get("/spring/tecaj/all/filterTecaj", {
        params: {
            tecajId: tecajId
        }
    });
    return response.data;
}


export const prihvatiUpisZaTecaj = async (upisId) => {
    const response = await axios.get("/spring/tecaj/potvrdi", {
        params: {
            upisTecajId: upisId
        }
    });
    return response.data;
}

export const odbijUpisZaTecaj = async (upisId) => {
    const response = await axios.get("/spring/tecaj/makniPotvrdu", {
        params: {
            upisTecajId: upisId
        }
    });
    return response.data;
}


export const fetchAllTermini = async (username) => {
    const response = await axios.get("/spring/termin/terminiTrenerKlijent", {
        params: {
            username : username
        }
    });
    return response.data;
}