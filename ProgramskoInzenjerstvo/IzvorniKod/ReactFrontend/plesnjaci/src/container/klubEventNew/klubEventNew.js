import "./klubEventNew.css"
import { useEffect, useState, useRef } from "react";
import { addPlesnjak } from "../../utils/axios/backendCalls/plesnjakEndpoints";
import { useNavigate, useLocation } from "react-router-dom";
import { useCookies } from 'react-cookie';
import { MapContainer, TileLayer} from 'react-leaflet'
import L from "leaflet";
import { getAllPotvrdeniKlubovi } from "../../utils/axios/backendCalls/clubEndpoints";
import { blueIcon,violetIcon } from "../../utils/markers";
import { validateNewEvent } from "../../utils/validation/eventValidation";
import { Multiselect } from "multiselect-react-dropdown";
import { getAllPlesovi } from "../../utils/axios/backendCalls/plesEndpoints";
import { hostImage } from "../../utils/axios/imageHost";
import { getAllPlesnjaci } from "../../utils/axios/backendCalls/plesnjakEndpoints";
import activeHeader from "../../utils/activeHeader";
import { handleFileChange } from "../../utils/resizeFile";


const KlubEventNew = () => {
    const navigate = useNavigate();

    const [cookies, setCookie] = useCookies(['user']);

    const user = cookies.user;
    
    const [state, setState] = useState({
        description: "", location: "", slika : "", eventName:"", dances: [], startingTime: ""
    });

    const [waiting , setWaiting] = useState(false);

    const multiselectRefDances = useRef();

    var theMarker = null;

    const [latlng, setLatLng] = useState({
        lat: null,
        lng: null
    });

    const location = useLocation();
    let vlasnik = "";
    let imeKluba = "";

    if (location.state) {
        if (location.state.vlasnik) {
            vlasnik = location.state.vlasnik;
        }
        if (location.state.imeKluba) {
            imeKluba = location.state.imeKluba.replaceAll("%20", " ");
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
    }, []);


    function handleChange(event){
        if (event.target.name === "eventName") {
            setState({
                ...state,
                eventName: event.target.value
            });
        };

        if (event.target.name === "description") {
            setState({
                ...state,
                description: event.target.value
            });
        };
        if (event.target.name === "timestamp") {
            setState({
                ...state,
                startingTime: event.target.value.replace("T", " ")
            });
        }
    }

    async function handleClick(event) {

        let validation = validateNewEvent(state);
        if(latlng.lat===null || latlng.lng===null){
            setError("Please select location on the map");
            return;
        }
        if (!validation.success) {
            setError(validation.errorMsg);
        }
        else {
            setError("");
            setWaiting(true);
            try {
                let imageLink = "";
                const response1 = await hostImage(state.slika.split(",")[1]);
                if (response1["status_code"] === 200) {
                    imageLink = response1.image.display_url;
                }

                const response = await addPlesnjak({
                    ...state,
                    location: latlng.lat + " " + latlng.lng,
                    photo: imageLink,
                    dances: multiselectRefDances.current.getSelectedItems().map(item => item.naziv)
                }, vlasnik, imeKluba);

                if (response.success) {
                    setWaiting(false);
                    navigate("/club/event/"+ response.plesnjak.plesnjakId); //idemo na novi page
                } else {
                    setWaiting(false);
                    setError(response.message);
                    return;
                }
            } catch (error) {
                setWaiting(false);
                setError("Something went wrong");
                return;
            }
        } 
        return;
    }

    async function fetchAllDances() {
        try {
            const response = await getAllPlesovi();
            setState({
                ...state,
                dances: response.listaPlesova
            });
        }
        catch {
            console.log("Error fetching dances");
        }
    }


    return location.state ? (
        <div className="page">

            <div className="club-page">
                <h2>Add new event</h2>
                <form>
                <div className="club-box">
                    <input type="text" name="eventName" value={state.clubName} onChange={handleChange} required>
                    </input>
                    <label>Event Name</label>
                </div>
                <div className="club-box no-animation">
                    <input type="datetime-local" name="timestamp" step="7" value={state.startingTime} onChange={handleChange} required>
                    </input>
                    <label>Starting time</label>
                </div>
                <div className="bigWrapper">
                    <span>
                        <p className="multiselect-label">Dances:</p>
                    </span>
                    <div className="multiSelectWrapper">
                        <Multiselect
                            id="dances"
                            className="multiselect-form"
                            showArrow
                            options={state.dances}
                            displayValue="naziv"
                            ref={multiselectRefDances}
                        />
                    </div>
                </div>

                <div className="club-box margin-top">
                <input type="file" accept="image/*" onChange={  (e) => handleFileChange(e, state, setState) } multiple={false} />
                    <label>Photo</label>
                </div>
                <div className="club-box">
                    <textarea name="description" value={state.description} onChange={handleChange} />
                    <label>Description</label>
                </div>
                    <input className="buttonR" type="button" value="Add Event" onClick={handleClick} disabled={waiting} />
                {error && <h1 className="errorMsg">{error}</h1>}
                </form>    
            </div>
            <div className="map-div">
                    <MapContainer center={[45.801245, 15.970876]} zoom={13} doubleClickZoom={false} scrollWheelZoom={true}
                    whenReady={async (map) => {
                        var clubMarker = null;
                        try{
                            const dict=new Object();
                            const allKlubovi= await getAllPotvrdeniKlubovi();
                            allKlubovi.listaKlubova.map(item => {
                                const dvorana=item.dvorana;
                                if(dvorana && dvorana.adresa!=="null null"){
                                    
                                    if(dict[dvorana.adresa]){
                                        dict[dvorana.adresa]+="<br>"+'<a href="/club/' + item.vlasnik.korisnicko_ime + '/' + item.imeKluba + '">' + item.imeKluba + '</a>';
                                        
                                    }else{
                                        dict[dvorana.adresa] ="clubs:"+"<br>"+ '<a href="/club/' + item.vlasnik.korisnicko_ime + '/' + item.imeKluba + '">' + item.imeKluba + '</a>';
                                    }
                                }
                            });
                            const dict2=new Object();
                            const allEvents=await getAllPlesnjaci();
                            allEvents.listaPlesnjaka.map(item => {
                                const dvorana=item.dvorana;
                                if(dvorana && dvorana.adresa!=="null null"){
                                    let vlasnik = item.klubOrganizator.vlasnik.korisnicko_ime;
                                    let klub = item.klubOrganizator.imeKluba.replace(" ", "%20");
                                    let redirect = "/club/event/" + item.plesnjakId;
                                    if(dict2[dvorana.adresa]){
                                        dict2[dvorana.adresa]+="<br>"+'<a href=' + redirect + '>' + item.naziv + '</a>';
                                        
                                    }else{
                                        dict2[dvorana.adresa] = '<a href=' + redirect + '>' + item.naziv + '</a>';
                                    }
                                    
                                }
                            });
                            for (var key in dict2) {
                                if(dict[key]){
                                    dict[key]+="<br>"+"events:"+"<br>"+dict2[key];
                                }else{
                                    dict[key]="events:"+"<br>"+dict2[key];
                                }
                            };
                            for (var key in dict) {
                                const location=key.split(" ");
                                const lat=parseFloat(location[0]);
                                const lng=parseFloat(location[1]);
                                L.marker([lat, lng]).addTo(map.target).bindPopup(dict[key]).on('click', function(e) {
                                    setLatLng({
                                        lat:lat,
                                        lng:lng 
                                    });
                                    if (theMarker) {
                                        map.target.removeLayer(theMarker);
                                    }
                                    if (clubMarker) {
                                        clubMarker.setIcon(blueIcon);
                                    }
                                    clubMarker=this;
                                    this.setIcon(violetIcon);
                                }).on('mouseover', function (e) {
                                    this.openPopup();
                                }).on('click',function (e) {
                                    map.target.setView([lat,lng], 13);
                                });
                            };
                        }catch(err){
                            console.log("Error while getting clubs");
                        }
                        map.target.on("click", function (e) {
                            const { lat, lng } = e.latlng;
                            if (theMarker) {
                                map.target.removeLayer(theMarker);
                            }
                            if (clubMarker) {
                                clubMarker.setIcon(blueIcon);
                                clubMarker=null;
                            }
                            setLatLng({
                                lat:lat,
                                lng:lng 
                            });
                            theMarker=L.marker([lat, lng], {icon: violetIcon}).addTo(map.target);
                        });
                    }}>
                        <TileLayer
                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />
                    </MapContainer>
            </div>
        </div>
    ) : <h1>Playing with links</h1>;
};


export default KlubEventNew;