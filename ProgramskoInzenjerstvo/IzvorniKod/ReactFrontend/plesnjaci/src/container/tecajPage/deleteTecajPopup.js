import { useState } from "react";
import "./deleteTecajPopup.css";
import { useNavigate } from "react-router-dom";
import { deleteTecaj } from "../../utils/axios/backendCalls/tecajEndpoints";


export default function DeleteTecajPopup(props) {


    const navigate = useNavigate();

    const [error, setError] = useState("");

    const [confirmation, setConfirmation] = useState("");

    const [waiting, setWaiting] = useState(false);

    async function handleClick() {
        setError("");
        try {
            setWaiting(true);
            const res = await deleteTecaj(props.tecajId);
            if (res.success) {
                setWaiting(false);
                navigate(-1);
            }
            else {
                setWaiting(false);
                setError(res.message);
            }
        } catch (e) {
            setWaiting(false);
            setError("Something went wrong. Please try again later.");
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
                <h2>Are you sure you want to delete this course?</h2>
                <h3>Type "DELETE COURSE" to delete this course.</h3>
                <div className="delete-confirmationInput">
                    
                    <input className="delete-confirmationText" type="text" maxLength={20} onChange={handleChange} value={confirmation} />
                    <br />
                </div>
                
                <button className="delete-btn" onClick={handleClick} disabled={waiting || confirmation !== "DELETE COURSE"}>Delete</button>

                <button className="delete-close-btn" onClick={closePopup} disabled={waiting}>
                            X
                </button>
                {props.children}
            </div>
        </div>
    ) : <></>;
}
