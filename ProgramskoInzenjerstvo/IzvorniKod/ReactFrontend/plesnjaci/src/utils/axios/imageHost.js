import axios from "./apiClient";
export const hostImage = async (image) => {
    const formData = new FormData();
    formData.append('key', "6d207e02198a847aa98d0a2a901485a5");
    formData.append('action', "upload");
    formData.append('source', image);
    formData.append('format', "json");
    const config = {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    }
    const response = await axios.post("/api/1/upload", formData, config);
    return response.data; 
} 