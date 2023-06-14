import { useEffect } from "react";
import { useCookies } from 'react-cookie'
import { useNavigate } from "react-router-dom";

const LogoutPage = () => {

    const [cookies, setCookie, removeCookie] = useCookies(['user']);
    const navigate = useNavigate();

    useEffect(() => {
        removeCookie('user');
        if (cookies.user) {
            removeCookie('user');
            window.location.reload();
        }
        else {
            navigate("/");
        }
    }, []);
    return (
        <div>
        </div>
    );
}

export default LogoutPage;