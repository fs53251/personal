import "./infoPage.css"
import { useEffect } from "react";
import { useCookies } from 'react-cookie';
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";

const InfoPage = () => {

    const navigate = useNavigate();
    
    const [cookies, setCookie] = useCookies(['user'])

    useEffect(() => { 
        activeHeader(document, "ABOUT US");
        refreshCookie(cookies, setCookie);
    }, []);

    function handleClick1() {
        navigate("/");
    }
    function handleClick2() {
        navigate("/login");
    }

    return (
        <div className="frame">
            <div className="car">
                <img src="https://i.pinimg.com/750x/9e/08/f0/9e08f041b3687b7f893fc9981a51bfee.jpg"/>
                <div className="intro">
                    <h1>About Us</h1>
                    <p className="pclass">We help you find and organize dance courses.</p>
                </div>
            </div>
            <div className="car">
                <img src="https://i.pinimg.com/564x/19/15/7b/19157bb6bda7e6ce1ef6edb1688787f0.jpg"/>
                <div className="intro">
                    <h1>What We Offer</h1>
                    <p className="pclass">View the profiles of clubs and the types of dance they offer.</p>
                    <input type="button" className="gumbic" value="See Offer" onClick={handleClick1}/>
                </div>
            </div>
            <div className="car">
                <img src="https://i.pinimg.com/564x/41/48/b9/4148b98437ddd9c2421fa26002c8df3f.jpg"/>
                <div className="intro">
                    <h1>Become Our Member</h1>
                    <p className="pclass">Register for additional features.</p>
                    <input type="button" className="gumbic" value="Go Register" onClick={handleClick2}/>
                </div>
            </div>
        </div>
    );
};

export default InfoPage;