import "./plesPage.css"
import { useNavigate } from "react-router-dom";
import { useCookies } from 'react-cookie'
import { useEffect, useState } from "react";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import {IoPencilOutline, IoCloseOutline} from "react-icons/io5";
import { editPles, getPles } from "../../utils/axios/backendCalls/plesEndpoints";
import { hostImage } from "../../utils/axios/imageHost";
import { validatePles } from "../../utils/validation/plesValidation";
import DeletePlesPopup from "../deleteDance/deleteDancePopup";
import defaultPic from "../../utils/a.png";
import React from "react";
import activeHeader from "../../utils/activeHeader";
import { handleFileChange } from "../../utils/resizeFile";


const PlesPage = () => {

    const [cookies, setCookie] = useCookies(['user']);

    const [state, setState] = useState({
        naziv: "", link: "", opis: "",
        slika: "", tipPlesaId: ""
    });

    const [stateBackup, setStateBackup] = useState();
    const [error, setError] = useState("");
    const [popupDelete, setPopupDelete] = useState(false);
    const [waiting, setWaiting] = useState(false);
    const [inEditMode, setInEditMode] = useState(false);
    const navigate = useNavigate();

    async function redirectIfNotLoggedIn() {
        const imePlesa =window.location.pathname.split("dance/")[1];
        if (!cookies.user) {
            navigate("/login", { state: { from: "dance/" + imePlesa } });
        }
        else {
            const response = await getPles(imePlesa);
            if (response.success) {
                const ples = response.ples;
                setState({
                    naziv: ples.naziv,
                    link: ples.link,
                    opis: ples.opis,
                    slika: ples.slika,
                    tipPlesaId: ples.tipPlesaId
                });
            }
            else {
                navigate("/*", { state: { from: "profile" } });
            }
        }
    }  
    
    async function handleClickDelete() {
        setPopupDelete(true);
    }

    useEffect(() => {
        activeHeader(document, "HOME PAGE");
        redirectIfNotLoggedIn();
        refreshCookie(cookies, setCookie);
    }, []);


    const clickEdit = async () => {

        if (inEditMode === false) {
            setStateBackup(state);
            setInEditMode(true);
        } else {
            setWaiting(true);
            try {
                const validation = validatePles(state);
                if (!validation.success) {
                    setError(validation.errorMsg);
                    setWaiting(false);
                }
                else {
                    setError("");

                    var imageLink = "";
                    if (state.slika) {
                        if (state.slika === stateBackup.slika) {
                            imageLink = stateBackup.slika;
                        }
                        else {
                            const response = await hostImage(state.slika.split(",")[1]);

                            if (response["status_code"] === 200) {
                                imageLink = response.image.display_url;
                            }
                            else {
                                imageLink = "";
                            }
                            setState({
                                ...state,
                                slika: imageLink
                            })
                        }
                    }
                    const response = await editPles({
                        ...state,
                        slika: imageLink
                    });
                    setWaiting(false);
                    setInEditMode(false);
                }
            } catch {
                setWaiting(false);
                setError("Something went wrong");
            }
        }
    }


    function removeImage(event) {
        setState({
            ...state,
            slika: ""
        });
    }

    const handleClose = () => {
        setState(stateBackup);
        setInEditMode(false);
    }

    function handleChange(event){

        if (event.target.name === "link") {
            setState({
                ...state,
                link: event.target.value
            });
        };

        if (event.target.name === "description") {
            setState({
                ...state,
                opis: event.target.value
            });
        };
    }

    const embedId = state.link.substring(state.link.lastIndexOf("=") + 1, state.link.length)
    
    return (
        <div className="ples-okvir">
            <div className="error-naslov">
                    {error && <h1 className="errorMsg">{error}</h1>}
            </div>
            <DeletePlesPopup
                trigger={popupDelete}
                setTrigger={setPopupDelete}
                naziv={state.naziv}
            />

            <div className="ples-glavni">
                <div className="ples-kartica">
                    <div className="ples-card">
                        <div className="ples-img">
                            <img src={state.slika ? state.slika : defaultPic} alt="Dance photo" />
                            {(inEditMode && state.slika) ?
                                <button className="btn btn-primary deletephoto" onClick={removeImage} disabled={waiting}>
                                    <span className="icon"><IoCloseOutline/></span>
                                </button> :
                                null}
                            {inEditMode ?
                                <label className="file-upload">
                                    <input
                                        className="btn btn-primary editphoto"
                                        type="file"
                                        accept="image/*"
                                        onChange={ (e) => handleFileChange(e, state, setState) }
                                        multiple={false} />
                                    <i className="btn btn-primary editphoto">
                                        <span className="icon"><IoPencilOutline/></span>
                                    </i>
                                </label>:null}
                        </div>
                        <div className="ples-content">
                            <div className="ples-ime">
                                <h2>{state.naziv}</h2>
                            </div>
                            {inEditMode && <div className="ples-info">
                                <label>Link:</label>
                                <input disabled={!inEditMode} name="link" className="ples-input" type="text" value={state.link} onChange={handleChange} /> <br /> <br />
                            </div>}
                            <div className="ples-info">
                                <label>Description:</label>
                                <textarea disabled={ !inEditMode } rows="3" name="description" className="ples-input" type="text" value={state.opis} onChange={handleChange} /> <br /> <br />
                            </div>
                        </div>
                        <div className="ples-buttons">
                            <div className="editAndClose">
                            <button id="ples-edit" className="ples-button" onClick={clickEdit} disabled={waiting}>{inEditMode ? "Save" : "Edit"}</button>
                            {inEditMode &&
                                    <button id="closeedit" className="btn btn-primary closeedit ples-button" onClick={handleClose} disabled={waiting}>
                                        <span className="closeicon"><IoCloseOutline/></span>
                                    </button>
                                }
                            </div>
                            <button id="ples-delete" className="ples-button ples-delete" onClick={handleClickDelete} disabled={waiting}>Delete</button>
                        </div>
                    </div>
                </div>
            <div className="ples-video-wrapper">
                    <div className="ples-video">
                        <iframe
                            width="500"
                            height="300"
                            src={embedId ? `https://www.youtube.com/embed/${embedId}` : ""}
                            frameBorder="0"
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                            allowFullScreen
                            title="Embedded youtube"
                        /> 
                    </div>
                </div>
            </div>
        </div>                     
    
    );
};

export default PlesPage;