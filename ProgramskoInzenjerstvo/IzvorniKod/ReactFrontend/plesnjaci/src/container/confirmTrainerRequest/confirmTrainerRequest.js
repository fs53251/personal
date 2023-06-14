import { createClient } from "@supabase/supabase-js";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";
import { fetchPrijava, odbijPrijavu, prihvatiPrijavu } from "../../utils/axios/backendCalls/trenerEndpoints";
import "./confirmTrainerRequest.css"
import { useCookies } from 'react-cookie';
const ConfirmTrainerRequest = () => {

    var imeIVlasnik = window.location.pathname.indexOf("club/") + 5;
    imeIVlasnik = window.location.pathname.substring(imeIVlasnik);
    imeIVlasnik = imeIVlasnik.split("/confirm-trainer/");

    const trener = imeIVlasnik[1];

    imeIVlasnik = imeIVlasnik[0].split("/");

    const vlasnik = imeIVlasnik[0]
    const imeKluba = imeIVlasnik[1].replaceAll("%20", " ");

    const supabaseUrl = 'https://kegpfrlpsvonohcjccaj.supabase.co'
    const supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtlZ3Bmcmxwc3Zvbm9oY2pjY2FqIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NzIyNTM3NjgsImV4cCI6MTk4NzgyOTc2OH0.xlobxPj-8BkTzyBCWgkBmCKFav8RHrlRLnwuyvtgTxI"
    const supabase = createClient(supabaseUrl, supabaseKey)


    const [cookies] = useCookies(['user']);

    const [prijava, setPrijava] = useState({});
    const [error, setError] = useState("");

    const dohvatiPrijavu = async () => {
        try {
            const response = await fetchPrijava(vlasnik, imeKluba, trener);
            if (response.success) {
                setPrijava(response.prijava);
                return
            }
            else {
                setError("Error fetching request");
                return
            }
        }
        catch {
            setError("Error fetching request");
        }
    }

    const skiniPotvrdu = async () => {
        const { data, error } = await supabase.storage.from('trainer-certificates').download("public/" + prijava.potvrda);
        if (window.navigator && window.navigator.msSaveOrOpenBlob) {
            window.navigator.msSaveOrOpenBlob(data, prijava.potvrda);
        } else {
            var objectUrl = URL.createObjectURL(data);
            window.open(objectUrl);
        }
    }

    const handleAccept = async () => {
        try {
            const response = await prihvatiPrijavu(vlasnik, imeKluba, trener);
            if (response.success) {
                navigate(-1);
            }
            else {
                setError("Error confirming request");
            }
        }
        catch {
            setError("Error confirming request");
        }
    }

    const handleDecline = async () => {
        try {
            const { data, error } = await supabase.storage.from('trainer-certificates').remove(["public/" + prijava.potvrda]);
            if (error) {
                setError("Error deleting file");
            }
            
            const response = await odbijPrijavu(vlasnik, imeKluba, trener);
            if (response.success) {
                navigate(-1);
            }
            else {
                setError("Error declining request");
            }
        }
        catch {
            setError("Error deleting trainer request");
        }
    }

    const navigate = useNavigate();

    useEffect(() => {
        activeHeader(document, "MY CLUBS");
        dohvatiPrijavu();
    }, []);

    return (cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") ? (

    <div className="trainer-box">
        <h2>Accept User As a Trainer</h2>
        <form>
            <div className="user-box">
                <h1 className="link" onAuxClick={(e) => window.open("/profile/" + trener, '_blank', 'noreferrer')} onClick={(e) => navigate("/profile/" + trener)}>{trener}</h1> 
                <label>User Profile:</label>
            </div>

            <div className="user-box">
                <h1 className="link" onAuxClick={skiniPotvrdu} onClick={skiniPotvrdu}>{prijava.potvrda}</h1>
                <label>Trainer Certificate:</label>
            </div>

            <div className="user-box">
                <textarea disabled={true} value={prijava.motivacijskoPismo}/>
                <label>Motivational Letter:</label>
            </div>
        
            <div className="trainer-buttons">
                <input type="button" className="trainer-button" value="Accept" onClick={handleAccept} />
                <input type="button" className="trainer-button-decline" value="Decline" onClick={handleDecline} />
            </div>

            <div className="error-naslov">
                    {error && <h1 className="errorMsg">{error}</h1>}
            </div>
    
        </form>
        </div>
        ) : <div className="error-naslov"> <h1 className="errorMsg">You are not the owner of this club</h1> </div>
};

export default ConfirmTrainerRequest;