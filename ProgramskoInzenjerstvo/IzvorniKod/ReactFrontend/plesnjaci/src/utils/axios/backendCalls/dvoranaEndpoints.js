import axios from "../apiClient";

export const getAllDvorane = async () => {
    const response = await axios.get("/spring/dvorana/all");
    return response.data;
}