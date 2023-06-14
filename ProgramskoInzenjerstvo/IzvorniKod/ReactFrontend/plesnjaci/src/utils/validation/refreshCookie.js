export const refreshCookie = (cookies, setCookie) => {

    const user = cookies.user;

    if (user) {
        let d = new Date();
        d.setTime(d.getTime() + (60*60*1000)); //60 min

        setCookie("user", user, {path: "/", expires: d});
    }
}