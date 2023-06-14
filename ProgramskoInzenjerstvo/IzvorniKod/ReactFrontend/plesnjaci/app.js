const express = require("express");
const { createProxyMiddleware } = require("http-proxy-middleware");
require("dotenv").config();
const path = require("path")

var cors = require('cors');
const app = express();
app.use(cors());

// Configuration
const { PORT } = process.env;
const { HOST } = process.env;
const { API_BASE_URL } = process.env;
//const API_BASE_URL = "https://plesnjaci.onrender.com";

// Proxy
app.use(
    "/spring",
    createProxyMiddleware({
        target: API_BASE_URL,
        changeOrigin: true,
    })
);

app.use("/api", createProxyMiddleware({ 
    target: 'https://freeimage.host/', //original url
    changeOrigin: true, 
    //secure: false,
    onProxyRes: function (proxyRes, req, res) {
        proxyRes.headers['Access-Control-Allow-Origin'] = '*';
    }
}));

app.use(express.static(path.join(__dirname, 'build')))

app.listen(PORT, HOST, () => {
    console.log(`Starting Proxy at ${HOST}:${PORT}`);
});

app.get("*", async (req, res) => {
    res.sendFile(path.join(__dirname, 'build', 'index.html'))
    }
);
