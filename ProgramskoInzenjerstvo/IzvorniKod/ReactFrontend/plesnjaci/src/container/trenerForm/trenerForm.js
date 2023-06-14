import "./trenerForm.css"
import { useEffect, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { validateTrenerForm } from "../../utils/validation/trenerFormValidation";
import { useCookies } from 'react-cookie';
import { createClient } from '@supabase/supabase-js'
import { becomeTrainer } from "../../utils/axios/backendCalls/trenerEndpoints";
import activeHeader from "../../utils/activeHeader";

const TrenerForm = (props) => {

    const navigate = useNavigate();

    var imeIVlasnik = window.location.pathname.indexOf("club/") + 5;
    imeIVlasnik = window.location.pathname.substring(imeIVlasnik);
    const vlasnik = imeIVlasnik.substring(0, imeIVlasnik.indexOf("/"));
    const imeKluba = imeIVlasnik.substring(imeIVlasnik.indexOf("/") + 1, imeIVlasnik.lastIndexOf("/")).replaceAll("%20", " ");

    const [waiting, setWaiting] = useState(false);

    const location = useLocation();
    let from = "";
    if (location.state) {
        if (location.state.from) {
            from = location.state.from;
        }
    }
    const [state, setState] = useState({
        motivacijsko: "", potvrda: ""
    });
    const [cookies, setCookie] = useCookies(['user']);
    const [error, setError] = useState("");


    const supabaseUrl = 'https://kegpfrlpsvonohcjccaj.supabase.co'
    const supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtlZ3Bmcmxwc3Zvbm9oY2pjY2FqIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NzIyNTM3NjgsImV4cCI6MTk4NzgyOTc2OH0.xlobxPj-8BkTzyBCWgkBmCKFav8RHrlRLnwuyvtgTxI"
    const supabase = createClient(supabaseUrl, supabaseKey)
    

    function handleChange(event){
        if (event.target.name === "motivacijsko") {
            setState({
                ...state,
                motivacijsko: event.target.value
            });
        };
    }

    async function handleClick(event) {
        setError("");

        let validation = validateTrenerForm(state);

        if (!validation.success) {
            setError(validation.errorMsg);
        }
        else {
            setWaiting(true);
            const fileName = Date.now() + state.potvrda.name;
            try {
                const { data, error } = await supabase.storage
                    .from('trainer-certificates')
                    .upload("public/" + fileName, state.potvrda)
                if (error) {
                    setWaiting(false);
                    setError("Something went wrong. Please try again later.");
                    return;
                }
            }
            catch (error) {
                setWaiting(false);
                setError("Something went wrong. Please try again later.");
                return;
            }

            // ZA PREUZIMANJE
            //const { data, error } = await supabase.storage.from('trainer-certificates').download("public/" + fileName)
            try{
                const response = await becomeTrainer(vlasnik, imeKluba, state.motivacijsko, fileName, cookies.user.username);
                if (response) {
                    if (response.success) {
                        if (location.state && location.state.from) {
                            setWaiting(false);
                            navigate("/" + location.state.from);
                        }
                        else {
                            setWaiting(false);
                            navigate("/");
                        }
                    } else {
                        setWaiting(false);
                        setError(response.message);
                        return;
                    }
                }
                else {
                    setError("Something went wrong. Please try again later.");
                    return;
                }
            } catch (error) {
                setWaiting(false);
                setError("Something went wrong. Please try again later.");
                return;
            }
        }
    }

    async function handleFileChange(e) {

        if (e.target.files[0]) {
            let file = e.target.files[0];
            setState({
                ...state,
                potvrda: file,
            });
        }
        else {
            setState({
                ...state,
                potvrda: "",
            });
        }
    }

    function redirectIfNotLoggedIn() {
        if (!cookies.user) {
            navigate("/login", { state: { from: "club/" + vlasnik + "/" + imeKluba + "/become-trainer" } });
        }
    }

    useEffect(() => { 
        activeHeader(document, "MY CLUBS");
        redirectIfNotLoggedIn();
    }, []);


    return (
        <div className="login-box">
            <h2>Become a trainer of "{imeKluba}"</h2>
        <form>
            <div className="user-box">
            <input type="file" accept="application/pdf" onChange={handleFileChange} multiple={false} />
                <label>Trainer certificate</label>
            </div>

            <div className="user-box">
            <textarea rows="10" className="motivacijsko-pismo" name="motivacijsko" value={state.motivacijsko} onChange={handleChange}/>
                <label>Why do you want to become a trainer?</label>
            </div>

                <input className="buttonP" type="button" value="Send request" onClick={handleClick} disabled={waiting} />
            {error && <h1 className="errorMsg">{error}</h1>}
        </form>
        </div>
    );
};

export default TrenerForm;