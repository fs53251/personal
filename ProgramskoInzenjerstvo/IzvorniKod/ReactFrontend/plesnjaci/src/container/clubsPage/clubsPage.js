import "./clubsPage.css"
import { useEffect, useState } from "react";
import { useCookies } from 'react-cookie';
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { useNavigate } from "react-router-dom";
import { klubFilter } from "../../utils/axios/backendCalls/clubEndpoints";
import { FiChevronRight } from "react-icons/fi";
import { IoReaderOutline } from "react-icons/io5";
import activeHeader from "../../utils/activeHeader";
import { dohvatiKluboveOdTrenera } from "../../utils/axios/backendCalls/trenerEndpoints";


const ClubPage = () => {
    //dohvat klubova
    const navigate = useNavigate();
    const [state, setState] = useState({
        ownersClubs:[],renderedOutput:"",ownsClubs:false,
    });

    const [trenerKlubovi, setTrenerKlubovi] = useState([]);

    //ostalo
    const [cookies, setCookie] = useCookies(['user'])

    useEffect(() => { 
        redirectIfNotLoggedIn();
        activeHeader(document, "MY CLUBS");
        refreshCookie(cookies, setCookie);
    }, []);
    
    //registracija novog kluba
    function handleClick() {
        navigate("/club/register", { state: { from: "clubs" } });
    }

    async function dohvatiKlubove() {
        const username=cookies.user.username;
        const response = await klubFilter(username);
        if (response.success) {
            state.ownersClubs = response.listaKlubova;
            var l = state.ownersClubs.map(item =>
                <div className="card-klub">
                    <div className="face face1">
                        <div className="content">
                            <h3>The Name Of Your Club : {item.imeKluba}</h3>
                        </div>
                    </div>
                    <div className="face face2">
                        <div className="content">
                            <p>See Club Profile</p>
                            <a href={'/club/' + cookies.user.username + '/' + item.imeKluba} key={item.imeKluba}> See Profile </a>
                        </div>
                    </div>
                </div>);
            setState({ renderedOutput: l });
        }else {
            navigate("/*", { state: { from: "profile" } });
        }
    }

    async function dohvatiTrenere() {
        const username=cookies.user.username;
        const response = await dohvatiKluboveOdTrenera(username);
        if (response.success) {
            setTrenerKlubovi(response.listaKlubova);
        }else {
            navigate("/*", { state: { from: "profile" } });
        }

    }

    async function redirectIfNotLoggedIn() {
        
        if (!cookies.user) {
            navigate("/login", { state: { from: "clubs" } });
        }
        else {
            dohvatiKlubove();
            dohvatiTrenere();
            }
    }

    return (
        <div className="obrub">
            <div className="menu">
                {state.renderedOutput.length!==0 ? state.renderedOutput : 
                <div className="dodatak">
                    <div className="no-clubs">
                        <div className="arrow"></div>
                        <div className="arrow"></div>
                        <div className="arrow"></div>
                    </div>
                    <div className="tekst-noclub">
                        <p>You don't own any clubs. <br/> Register your new dance club right here.</p>
                    </div>
                </div>
                
                }
            </div>
            <div className="right-side">
                <div className="card-wrapper">
                    <div className="card-top">
                        <img className="image" alt="People dancing" src="http://www.lovelifepractice.com/wp-content/uploads/2015/07/AnaDiego.jpg"/>
                    </div>

                    <div className="card-bottom">
                        <h1>New Dance Club</h1>
                        <p> We are passionate about giving everybody the opportunity to dance at SBT and we fully support individuals to start new dance stories.</p>
                        <br/>
                        <input className="button2" type="button" value="Register Club" onClick={handleClick}/>
                    </div>
                </div>

                <div className="popis">
                    <div className="klub-naslov">
                        <h1>Clubs Where I Work as a Trainer</h1>
                    </div>
                    {trenerKlubovi.length === 0 ? <div className="nisam-trener"><label>You are not a dance trainer yet.</label> <img src="https://t3.ftcdn.net/jpg/02/72/37/84/360_F_272378472_xy1sXe5UqOWr0YpJDy7wLXFYOAG4fOHT.jpg"/></div> : <ul className="ulClass">
                        {trenerKlubovi.map((item, index) =>
                            <li className={index === 0 ? "prvi" : ""}>
                                <FiChevronRight />
                                <a className="lijepiA" href={"/club/" + item.vlasnik.korisnicko_ime + "/" + item.imeKluba}>{item.imeKluba}</a>
                            </li>
                        )}
                    </ul>
                    }
                </div>
            </div>

        </div>

    
    );
};

export default ClubPage;