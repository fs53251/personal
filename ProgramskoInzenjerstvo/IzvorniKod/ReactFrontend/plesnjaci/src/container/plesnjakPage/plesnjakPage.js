import "./plesnjakPage.css"
import { deletePlesnjak, editPlesnjak, getPlesnjak, getPlesoveOdPlesnjaka } from "../../utils/axios/backendCalls/plesnjakEndpoints";
import { useEffect, useState } from "react";
import { MapContainer, TileLayer } from "react-leaflet";
import L from "leaflet";
import { violetIcon } from "../../utils/markers";
import { validateEvent } from "../../utils/validation/eventValidation";
import activeHeader from "../../utils/activeHeader";
import DeletePlesnjakPopup from "./deletePlesnjakPopup";
import { IoCloseOutline } from "react-icons/io5";
import { useCookies } from "react-cookie";

const PlesnjakPage = () => {

    const [plesnjak, setPlesnjak] = useState();
    const [plesnjakBackup, setPlesnjakBackup] = useState();
    const [error, setError] = useState("");
    const [inEditMode, setInEditMode] = useState(false);

    const [plesovi, setPlesovi] = useState([]);

    const [popupDelete, setPopupDelete] = useState(false);

    const idPlesnjaka = window.location.pathname.substring(window.location.pathname.indexOf("/event/") + 7);

    const [waiting, setWaiting] = useState(false);
    
    const [cookies] = useCookies(['user']);
    

    const GetPlesnjak = async () => {
        const plesnjak = await getPlesnjak(idPlesnjaka);
        if (plesnjak == null) {
            return;
        }
        setPlesnjak(plesnjak.plesnjak);
    }

    const GetPlesovi = async () => {
        const plesovi = await getPlesoveOdPlesnjaka(idPlesnjaka);
        try {
            setPlesovi(plesovi.plesovi);
        } catch (error) {
            setError(error);
        }
    }

    const updatePlesnjak = async () => {

        let validation = validateEvent(plesnjak);
        if (!validation.success) {
            setError(validation.errorMsg);
        }
        else {
            setError("");
            setWaiting(true);
            try{
                const response = await editPlesnjak({ ...plesnjak, vrijeme: plesnjak.vrijeme.replace("T" , " ") });
                if (response.success) {
                    setWaiting(false);
                    return "success"
                } else {
                    setWaiting(false);
                    setError(response.message);
                }
            } catch (error) {
                setWaiting(false);
                console.log(error);
                setError("Something went wrong. Please try again later.");
            }
        }
        }

    const clickEdit = async () => {

        if (inEditMode === false) {
            setPlesnjakBackup(plesnjak);
            setInEditMode(true);
        } else {
            const uspjeh = await updatePlesnjak();
            if (uspjeh !== "success") {
                return
            }
            setInEditMode(false);
        }
    }

    const clickDelete = async () => {
        setPopupDelete(true);
    }

    const handleClose = () => {
        setPlesnjak(plesnjakBackup);
        setInEditMode(false);
    }

    const handleChange = (e) => {
        setPlesnjak({
            ...plesnjak,
            [e.target.name]: e.target.value
        });
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
            GetPlesnjak();
            GetPlesovi();
            activeHeader(document, "HOME PAGE");
        }, []);
    
        return plesnjak && (
            <div className="plesnjak-okvir">
                <div className="error-naslov">
                    {error && <h1 className="errorMsg">{error}</h1>}
                </div>

                <DeletePlesnjakPopup
                        trigger={popupDelete}
                        setTrigger={setPopupDelete}
                        plesnjakId={plesnjak.plesnjakId}
                    />

                <div className="plesnjak-glavni">
                    <div className="plesnjak-kartica">
                        <div className="plesnjak-card">
                            <div className="plesnjak-img">
                                <img src={plesnjak.slika} alt="Event photo" />
                            </div>
                            <div className="plesnjak-content">
                                <div className="plesnjak-ime">
                                    <h2>{plesnjak.naziv}</h2>
                                </div>
                                <div className="plesnjak-info">
                                    <label>Event Name:</label>
                                    <input disabled={ !inEditMode } name="naziv" className="plesnjak-input" type="text" value={plesnjak.naziv} onChange={handleChange} /> <br /> <br />
                                </div>
                                <div className="plesnjak-info">
                                    <label>Event Organizer:</label>
                                    <a type="text" href={"/club/" + plesnjak.klubOrganizator.vlasnik.korisnicko_ime + "/" + plesnjak.klubOrganizator.imeKluba}>{plesnjak.klubOrganizator.imeKluba}</a> 
                                </div>
                                <div className="plesnjak-info">
                                    <label>Event Description:</label>
                                    <input disabled={ !inEditMode } name="opis" className="plesnjak-input" type="text" value={plesnjak.opis} onChange={handleChange}/> <br /> <br />
                                </div>
                                <div className="plesnjak-info">
                                    <label>Event Starting Time:</label>
                                    <input disabled={ !inEditMode } name="vrijeme" className="plesnjak-input" type="datetime-local" step="7" value={plesnjak.vrijeme} onChange={handleChange} /> <br /> <br />
                                </div>
                                <div className="plesnjak-info">
                                    <label>Teaches dances:</label>
                                    {plesovi.length > 0 ? <GenerateContent /> : <input disabled={true} value={"None"}/>}
                                </div>
                            </div>
                            {(cookies.user?.username === plesnjak.klubOrganizator.vlasnik.korisnicko_ime || cookies.user?.type === "Administrator") &&
                                <div className="plesnjak-buttons">
                                    <div className="editAndClose">
                                        <button id="plesnjak-edit" className="plesnjak-button" onClick={clickEdit} disabled={waiting}>{inEditMode ? "Save" : "Edit"}</button>
                                        {inEditMode &&
                                            <button id="closeedit" className="btn btn-primary closeedit plesnjak-button" onClick={handleClose} disabled={waiting}>
                                                <span className="closeicon"><IoCloseOutline /></span>
                                            </button>
                                        }
                                    </div>
                                    <button id="plesnjak-delete" className="plesnjak-button plesnjak-delete" onClick={clickDelete} disabled={waiting}>Delete</button>
                                </div>
                            }
                        </div>
                    </div>
                <div className="plesnjak-karta">
                        <div className="karta-div">
                            <MapContainer center={[45.801245, 15.970876]} zoom={13} doubleClickZoom={false} scrollWheelZoom={true}
                                whenReady={async (map) => {
                                    var latLng = plesnjak.dvorana.adresa.split(" ");
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
                    </div>
                </div>
            </div>
        )
}

export default PlesnjakPage;