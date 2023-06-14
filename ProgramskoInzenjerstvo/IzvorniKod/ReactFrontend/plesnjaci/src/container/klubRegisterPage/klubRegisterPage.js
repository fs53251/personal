import { useEffect, useState } from "react";
import { klubRegister } from "../../utils/axios/backendCalls/clubEndpoints";
import "./klubRegisterPage.css"
import { useNavigate, useLocation } from "react-router-dom";
import { validateKlubRegistration } from "../../utils/validation/klubRegisterValidation";
import { useCookies } from 'react-cookie';
import { MapContainer, TileLayer } from 'react-leaflet'
import L from "leaflet";
import { getAllPotvrdeniKlubovi } from "../../utils/axios/backendCalls/clubEndpoints";
import { blueIcon, violetIcon } from "../../utils/markers";
import activeHeader from "../../utils/activeHeader";

const KlubRegisterPage = () => {

    const navigate = useNavigate();

    const [cookies, setCookie] = useCookies(['user']);

    const [waiting , setWaiting] = useState(false);

    const user = cookies.user;

    const [state, setState] = useState({
        owner: user ? user["username"] : "", clubName: "",
        telephone: "", email: "", link: "", description: "",
        location: "", isConfirmed : ""
    });

    var theMarker = null;

    const [latlng, setLatLng] = useState({
        lat: null,
        lng: null
    });

    const location = useLocation();
    let from = "";
    if (location.state) {
        if (location.state.from) {
            from = location.state.from;
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
    }, []);


    function handleChange(event){
        if (event.target.name === "clubName") {
            setState({
                ...state,
                clubName: event.target.value
            });
        };

        if (event.target.name === "telephone") {
            setState({
                ...state,
                telephone: event.target.value
            });
        };

        if (event.target.name === "email") {
            setState({
                ...state,
                email: event.target.value
            });
        };

        if (event.target.name === "description") {
            setState({
                ...state,
                description: event.target.value
            });
        };
    }

    async function handleClick(event) {
        
        let validation = validateKlubRegistration(state);
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
            try{
                const response = await klubRegister({
                    ...state,
                    location: latlng.lat + " " + latlng.lng
                });

                if (response.success) {
                    setWaiting(false);
                    navigate("/club/"+ response.klub.vlasnik.korisnicko_ime + "/" + response.klub.imeKluba); //na novi page
                } else {
                    setWaiting(false);
                    setError(response.message);
                    return;
                }
            } catch (error) {
                setWaiting(false);
                setError(error.message);
                return;
            }
        } 
        return;
    }
    return (
        <div className="page">

            <div className="club-page">
                <h2>Register Your Club Here</h2>
                <form>
                <div className="club-box">
                    <input type="text" name="clubName" value={state.clubName} onChange={handleChange} required>
                    </input>
                    <label>Club Name</label>
                </div>
                <div className="club-box">
                    <input type="tel" name="telephone" value={state.telephone} onChange={handleChange} required>
                    </input>
                    <label>Phone Number</label>
                </div>
                <div className="club-box">
                    <input type="email" name="email" value={state.email} onChange={handleChange} required>
                    </input>
                    <label>Email</label>
                </div>
                <div className="club-box">
                    <textarea name="description" value={state.description} onChange={handleChange} />
                    <label>Description</label>
                </div>
                    <input className="buttonR" type="button" value="Register Club" onClick={handleClick} disabled={waiting} />
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
                                        dict[dvorana.adresa] = '<a href="/club/' + item.vlasnik.korisnicko_ime + '/' + item.imeKluba + '">' + item.imeKluba + '</a>';
                                    }
                                }
                            });
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
                                })//.on('mouseout', function (e) {
                                    //this.closePopup();
                                //});
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
    );
};
export default KlubRegisterPage;