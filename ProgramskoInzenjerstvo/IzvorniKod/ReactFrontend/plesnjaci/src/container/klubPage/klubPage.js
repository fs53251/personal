import "./klubPage.css"
import { getKlub, getPlesovi } from "../../utils/axios/backendCalls/clubEndpoints";
import { useEffect, useState } from "react";
import { MapContainer, TileLayer} from 'react-leaflet'
import L from "leaflet";
import { useCookies } from "react-cookie";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { useNavigate } from "react-router-dom";
import { IoCloseOutline } from "react-icons/io5";
import { editKlub } from "../../utils/axios/backendCalls/clubEndpoints";
import DeleteKlubPopup from "../deleteClub/deleteClubPopup";
import { blueIcon, violetIcon } from "../../utils/markers";
import activeHeader from "../../utils/activeHeader";
import { validateKlubRegistration } from "../../utils/validation/klubRegisterValidation";
import { getPlesnjaciOdKluba } from "../../utils/axios/backendCalls/plesnjakEndpoints";


const KlubPage = () => {
    const [state, setState] = useState({
        id: "", owner: "", clubName: "",
        telephone: "", email: "", link: "", description: "",
        location: "", isConfirmed : ""
    });
    const [allPlesnjaci, setAllPlesnjaci] = useState([]);

    const [stateBackup, setStateBackup] = useState();
    const [cookies, setCookie] = useCookies(['user']);
    const [popupDelete, setPopupDelete] = useState(false);
    const [waiting, setWaiting] = useState(false);
    const [inEditMode, setInEditMode] = useState(false);
    const [error, setError] = useState("");
    const [plesovi, setPlesovi] = useState([]);

    const navigate = useNavigate();

    var imeIVlasnik = window.location.pathname.indexOf("club/") + 5;
    imeIVlasnik = window.location.pathname.substring(imeIVlasnik);
    const vlasnik = imeIVlasnik.substring(0, imeIVlasnik.indexOf("/"));
    const imeKluba = imeIVlasnik.substring(imeIVlasnik.indexOf("/") + 1);


    async function fetchClub() {
        const response = await getKlub(imeKluba, vlasnik);
        if (response.success) {
            const klub = response.klub;
            setState({
                id: klub.klubId,
                owner: klub.vlasnik.korisnicko_ime,
                clubName: klub.imeKluba,
                telephone: klub.telefon,
                email: klub.email,
                link: klub.poveznica,
                description: klub.opis,
                location: klub.dvorana,
                isConfirmed: klub.jePotvrden
            });
            const plesovi = await getPlesovi(klub.klubId);
            setPlesovi(plesovi.plesovi);
            return klub.dvorana;
        }
        else {
            navigate("/*", { state: { from: "profile" } });
        }
    }

    async function fetchEventiOdKluba() {
        try {
            const response = await getPlesnjaciOdKluba(imeKluba, vlasnik);
            setAllPlesnjaci(response.listaPlesnjaka)
            return response.listaPlesnjaka;
        }
        catch {
            console.log("Error fetching events");
        }
    }

    const clickEdit = async () => {

        if (inEditMode === false) {
            setStateBackup(state);
            setInEditMode(true);
        } else {
            let validation = validateKlubRegistration(state);
            if (!validation.success) {
                setError(validation.errorMsg);
                state.errorMsg = validation.errorMsg; //prikaz error poruke
            }
            else {
                setError("");
                const response = await editKlub({
                    ...state,
                });
                setWaiting(false);
                setInEditMode(false);
            }
        }

    }

    const handleClose = () => {
        setError("");
        setState(stateBackup);
        setInEditMode(false);
    }

    async function handleClickDelete() {
        setPopupDelete(true);
    }
    async function handleClickTrainer() {
        navigate("/club/" + vlasnik + "/" + imeKluba + "/become-trainer", { state: { from: "club/" + vlasnik + "/" + imeKluba } });
    }

    async function handleAllEvents() {
        navigate("/club/" + vlasnik + "/" + imeKluba + "/events", { state: { from: "club/" + vlasnik + "/" + imeKluba } });
    }

    async function handleClickAllTrainers() {
        navigate("/club/" + vlasnik + "/" + imeKluba + "/trainers", { state: { from: "club/" + vlasnik + "/" + imeKluba } });
    }

    async function handleAllCourses() {
        navigate("/club/" + vlasnik + "/" + imeKluba + "/courses", { state: { from: "club/" + vlasnik + "/" + imeKluba } });
    }

    function handleChange(event){
        if (event.target.name === "email") {
            setState({
                ...state,
                email: event.target.value
            });
        };

        if (event.target.name === "phone") {
            setState({
                ...state,
                telephone: event.target.value
            });
        };

        if (event.target.name === "description") {
            setState({
                ...state,
                description: event.target.value
            });
        };
    }

    function GenerateContent() {
        return (
            <div className="popisPlesova">        
                {plesovi.map((ples) => (
                        <a href={"/dance/" + ples} className="plesovi">{ples}</a>
                    )
                )}
            </div>)

    }

    useEffect(() => {
    activeHeader(document, "HOME PAGE");
    refreshCookie(cookies, setCookie);
    }, []);
    
    return (
        <div className="klub-okvir">
            <div className="error-naslov">
                {!state.isConfirmed && <h1 className="errorMsg">Club is not yet confirmed by an administrator.</h1>}
            </div>
            <div className="error-naslov">
                {error && <h1 className="errorMsg">{error}</h1>}
            </div>
                <DeleteKlubPopup
                        trigger={popupDelete}
                        setTrigger={setPopupDelete}
                        imeKluba={state.clubName}
                        vlasnik={state.owner}
                />

                <div className="klub-glavni">
                    <div className="klub-kartica">
                    {state.isConfirmed && <button className="klub-button button-trainer" onClick={handleClickTrainer} disabled={waiting}>Become a trainer</button>}
                        <div className="klub-card">
                            <div className="klub-img">
                                <img src="https://thumbs.dreamstime.com/b/dancing-ballerinas-silhoette-light-pink-white-collors-bit-pixel-ballerina-dancing-ballerinas-silhoette-light-pink-241405635.jpg" alt="Klub photo" />
                            </div>
                            <div className="klub-content">
                                <div className="klub-ime">
                                    <h2>{state.clubName}</h2>
                                </div>
                                <div className="klub-info">
                                    <label>Owner:</label>
                                    <a type="text" href={"/profile/" + state.owner}>{state.owner}</a> 
                                </div>
                                <div className="klub-info">
                                    <label>Email:</label>
                                    <input disabled={ !inEditMode } name="email" className="klub-input" type="text" value={state.email} onChange={handleChange} /> <br /> <br />
                                </div>
                                <div className="klub-info">
                                    <label>Phone:</label>
                                    <input disabled={ !inEditMode } name="phone" className="klub-input" type="text" value={state.telephone} onChange={handleChange}/> <br /> <br />
                                </div>
                                <div className="klub-info">
                                    <label>Description:</label>
                                    <textarea disabled={ !inEditMode } rows="3" name="description" className="klub-input" type="text" value={state.description} onChange={handleChange} /> <br /> <br />
                                </div>
                                <div className="klub-info">
                                    <label>Teaches dances:</label>
                                    {plesovi.length > 0 ? <GenerateContent /> : <input disabled={true} value={"None"}/>}
                                </div>
                            </div>
                            {(cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") &&
                                <div className="klub-buttons">
                                    <div className="editAndClose">
                                        <button id="klub-edit" className="klub-button" onClick={clickEdit} disabled={waiting}>{inEditMode ? "Save" : "Edit"}</button>
                                        {inEditMode &&
                                            <button id="closeedit" className="btn btn-primary closeedit klub-button" onClick={handleClose} disabled={waiting}>
                                                <span className="closeicon"><IoCloseOutline /></span>
                                            </button>
                                        }
                                    </div>
                                    <button id="klub-delete" className="klub-button klub-delete" onClick={handleClickDelete} disabled={waiting}>Delete</button>
                                </div>}
                        </div>
                    </div>
                <div className="klub-karta-gumbi">
                    {state.isConfirmed &&
                        <div className="side-buttons">
                            <button className="klub-button" onClick={handleAllEvents} disabled={waiting}>See all events</button >
                            <button className="klub-button" onClick={handleAllCourses} disabled={waiting}>See all courses</button>
                            <button className="klub-button" onClick={handleClickAllTrainers} disabled={waiting}>See all trainers</button>
                        </div>
                    }
                        <div className="karta-div">
                            <MapContainer center={[45.801245, 15.970876]} zoom={13} doubleClickZoom={false} scrollWheelZoom={true}
                                whenReady={async (map) => {
                                    var latLng = await fetchClub();
                                    latLng = latLng.adresa.split(" ");
                                    const lat = parseFloat(latLng[0]);
                                    const lng = parseFloat(latLng[1]);
                                    map.target.setView([lat,lng], 13);
                                    L.marker([lat, lng], {icon: violetIcon}).addTo(map.target).bindPopup("Club is located here")
                                    .on('mouseover', function (e) {
                                        this.openPopup();
                                    });
                                    var allPlesnjaci = await fetchEventiOdKluba();

                                    allPlesnjaci.forEach((plesnjak) => {
                                        var latLng = plesnjak.dvorana.adresa.split(" ");
                                        const lat = parseFloat(latLng[0]);
                                        const lng = parseFloat(latLng[1]);
                                        L.marker([lat, lng], {icon: blueIcon}).addTo(map.target).bindPopup('<a href="/club/event/' + plesnjak.plesnjakId + '">' + plesnjak.naziv + '</a>')
                                        .on('mouseover', function (e) {
                                            this.openPopup();
                                        });
                                    });
                                }}>
                                <TileLayer
                                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                />
                            </MapContainer>
                        </div>
                    </div>
                </div>
            </div>
    );
}
export default KlubPage;
