export const hash = (password) => {
    var jsSHA = require("jssha");
    var hashObj = new jsSHA("SHA-512", "TEXT", { numRounds: 1 });
    hashObj.update(password);
    var hash = hashObj.getHash("HEX");
    return hash;
} 