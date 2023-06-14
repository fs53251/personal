import axios from "../apiClient";

export const registerPles = async (ples) => {
    const response = await axios.post("/spring/ples/add", {
        naziv: ples.naziv,
        opis: ples.opis,
        slika: ples.slika,
        link: ples.link     
    });
    return response.data; 
};

export const getPles = async (imePlesa) => {
    const response = await axios.get("/spring/ples/fetch/" + imePlesa);
    return response.data;
}

export const getAllPlesovi = async () => {
    const response = await axios.get("/spring/ples/all");
    return response.data;
}

export const editPles = async (ples) => {
    const imePlesa =window.location.pathname.split("/dance/")[1];
    const response = await axios.post("/spring/ples/update/" + imePlesa, {
        tipPlesaId: ples.tipPlesaId,
        naziv: ples.naziv,
        opis: ples.opis,
        slika: ples.slika,
        link: ples.link
    });
    return response.data;
}

export const deletePles = async (imePlesa) => {
    const response = await axios.get("/spring/ples/delete/"+imePlesa);
    return response.data;
    }