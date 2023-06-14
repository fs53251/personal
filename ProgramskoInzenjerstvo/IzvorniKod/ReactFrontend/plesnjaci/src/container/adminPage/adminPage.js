import { useCookies } from "react-cookie";
import { useEffect } from "react";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import "./adminPage.css"
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";

const AdminPage = () => {
    
    const [cookies, setCookie] = useCookies(['user'])
    const user = cookies.user;
    
    const navigate = useNavigate();

    function redirectIfNotLoggedIn() {
        if (!user) {
            navigate("/login", { state: { from: "admin" } });
        }
    }

    function handleClick(e) {
        if (e.target.name === "clients") {
            navigate("/admin/clients")
        }
        else if (e.target.name === "clubs") {
            navigate("/admin/clubs")
        }
        else if (e.target.name === "dances") {
            navigate("/admin/dances")
        }
        else if (e.target.name === "clubs") {
            navigate("/admin/clubs")
        }
    }

    useEffect(() => {
        activeHeader(document, "ADMIN");
        redirectIfNotLoggedIn();
        refreshCookie(cookies, setCookie);
    }, []);
    return user?.type==="Administrator" ? (
        <div className="admin-page">
            <div className="admin-buttons">
                <input className="admin-button" type="button" name="clients" value="See all clients" onClick={e => handleClick(e)} /> <br/> <br/>
                <input className="admin-button" type="button" name="clubs" value="See all clubs" onClick={e => handleClick(e)} /> <br/> <br/>
                <input className="admin-button" type="button" name="dances" value="See all dances" onClick={e => handleClick(e)} /> <br />
            </div>
        </div >
    ) : <h1 className="unauthorized">Unauthorized</h1>;
};

export default AdminPage;