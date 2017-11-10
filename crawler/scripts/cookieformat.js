exports.jsCookies = function(cookies) {
    var string = "";
    string += "# Netscape HTTP Cookie File\n";
    string += "# http://curl.haxx.se/rfc/cookie_spec.html\n";
    for (var i = 0; i < cookies.length; i++) {
        cookie = cookies[i];
        string += cookie.domain + "\t" +
            'FALSE' + "\t" +
            cookie.path + "\t" +
            cookie.secure.toString().toUpperCase() + "\t" +
            ((cookie.expiry != undefined) ? cookie.expiry : "") + "\t" +
            cookie.name + "\t" +
            cookie.value + ((i == cookies.length - 1) ? "" : "\n");
    }
    return string;
};