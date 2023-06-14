import "./klubCourseNew.css"
import { useEffect, useState, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useCookies } from 'react-cookie';
import { getAllPlesovi } from "../../utils/axios/backendCalls/plesEndpoints";
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import { addTecaj } from "../../utils/axios/backendCalls/tecajEndpoints";
import { validateNewCourse } from "../../utils/validation/courseValidation";
import activeHeader from "../../utils/activeHeader";
import { getPotvrdeniTreneriOdKluba } from "../../utils/axios/backendCalls/trenerEndpoints";
import { IoAddCircleSharp, IoCloseCircleSharp } from "react-icons/io5";

const KlubCourseNew = () => {
    const navigate = useNavigate();

    const [cookies, setCookie] = useCookies(['user']);

    const user = cookies.user;
    
    const [state, setState] = useState({
        courseDescription: "",
        deadline: "",
        capacity: "",
        plesId: "",
        courseLimitation: "",
        trenerId: "",
    });

    const [termini, setTermini] = useState([0]);

    const [dances, setDances] = useState([]);
    const [trainers, setTrainers] = useState([]);
    
    const [waiting, setWaiting] = useState(false);

    const multiselectRefDances = useRef();

    const location = useLocation();
    let vlasnik = "";
    let imeKluba = "";
    let klubId = "";
    let adresa = "";

    if (location.state) {
        if (location.state.vlasnik) {
            vlasnik = location.state.vlasnik;
        }
        if (location.state.imeKluba) {
            imeKluba = location.state.imeKluba;
        }
        if (location.state.klubId) {
            klubId = location.state.klubId;
        }
        if (location.state.adresa) {
            adresa = location.state.adresa;
        }
    }
    
    const [error, setError] = useState("");
    
    function redirectIfNotLoggedIn() {
        if (!user) {
            navigate("/login", { state: { from: "klub/register" } });
        }
    }

    useEffect(() => {
        activeHeader(document, "MY CLUBS");
        redirectIfNotLoggedIn();
        fetchAllDances();
        fetchAllTrainers();
    }, []);


    function handleChange(event){
        const {name, value} = event.target;
        setState({
            ...state,
            [name]: value
        });
    }

    async function handleClick(event) {

        setState({
            ...state,
            klubId: klubId
        });
        let validation = validateNewCourse(state);
        
        if (!validation.success) {
            setError(validation.errorMsg);
        }
        else {
            setError("");
            setWaiting(true);
            try {
                const response = await addTecaj({
                    ...state,
                    klubId: klubId
                }, termini.filter(termin => termin !== 0), adresa);

                if (response.success) {
                    setWaiting(false);
                    navigate("/club/course/" + response.tecaj.tecajId); //idemo na novu stranicu
                } else {
                    setWaiting(false);
                    setError(response.message);
                }
            }
            catch {
                setWaiting(false);
                setError("Error adding course");
            }
        } 
        return;
    }

    async function fetchAllDances() {
        try {
            const response = await getAllPlesovi();
            let listaPlesova = [];
            response.listaPlesova.forEach(ples => {
                listaPlesova.push({ value: ples.tipPlesaId, label: ples.naziv });
            });
            setDances(listaPlesova);
        }
        catch {
            console.log("Error fetching dances");
        }
    }

    async function fetchAllTrainers() {
        try {
            const response = await getPotvrdeniTreneriOdKluba(klubId);
            let listaTrenera = [];
            response.listaKlijenata.forEach(klijent => {
                listaTrenera.push({ value: klijent.klijentId, label: klijent.ime + " " + klijent.prezime + " (@" + klijent.korisnicko_ime  + ")"});
            });
            setTrainers(listaTrenera);
        }
        catch {
            console.log("Error fetching dances");
        }
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

    return location.state ? ((cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") ? (
        <div className="page">

            <div className="club-page">
                <h2>Add new course</h2>
                <form>
                <div className="club-box">
                    <textarea type="text" name="courseDescription" value={state.courseDescription} onChange={handleChange} required>
                    </textarea>
                    <label>Course description</label>
                </div>
                <div className="club-box no-animation">
                    <input type="datetime-local" name="timestamp" step="7" value={state.deadline} onChange={event => setState({
                                                                                                                ...state,
                                                                                                                deadline: event.target.value.replace("T", " ")
                                                                                                            })} required/>
                    <label>Application deadline</label>
                </div>

                <div className="club-box">
                        <input type="text" name="capacity" value={state.capacity} onChange={event => setState({
                                                                                                                ...state,
                                                                                                                capacity: event.target.value.replace(/\D/, '')
                                                                                                            })} required/>
                        <label>Capacity</label>
                    </div>
                    
                <div className="club-box">
                    <textarea type="text" name="courseLimitation" value={state.courseLimitation} onChange={handleChange} required>
                    </textarea>
                    <label>Course limitations</label>
                </div>

                    <div className="club-box">
                        <Dropdown options={dances} onChange={(e) => { setState({ ...state, plesId: e.value }) }} placeholder="Select a dance type" />
                    </div>

                    <div className="club-box">
                        <Dropdown options={trainers} onChange={(e) => { setState({ ...state, trenerId: e.value }) }} placeholder="Select a trainer" />
                    </div>

                    <div className="club-box">
                        <div className="addAppointment">
                            <h1 className="left-aligned">Appointments:</h1>
                            <span className="addTerminBtn" onClick = { (e) => dodajPrazanTermin() }><IoAddCircleSharp /></span>
                        </div>
                        {termini.map((termin, index) => (
                                <div className="redak">
                                    <input className="termin" type="datetime-local" name="timestamp" step="7" value={termini[index]} onChange={(e) => dopuniTermine(index, e.target.value)} />
                                {index !== 0 && <span className="deleteTerminBtn" onClick={(e) => obrisiTermin(index)}><IoCloseCircleSharp /></span>}
                                </div>
                        )
                        )}
                    </div>
                    

                    <input className="buttonR" type="button" value="Add Course" onClick={handleClick} disabled={waiting} />
                {error && <h1 className="errorMsg">{error}</h1>}
                </form>    
            </div>
        </div>
    ) : <div className="error-naslov"> <h1 className="errorMsg">You are not the owner of this club</h1> </div>) : <h1>Playing with links</h1>
};

export default KlubCourseNew;