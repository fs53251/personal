import axios from "axios";

/* if (window.location.hostname === "localhost") {
    var port = 8080; // Depends on backend port

    axios.defaults.baseURL = "http://" + window.location.hostname + ":" + port;
} else {
    axios.defaults.baseURL = "production url";
} */

// axios.interceptors.request.use(
//     function (config) {
//         // Do something before request is sent
//         console.log(`REQUEST : ${config.url}\n\nJSON : ${JSON.stringify(config)}`);

//         return config;
//     },
//     function (error) {
//         // Do something with request error
//         console.log(`API request error : ${JSON.stringify(error)}`);

//         return error;
//     }
// );

// axios.interceptors.response.use(
//     function (response) {
//         // Do something before request is sent
//         console.log(`RESPONSE : ${response.config.url} - Status : ${response.status} \n\nJSON : ${JSON.stringify(response)}`);

//         return response;
//     },
//     function (error) {
//         // Do something with request error
//         console.log(`API response error : ${JSON.stringify(error)}`);

//         return error;
//     }
// );

export default axios;
