import { useState } from "react";
import "./deleteUserPopup.css";
import { useNavigate } from "react-router-dom";
import { deleteUser } from "../../utils/axios/backendCalls/userEndpoints";
import { useCookies } from "react-cookie";

export default function DeleteUserPopup(props) {


    const navigate = useNavigate();

    const [error, setError] = useState("");

    const [confirmation, setConfirmation] = useState("");

    const [cookies, setCookie, removeCookie] = useCookies(['user']);

    const [waiting, setWaiting] = useState(false);
    
    async function handleClick() {
        setError("");
        try {
            setWaiting(true);
            const res = await deleteUser(props.username);
            if (res.success) {
                if (cookies.user.username === props.username) {
                    removeCookie('user');
                }
                setWaiting(false);
                navigate("/");
            }
            else {
                setError(res.message);
                setWaiting(false);
            }
        } catch (e) {
            setError("Something went wrong. Please try again later.");
            setWaiting(false);
        }

    }

    function handleChange(e) {
        setConfirmation(e.target.value);
    }

    function closePopup() {
        setConfirmation("");
        setError("");
        props.setTrigger(false);
    }

    return (props.trigger) ? (
        <div className="delete-popup">
            <div className="delete-popup-inner">
                {error && <h1 className="errorMsg">{error}</h1>}
                <h2>Are you sure you want to delete {"@" + props.username}?</h2>
                <h3>Type "DELETE USER" to delete {"@" + props.username}.</h3>
                <div className="delete-confirmationInput">
                    
                    <input className="delete-confirmationText" type="text" maxLength={20} onChange={handleChange} value={confirmation} />
                    <br />
                </div>
                
                <button className="delete-btn" onClick={handleClick} disabled={waiting || confirmation !== "DELETE USER"}>Delete</button>

                <button className="delete-close-btn" onClick={closePopup} disabled={waiting}>
                            X
                </button>
                {props.children}
            </div>
        </div>
    ) : <></>;
}
