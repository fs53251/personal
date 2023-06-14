import React from 'react';
import "./changePasswordPopup.css";
import { useState } from 'react';
import { changePassword } from '../../utils/axios/backendCalls/userEndpoints';
import { hash } from '../../utils/hasher';

export default function ChangePasswordPopup(props) {
    const [state, setState] = useState({
        oldPassword: "",
        newPassword: ""
    });

    const [waiting , setWaiting] = useState(false);

    const [error, setError] = useState("");

    function handleChange(e) {
        const { name, value } = e.target;
        setState(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    function closePopup() {
        setError("");
        setState({
            oldPassword: "",
            newPassword: ""
        })
        props.setTrigger(false);
    }

    async function handleClick() {
        if (state.oldPassword === "" || state.newPassword === "") {
            setError("Fill out all required fields.")
        }
        else if (state.oldPassword.length<5 || state.newPassword.length<5) {
            setError("Password must be atleast 5 characters long.")
        }
        else if(state.newPassword.length>20 || state.oldPassword.length>20){
            setError("Password can't be longer then 20 characters.")
        }
        else {
            setError("");
            setWaiting(true);
            try {
                const response = await changePassword(props.username, hash(state.oldPassword), hash(state.newPassword));
                if (response.success) {
                    setWaiting(false);
                    closePopup();
                } else {
                    setError(response.message);
                    setWaiting(false);
                }
            }
            catch {
                setError("Something went wrong. Please try again later.");
                setWaiting(false);
            }
        }
    }

    return (props.trigger) ? (
        <div className="pass-popup">
            <div className="pass-popup-inner">
                {error && <h1 className="errorMsg">{error}</h1>}
                <h3>Change password</h3>
                <div className="passwords">
                    <label className="pass-oldRow">
                        *Current password:
                        <input name="oldPassword" className="pass-input" type="password" autoComplete="old-password" maxLength={20} onChange={handleChange} value={state.oldPassword} />
                    </label>
                    
                    
                    <br />

                    <label className="pass-newRow">
                        *New password:
                        <input name="newPassword" className="pass-input" type="password" autoComplete="new-password" maxLength={20} onChange={handleChange} value={state.newPassword} />
                    </label>
                </div>
                <br />
                
                <button className="pass-change-btn" onClick={handleClick} disabled={waiting}>Change</button>

                <button id="closeedit" className="btn btn-primary pass-close-btn" onClick={closePopup} disabled={waiting}>
                            X
                        </button>
                {props.children}
            </div>
        </div>
    ) : <></>;
}
