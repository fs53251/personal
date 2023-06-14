import "./tecajPage.css"
import { useEffect, useState } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import L from "leaflet";
import { violetIcon } from "../../utils/markers";
import activeHeader from "../../utils/activeHeader";
import { dohvatiTermineTecaja, editTecaj, getTecaj, upisiTecaj } from "../../utils/axios/backendCalls/tecajEndpoints";
import { useNavigate } from "react-router-dom";
import { validateCourse } from "../../utils/validation/courseValidation";
import DeleteTecajPopup from "./deleteTecajPopup";
import { IoAddCircleSharp, IoCloseCircleSharp, IoCloseOutline } from "react-icons/io5";
import { getPotvrdeniTreneriOdKluba } from "../../utils/axios/backendCalls/trenerEndpoints";
import Dropdown from 'react-dropdown';
import { getAllPlesovi } from "../../utils/axios/backendCalls/plesEndpoints";
import { useCookies } from "react-cookie";

const TecajPage = () => {

    const [tecaj, setTecaj] = useState();
    const [tecajBackup, setTecajBackup] = useState();
    const [error, setError] = useState("");
    const [inEditMode, setInEditMode] = useState(false);
    const [trainers, setTrainers] = useState([])
    const [currentTrainer, setCurrentTrainer] = useState({});
    const [currentTrainerBackup, setCurrentTrainerBackup] = useState({});

    const [termini, setTermini] = useState([]);
    const [terminiBackup, setTerminiBackup] = useState([]);

    const [dances, setDances] = useState([])
    const [currentDance, setCurrentDance] = useState({});
    const [currentDanceBackup, setCurrentDanceBackup] = useState({});

    const [popupDelete, setPopupDelete] = useState(false);

    const [waiting, setWaiting] = useState(false);
    
    const [cookies, setCookie] = useCookies(['user'])
    const [applyValue, setApplyValue] = useState("Apply");
    const [applied, setApplied] = useState(false);

    const user = cookies.user;

    const navigate = useNavigate();

    const idTecaja = window.location.pathname.substring(window.location.pathname.indexOf("/course/") + 8);
    

    const GetTecaj = async () => {
        const tecaj = await getTecaj(idTecaja);
        if (tecaj == null) {
            return;
        }
        setTecaj(tecaj.tecaj);
        fetchAllTrainers(tecaj.tecaj.klub.klubId);
        fetchAllDances();
        fetchAllTermini(tecaj.tecaj.tecajId);

        setCurrentTrainer({value: tecaj.tecaj.trener.klijentId, label: tecaj.tecaj.trener.ime + " " + tecaj.tecaj.trener.prezime + " (@" + tecaj.tecaj.trener.korisnicko_ime  + ")"});
        setCurrentDance({value: tecaj.tecaj.tipPlesa.tipPlesaId, label: tecaj.tecaj.tipPlesa.naziv});
    }

    const updateTecaj = async () => {

        const updatedTecaj = {
            ...tecaj,
            rokPrijave: tecaj.rokPrijave.replace("T", " "),
            trener: {
                ...tecaj.trener,
                klijentId: currentTrainer.value
            },
            tipPlesa: {
                ...tecaj.tipPlesa,
                tipPlesaId: currentDance.value,
            }
        }
        let validation = validateCourse(updatedTecaj);
        if (!validation.success) {
            setError(validation.errorMsg);
        }
        else {
            setError("");
            setWaiting(true);
            try {

                const response = await editTecaj(updatedTecaj, termini.filter(termin => termin !== 0));
                if (response.success) {
                    setWaiting(false);
                    return "success"
                } else {
                    setWaiting(false);
                    setError(response.message);
                }
            }
            catch (error) {
                setWaiting(false);
                console.log(error);
                setError("Something went wrong. Please try again.");
            }
        }
    }

    const clickEdit = async () => {

        if (inEditMode === false) {
            setError("");
            setTecajBackup(tecaj);
            setCurrentTrainerBackup(currentTrainer);
            setCurrentDanceBackup(currentDance);
            setTerminiBackup(termini);
            setInEditMode(true);
        } else {
            const uspjeh = await updateTecaj();
            if (uspjeh !== "success") {
                return
            }
            setInEditMode(false);
            GetTecaj();
        }
    }

    const clickDelete = async () => {
        setPopupDelete(true);
    }

    const handleChange = (e) => {
        if (e.target.name === "kapacitetGrupe") {
            setTecaj({
                ...tecaj,
                [e.target.name]: e.target.value.replace(/\D/, '')
            });
        }
        else {
            setTecaj({
                ...tecaj,
                [e.target.name]: e.target.value
            });
        }
    }

    const handleClose = () => {
        setTecaj(tecajBackup);
        setCurrentTrainer(currentTrainerBackup);
        setCurrentDance(currentDanceBackup);
        setTermini(terminiBackup);
        setInEditMode(false);
        setError("");
    }


    function dopuniTermine(index, noviTermin) {

        let listaTermina = [...termini];
        const splitani = noviTermin.split("T");
        if (splitani[1].length === 5) {
            noviTermin += ":00";
        }

        if (listaTermina.length <= index) {
            listaTermina.push(noviTermin);
        }
        else {
            listaTermina[index] = noviTermin;
        }
        setTermini(listaTermina);
    }

    function obrisiTermin(index) {

        let listaTermina = [...termini];
        listaTermina.splice(index, 1);
        
        setTermini(listaTermina);
    }

    function dodajPrazanTermin(index) {

        let listaTermina = [...termini];
        listaTermina.push(0);
        
        setTermini(listaTermina);
    }

    const apply = () => {
        try {
            setWaiting(true);
            const response = upisiTecaj(user.id, tecaj.tecajId);
            if (response.success) {
                setWaiting(false);
                setApplied(true);
                setApplyValue("Applied");
            }
            else {
                setWaiting(false);
                setApplied(true);
                setApplyValue("Applied");
            }
        }
        catch (error) {
            setWaiting(false);
            setError(error);
        }
    }

    async function fetchAllTrainers(klubId) {
        try {
            const response = await getPotvrdeniTreneriOdKluba(klubId);
            let listaTrenera = [];
            response.listaKlijenata.forEach(klijent => {
                listaTrenera.push({ value: klijent.klijentId, label: klijent.ime + " " + klijent.prezime + " (@" + klijent.korisnicko_ime  + ")"});
            });
            setTrainers(listaTrenera);
        }
        catch {
            console.log("Error fetching trainers");
        }
    }

    async function fetchAllDances() {
        try {
            const response = await getAllPlesovi();
            let listaPlesova = [];
            response.listaPlesova.forEach(ples => {
                listaPlesova.push({ value: ples.tipPlesaId, label: ples.naziv});
            });
            setDances(listaPlesova);
        }
        catch {
            console.log("Error fetching trainers");
        }
    }

    async function fetchAllTermini(tecajId) {
        try {
            const response = await dohvatiTermineTecaja(tecajId);
            let listaTermina = [];
            response.listaTermina.forEach(termin => {
                listaTermina.push(termin.vrijeme);
            });
            listaTermina.sort();
            setTermini(listaTermina);
        }
        catch {
            console.log("Error fetching trainers");
        }
    }


        useEffect(() => {
            GetTecaj();
            activeHeader(document, "HOME PAGE");
        }, []);
    
        return tecaj && (
           <div className="tecaj-okvir">
                <div className="tecaj-page">
                    <div className="tecaj-naslovi">
                        <h2>Course Info</h2>
                        <h2>See Where The Course Takes Place</h2>
                    </div>
                    <div className="tecaj-div">
                        <div className="tecaj-info">
                            <form className="tecaj-forma">
                                <div className="user-box">
                                    <a type="text" href={"/club/" + tecaj.klub.vlasnik.korisnicko_ime + "/" + tecaj.klub.imeKluba}>{tecaj.klub.imeKluba}</a> 
                                    <label>Course Organizer:</label>
                                </div>

                                <div className="user-box">
                                    {!inEditMode ? <a type="text" href={"/dance/" + tecaj.tipPlesa.naziv}>{tecaj.tipPlesa.naziv}</a>
                                    : <div className="course-box-dropdown">
                                    <Dropdown options={dances} onChange={(e) => { setCurrentDance({ value: e.value, label: e.label }) }} placeholder="Select a trainer" value={currentDance}/>
                                    </div>} 
                                   <label>Course for:</label>
                                </div>

                                <div className="user-box">
                                    {!inEditMode ? <a type="text" href={"/profile/" + tecaj.trener.korisnicko_ime}>{tecaj.trener.ime + " " + tecaj.trener.prezime}</a>
                                    : <div className="course-box-dropdown">
                                    <Dropdown options={trainers} onChange={(e) => { setCurrentTrainer({ value: e.value, label: e.label }) }} placeholder="Select a trainer" value={currentTrainer}/>
                                    </div>
                                    }  
                                    <label>Trainer:</label>
                                </div>

                                <div className="user-box">
                                    <input disabled={ !inEditMode } name="rokPrijave" className="tecaj-input" type="datetime-local" value={tecaj.rokPrijave} onChange={handleChange} /> 
                                    <label>Application Deadline:</label>
                                </div>

                                <div className="user-box">
                                    <input disabled={ !inEditMode } name="opis" className="tecaj-input" type="text" value={tecaj.opis} onChange={handleChange} /> 
                                    <label>Course Description:</label>
                                </div>

                                <div className="user-box">
                                    <input disabled={ !inEditMode } name="ogranicenja" className="tecaj-input" type="text" value={tecaj.ogranicenja??""} onChange={handleChange} /> 
                                    <label>Course Limitations:</label>
                                </div>

                                <div className="user-box">
                                    <input disabled={ !inEditMode } name="kapacitetGrupe" className="tecaj-input" type="text" value={tecaj.kapacitetGrupe} onChange={handleChange} /> 
                                    <label>Capacity:</label>
                                </div>

                                <div className="user-box-appointments">
                                    {inEditMode && <div className="addTerminBtn" onClick={(e) => dodajPrazanTermin()}><IoAddCircleSharp /></div>}
                                    {termini.map((termin, index) => (
                                        <div className="redak">
                                        <input disabled={ !inEditMode } className="termin" type="datetime-local" name="timestamp" step="7" value={termini[index]} onChange={(e) => dopuniTermine(index, e.target.value)} />
                                    {inEditMode && <span className="deleteTerminBtn" onClick={(e) => obrisiTermin(index)}><IoCloseCircleSharp /></span>}
                                </div>))}
                                    <label>Appointments:</label>
                                </div>
                            </form>
                            {error && <h1 className="errorMsg">{error}</h1>}
                            <input className="apply-button" type="button" onClick={apply} value={applyValue}  disabled={waiting || applied}/>
                        </div>
                        <div className="tecaj-karta">
                            <div className="t-karta">
                                <MapContainer center={[45.801245, 15.970876]} zoom={13} doubleClickZoom={false} scrollWheelZoom={true}
                                                whenReady={async (map) => {
                                                    var latLng = tecaj.klub.dvorana.adresa.split(" ");
                                                    const lat = parseFloat(latLng[0]);
                                                    const lng = parseFloat(latLng[1]);
                                                    map.target.setView([lat,lng], 13);
                                                    L.marker([lat, lng], {icon: violetIcon}).addTo(map.target);
                                            }}>
                                                <TileLayer
                                                    attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                                                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                                />
                                </MapContainer>
                            </div>
                            <div className="tecaj-buttons">
                                {(cookies.user?.username === tecaj.klub.vlasnik.korisnicko_ime || cookies.user?.type === "Administrator" || cookies.user?.username === tecaj.trener.korisnicko_ime) &&
                                    <div className="tecaj-buttons">
                                        <button id="tecaj-edit" className="enroll-button" onClick={(e) => navigate("enrolled")} disabled={waiting}>See enrolled clients</button>
                                    </div>
                                }
                                {(cookies.user?.username === tecaj.klub.vlasnik.korisnicko_ime || cookies.user?.type === "Administrator") &&
                                    <div className="tecaj-buttons">
                                        <button id="tecaj-edit" className="edit-save-button" onClick={clickEdit} disabled={waiting}>{inEditMode ? "Save" :    "Edit"}</button>
                                            {inEditMode &&
                                                <button id="closeedit" className="tecaj-close-button" onClick={handleClose} disabled={waiting}>
                                                <span className="closeicon"><IoCloseOutline /></span>
                                                </button>
                                            }
                                        <button id="tecaj-delete" className="tecaj-delete-button" onClick={clickDelete} disabled={waiting}>Delete</button>
                                    </div>
                                }
                            </div>   
                        </div>
                    </div>
                </div>

                <DeleteTecajPopup
                trigger={popupDelete}
                setTrigger={setPopupDelete}
                tecajId={tecaj.tecajId}
                />
        </div>
        )
}

export default TecajPage;