import { useEffect, useState } from "react";
import { registerPles } from "../../utils/axios/backendCalls/plesEndpoints";
import "./registerPles.css"
import { useNavigate, useLocation } from "react-router-dom";
import { validatePles } from "../../utils/validation/plesValidation";
import { useCookies } from 'react-cookie';
import { hostImage } from "../../utils/axios/imageHost";
import activeHeader from "../../utils/activeHeader";
import { handleFileChange } from "../../utils/resizeFile";

const RegisterPles = (props) => {

    const navigate = useNavigate();

    const location = useLocation();
    let from = "";
    if (location.state) {
        if (location.state.from) {
            from = location.state.from;
        }
    }
    const [state, setState] = useState({
        naziv: "", link: "", opis: "",
        slika: ""
    });

    const [waiting, setWaiting] = useState(false);

    const [cookies, setCookie] = useCookies(['user']);

    const [error, setError] = useState("");
    

    function handleChange(event){
        if (event.target.name === "naziv") {
            setState({
                ...state,
                naziv: event.target.value
            });
        };

        if (event.target.name === "link") {
            setState({
                ...state,
                link: event.target.value
            });
        };
        if (event.target.name === "opis") {
            setState({
                ...state,
                opis: event.target.value
            });
        };
    }

    async function handleClick(event) {
        let validation = validatePles(state);

        if (!validation.success) {
            setError(validation.errorMsg);
        }
        else {
            setError("");
            setWaiting(true);
        //validation...
            var imageLink = "";
            try {
                if (state.slika) {
                    const response = await hostImage(state.slika.split(",")[1]);
                    if (response) {
                        if (response["status_code"] === 200) {
                            imageLink = response.image.display_url;
                        }
                        else {
                            imageLink = "";
                        }
                    }
                }
            
                const response = await registerPles({
                    ...state,
                    slika: imageLink
                });
                if (response) {
                    if (response.success) {
                        setWaiting(false);
                        if (location.state && location.state.from) {
                            navigate("/" + location.state.from);
                        }
                        else {
                            navigate("/");
                        }

                    } else {
                        setWaiting(false);
                        setError(response.message);
                        return;
                    }
                }
            } catch (error) {
                setWaiting(false);
                setError(error.message);
                return;
            }
        return;
    } 
    }
    
    function redirectIfNotLoggedIn() {
        if (!cookies.user) {
            navigate("/login", { state: { from: "dance/new" } });
        }
    }

    useEffect(() => { 
        activeHeader(document, "ADMIN");
        redirectIfNotLoggedIn();
    }, []);

    return cookies.user?.type==="Administrator" ? (
        <div className="login-box">
        <h2>Add Dance Here</h2>
        <form>
            <div className="user-box">
            <input type="text" name="naziv" value={state.naziv} onChange={handleChange} required />
                <label>Dance Name</label>
            </div>
            <div className="user-box">
            <input type="text" name="link" value={state.link} onChange={handleChange} required/>
                <label>Link</label>
            </div>
            <div className="user-box">
            <input type="file" accept="image/*" onChange={ (e) => handleFileChange(e, state, setState) } multiple={false} />
                <label>Photo</label>
            </div>

            <div className="user-box">
            <textarea name="opis" value={state.opis} onChange={handleChange}/>
                <label>Description</label>
            </div>

            <input className="buttonP" type="button" value="Add dance" onClick={handleClick} disabled={waiting} />
            {error && <h1 className="errorMsg">{error}</h1>}
        </form>
        </div>
    ) : <h1 className="unauthorized">Unauthorized</h1>;
};

export default RegisterPles;