import "./profilePage.css"
import { useNavigate } from "react-router-dom";
import { useCookies } from 'react-cookie'
import { useEffect, useState } from "react";
import defaultPic from "../../utils/a.png";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import {IoPencilOutline, IoCloseOutline} from "react-icons/io5";
import { validateRegistration } from "../../utils/validation/registerValidation";
import { editProfile, getUser } from "../../utils/axios/backendCalls/userEndpoints";
import { hostImage } from "../../utils/axios/imageHost";
import ChangePasswordPopup from "../changePasswordPopup/changePasswordPopup";
import DeleteUserPopup from "../deleteUser/deleteUserPopup";
import activeHeader from "../../utils/activeHeader";
import { handleFileChange } from "../../utils/resizeFile";

const ProfilePage = () => {

    const [cookies, setCookie] = useCookies(['user']);

    const [state, setState] = useState({});

    const [stateBackup, setStateBackup] = useState();

    const [error, setError] = useState("");

    const [passwordPopup, setPasswordPopup] = useState(false);

    const [popupDelete, setPopupDelete] = useState(false);

    const [showEditPhoto, setShowEditPhoto] = useState(false);

    const [waiting , setWaiting] = useState(false);


    const navigate = useNavigate();

    function redirectIfNotLoggedIn() {
        if (!cookies.user) {
            navigate("/login", { state: { from: "profile" } });
        }
    }
    
    async function handleClickDelete() {
        setPopupDelete(true);
    }

    async function checkLink() {
        var username = window.location.pathname.split("profile/");
        if (username.length === 2) {
            username = username[1];
            if (username === cookies.user.username) {
                setState({
                    ...cookies.user,
                    slika: cookies.user ? cookies.user.photo : "",
                });
            }
            else {
                const response = await getUser(username);
                if (response.success) {
                    setState({
                        id: response.user.klijentId,
                        username: response.user.korisnicko_ime,
                        firstName: response.user.ime,
                        lastName: response.user.prezime,
                        email: response.user.email,
                        DOB: response.user.datumRodenja,
                        gender: response.user.spol,
                        telephone: response.user.brojMobitela,
                        experience: response.user.plesnoIskustvo,
                        slika: response.user.fotografija,
                        type: response.user.tipKorisnika
                    });
                }
                else {
                    navigate("/*");
                }
            }
        } else {
            setState({
                ...cookies.user,
                slika: cookies.user ? cookies.user.photo : "",
            });
        }
    }

    useEffect(() => {
        activeHeader(document, "PROFILE");
        redirectIfNotLoggedIn();
        refreshCookie(cookies, setCookie);
        checkLink();
    }, []);

    const setUserCookie = (user) => {
        let d = new Date();
        d.setTime(d.getTime() + (60*60*1000)); //60 min

        setCookie("user", user, { path: "/", expires: d });
    };

    async function handleClick(event) {
        setError("");
        
        var editbutton = document.getElementById('editbutton');

        if (editbutton.innerHTML === "Edit profile" ) {
            switchToEdit();
        } else {

            let validation = validateRegistration(state,"edit");
            if (!validation.success) {
                setError(validation.errorMsg);
            } else {
                setWaiting(true);
                var imageLink = "";
                try {
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
                                photo: imageLink
                            })
                        }
                    }
                    const response = await editProfile({
                        ...state,
                        photo: imageLink,
                    });

                    if (response.success) {
                        if (response.user.korisnicko_ime === cookies.user.username) {
                            setUserCookie({
                                id: response.user.klijentId,
                                username: response.user.korisnicko_ime,
                                firstName: response.user.ime,
                                lastName: response.user.prezime,
                                email: response.user.email,
                                DOB: response.user.datumRodenja,
                                gender: response.user.spol,
                                telephone: response.user.brojMobitela,
                                experience: response.user.plesnoIskustvo,
                                photo: response.user.fotografija,
                                type: response.user.tipKorisnika
                            });
                        }
                        else {
                            navigate("/profile/" + response.user.korisnicko_ime, { state: { from: "profile" } });
                        }
                    } else {
                        setError(response.errorMsg);
                    }
                    setWaiting(false);
                    switchToSave();
                }
                catch (error) {
                    setError("Something went wrong, please try again later");
                    setWaiting(false);
                    switchToSave();
                }
            }
        }
    }

    function switchToEdit() {
        setStateBackup(state);

        var editbutton = document.getElementById('editbutton');
        var inputs = document.querySelectorAll('.input');
        var editphoto=document.getElementsByClassName('editphoto');
        var iskustvo=document.getElementsByClassName('profile-experience')[0];
        setShowEditPhoto(!showEditPhoto);
            for (var i=0; i<inputs.length; i++) {
                inputs[i].readOnly=false
            }
            editbutton.innerHTML = "Save";
            editphoto.hidden=false;
            iskustvo.readOnly=false;
    }

    function switchToSave() {
        var editbutton = document.getElementById('editbutton');
        var inputs = document.querySelectorAll('.input');
        var editphoto=document.getElementsByClassName('editphoto');
        var iskustvo=document.getElementsByClassName('profile-experience')[0];
        setShowEditPhoto(!showEditPhoto);
        for (var i=0; i<inputs.length; i++) {
            inputs[i].readOnly=true
        }
        editbutton.innerHTML = "Edit profile";
        editphoto.hidden=true;
        iskustvo.readOnly=true;
    }
    async function handleClick2(event) {
        setState({
            ...state,
            slika: ""
        });
    }

    async function handleClose(event) {
        setState({
            ...stateBackup,
        });
        switchToSave();
        setError("");
    }

    function handleChange(event){
        if (event.target.name === "gender") {
            setState({
                ...state,
                gender: event.target.value
            });
        };

        if (event.target.name === "DOB") {
            setState({
                ...state,
                DOB: event.target.value
            });
        };

        if (event.target.name === "phone") {
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
        if (event.target.name === "firstname") {
            setState({
                ...state,
                firstName: event.target.value
            });
        };
        if (event.target.name === "lastname") {
            setState({
                ...state,
                lastName: event.target.value
            });
        };
        if (event.target.name === "experience") {
            setState({
                ...state,
                experience: event.target.value
            });
        };
    }

    return state.id && (
        <div className="profile-page-center">
            {cookies.user &&
            <div className="wrapper">
            {error && <h1 className="errorMsg">{error}</h1>}
                    {(cookies.user.username === state.username || cookies.user.type === "Administrator") &&
                    <div className="edit">
                        <div className="edit-save">
                            <button id="editbutton" className="btn btn-primary editbutton" onClick={handleClick} disabled={waiting}>Edit profile</button>
                            {showEditPhoto &&
                                <button id="closeedit" className="btn btn-primary closeedit" onClick={handleClose} disabled={waiting}>
                                    <span className="closeicon"><IoCloseOutline/></span>
                                </button>
                            }
                        </div>
                        <div className="deleteUserWrap">
                            <button className="deleteUser" onClick={handleClickDelete} disabled={waiting}>Delete user</button>
                        </div>
                    </div>
                    }
                
                
                <div className="left">
                    <div className="imagewrap">
                        <img src={state.slika? state.slika : defaultPic} alt="user"/>
                        {(showEditPhoto && state.slika)?<button className="btn btn-primary deletephoto" onClick={handleClick2} disabled={waiting}>
                            <span className="closeicon"><IoCloseOutline/></span>
                        </button>:null}
                        {showEditPhoto?<label className="file-upload">
                            <input
                                className="btn btn-primary editphoto"
                                type="file"
                                accept="image/*"
                                onChange={ (e) => handleFileChange(e, state, setState) }
                                multiple={false} />
                            <i className="btn btn-primary editphoto">
                                <span className="pencilicon"><IoPencilOutline/></span>
                            </i>
                        </label>:null}
                    </div>
                    <div className="leftleft">
                            <input name="firstname" className="input" type="text" maxLength={20} readOnly onChange={handleChange} value={state.firstName} />
                            <input name="lastname" className="input" type="text" maxLength={20} readOnly onChange={handleChange} value={state.lastName} />
                            <input className="username" type="text" readOnly value={"@" + state.username} />
                            {cookies.user.username === state.username && <div className="changePass">
                                <button className="changePassword" onClick={setPasswordPopup} disabled={waiting}>Change password</button>
                            </div>}
                    </div>
                </div>
                    <ChangePasswordPopup trigger={passwordPopup} setTrigger={setPasswordPopup} username={state.username} />
                    <DeleteUserPopup
                        trigger={popupDelete}
                        setTrigger={setPopupDelete}
                        username={state.username}
                    />

                <div className="right">
                    <h3>Profile</h3>
                    <div className="profile-firstRow">
                        <div className="data">
                            <h4>Email</h4>
                            <input name="email" className="input" type="text" readOnly onChange={handleChange} value={state.email} />
                        </div>
                        <div className="data">
                            <h4>Phone</h4>
                            <input name="phone" className="input" type="text" maxLength={20} readOnly onChange={handleChange} value={state.telephone} />
                        </div>
                    </div>
                    <div className="profile-secondRow">
                        <div className="data">
                            <h4>Gender</h4>
                            {!showEditPhoto?<input name="gender" className="input" type="text" readOnly onChange={handleChange} value={state.gender==="M"?"Male":"Female"} />
                            :<span className="radioHolderGender">
                                <input className="radioInput" type="radio" name="gender" value="M" checked={state.gender === "M"} onChange={handleChange} />M
                                <input className="radioInput" type="radio" name="gender" value="F" checked={state.gender === "F"} onChange={handleChange}/>F<br/>
                            </span>
                            }
                        </div>
                        <div className="data">
                            <h4>Date of birth</h4>
                            <input name="DOB" className="input" type="date" readOnly onChange={handleChange} value={state.DOB} />
                        </div>
                    </div>
                        <div className="profile-thirdRow">
                            <h4>Dancing experience</h4>
                            <textarea className="profile-experience" name="experience" rows="4" cols="50" 
                                placeholder="No experience" value={state.experience} onChange={handleChange} readOnly/>
                        </div>
                </div>
            </div>
        }
        
    </div>
            
    );
};

export default ProfilePage;