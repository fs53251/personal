const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  app.use(
    "/spring",
    createProxyMiddleware({
      target: "http://localhost:8080/",
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
};
