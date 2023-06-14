//za dizajn
import * as Components from './Design';
import React from "react";
//za register i login
import { login, register } from "../../utils/axios/backendCalls/userEndpoints";
import { hash } from "../../utils/hasher";
import { validateLogin } from "../../utils/validation/loginValidation";
import { validateRegistration } from "../../utils/validation/registerValidation";
import { hostImage } from "../../utils/axios/imageHost";
//ostalo
import { useEffect, useState } from "react";
//import { Link } from "react-router-dom";
import { useCookies } from 'react-cookie'
import { useNavigate, useLocation } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";
import { handleFileChange } from '../../utils/resizeFile';



function LoginPage (props) {
    //REGISTRACIJA
    //za dizajn
    const [signIn, toggle] = React.useState(true);

    //redirect
    const navigate = useNavigate();
    const location = useLocation();
    let from = "";
    if (location.state) {
        if (location.state.from) {
            from = location.state.from;
        }
    }

    //stateRegister
    const [stateRegister, setState] = useState({
        username: "", password: "", firstName: "",
        lastName: "", gender: "M", DOB: "", telephone: "",
        email: "", experience: "", slika: ""
    });

    const [waiting, setWaiting] = useState(false);

    //cookie
    const [cookies, setCookie] = useCookies(['user']);
    const setUserCookie = (user) => {
        let d = new Date();
        d.setTime(d.getTime() + (60*60*1000)); //60 min

        setCookie("user", user, {path: "/", expires: d});
    };
    //error
    const [error, setError] = useState("");

    //handleChange spremanje
    function handleChange(event){
        if (event.target.name === "username") {
            setState({
                ...stateRegister,
                username: event.target.value
            });
        };

        if (event.target.name === "password") {
            setState({
                ...stateRegister,
                password: event.target.value
            });
        };

        if (event.target.name === "firstName") {
            setState({
                ...stateRegister,
                firstName: event.target.value
            });
        };

        if (event.target.name === "lastName") {
            setState({
                ...stateRegister,
                lastName: event.target.value
            });
        };

        if (event.target.name === "gender") {
            setState({
                ...stateRegister,
                gender: event.target.value
            });
        };

        if (event.target.name === "DOB") {
            setState({
                ...stateRegister,
                DOB: event.target.value
            });
        };

        if (event.target.name === "telephone") {
            setState({
                ...stateRegister,
                telephone: event.target.value
            });
        };

        if (event.target.name === "email") {
            setState({
                ...stateRegister,
                email: event.target.value
            });
        };

        if (event.target.name === "experience") {
            setState({
                ...stateRegister,
                experience: event.target.value
            });
        };
    }


    //klik na register gumb
    async function handleClick(event) {

        let validation = validateRegistration(stateRegister, "register");
        

        if (!validation.success) {
            setError(validation.errorMsg);
        }
        else {
            setError("");
            setWaiting(true);
            var imageLink = "";
            try {
                if (stateRegister.slika) {
                    const response = await hostImage(stateRegister.slika.split(",")[1]);
                    if (response["status_code"] === 200) {
                        imageLink = response.image.display_url;
                    }
                    else {
                        imageLink = "";
                    }
                }
                const response = await register({
                    ...stateRegister,
                    photo: imageLink,
                    hashPassword: hash(stateRegister.password)
                });

                if (response.success) {
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
                    navigate("/" + from); //nazad na prosli page
                } else {
                    setError(response.message);
                    setWaiting(false);
                    return;
                }
            } catch (error) {
                setError("Server error");
                setWaiting(false);
                return;
            }
        } 
        return;
    } 
    
     //ostalo
    function redirectIfLoggedIn() {
        if (cookies.user) {
            navigate("/profile");
        }
    }
    useEffect(() => { 
        activeHeader(document, "LOGIN");
        redirectIfLoggedIn();
    }, []);

    const enterListener = event => {
            if (event.key === "Enter" || event.key === "NumpadEnter") {
                handleClick();
            };
    };
    
    //LOGIN

    //stateLogin, error1
    const [stateLogin, setState1] = useState({ username: "", password: ""});
    const [error1, setError1] = useState("");


    //handleChange spremanje
    function handleChange1(event){
        if (event.target.name === "username") {
            setState1({
                ...stateLogin,
                username: event.target.value
            });
        };

        if (event.target.name === "password") {
            setState1({
                ...stateLogin,
                password: event.target.value
            });
        };
    }
    //klik na login gumb
    async function handleClick1(event) {

        let validation = validateLogin(stateLogin);

        if (!validation.success) {
            setError1(validation.errorMsg);
        } else {
            setWaiting(true);
            setError1("");
            try {
                const response = await login({
                    ...stateLogin,
                    hashPassword: hash(stateLogin.password)
                });
                if (response.success === true) {
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
                    navigate("/" + from); //nazad na prosli page
                } else {
                    setWaiting(false);
                    setError1(response.message);
                    return;
                }
            }
            catch (error) {
                setWaiting(false);
                setError1(error.message);
                return;
            }
        }
    }

    const enterListener1 = event => {
        if (event.key === "Enter" || event.key === "NumpadEnter") {
            handleClick1();
        };
    };



     //html
    return(
        <Components.Container>
            <Components.SignUpContainer signinIn={signIn}>
                {/* register form */}
                <Components.Form>
                    <Components.Title>Register here</Components.Title>
                    <Components.Input type='text' placeholder='Username' name='username' autoComplete="username" value={stateRegister.username} onChange={handleChange} required/>
                    <Components.Input type='password' placeholder='Password' name='password' autoComplete="new-password" value={stateRegister.password} onChange={handleChange} required/>
                    <Components.Input type='text' placeholder='First Name' name='firstName' value={stateRegister.firstName} onChange={handleChange} required />
                    <Components.Input type='text' placeholder='Last Name' name='lastName' value={stateRegister.lastName} onChange={handleChange} required />
                    <Components.Line>  
                        <label>Gender:&nbsp;</label>     
                        <input type='radio' name='gender' value='M' checked={stateRegister.gender === "M"} onChange={handleChange} />
                        <label> M</label>
                        <input type='radio' name='gender' value='F' checked={stateRegister.gender === "F"} onChange={handleChange} />
                        <label>F </label> 
                    </Components.Line> 
                    <Components.Input type='tel' placeholder='Phone Number' name='telephone' value={stateRegister.telephone} onChange={handleChange} required />
                    <Components.Line>
                        <label>Birth:&nbsp;&nbsp;&nbsp;</label>
                        <input type='date' name='DOB' value={stateRegister.DOB} onChange={handleChange} />
                    </Components.Line>
                    <Components.Input type='email' placeholder='Email' name='email' value={stateRegister.email} onChange={handleChange} required />
                    <Components.Line>
                        <label>Dancing experience:</label>
                        <textarea name='experience' value={stateRegister.experience} onChange={handleChange} />
                    </Components.Line>
                    <Components.Line>
                        <label>Photo:&nbsp;&nbsp;&nbsp;</label>
                        <input type='file' accept='image/*' onChange={ (e) => handleFileChange(e, stateRegister, setState) } multiple={false}/>
                    </Components.Line>
                     {/* gumb za registraciju */}
                    <Components.Button type="button" onClick={handleClick} disabled={waiting}>Register</Components.Button>
                    {error && <h1 className="errorMsg">{error}</h1>}
                </Components.Form>
                
            </Components.SignUpContainer>


            <Components.SignInContainer signinIn={signIn}>
                {/* login forma*/}
                <Components.Form>
                    <Components.Title>Login </Components.Title>
                    <Components.Input type='text' name='username' placeholder='Username' autoComplete="username"  value={stateLogin.username} onChange={handleChange1} required />
                    <Components.Input type='password' name='password' placeholder='Password' autoComplete="current-password" value={stateLogin.password} onChange={handleChange1} required />
                      {/* login gumb */}
                    <Components.Button type="button" onClick={handleClick1} disabled={waiting}>Login</Components.Button>
                    {error1 && <h1 className="errorMsg">{error1}</h1>}
                </Components.Form>

            </Components.SignInContainer>

            <Components.OverlayContainer signinIn={signIn}>
                <Components.Overlay signinIn={signIn}>

                <Components.LeftOverlayPanel signinIn={signIn}>
                    <Components.Title2>You already have an account?</Components.Title2>
                    <Components.Paragraph2>
                        Login with your personal info
                    </Components.Paragraph2>
                    <Components.GhostButton2 onClick={() => toggle(true)}>
                        Login here
                    </Components.GhostButton2>
                    </Components.LeftOverlayPanel>

                    <Components.RightOverlayPanel signinIn={signIn}>
                    <Components.Title1>Are you new?</Components.Title1>
                    <Components.Paragraph>
                        Enter your personal info and enjoy dancing
                    </Components.Paragraph>
                        <Components.GhostButton onClick={() => toggle(false)}>
                            Register here
                        </Components.GhostButton> 
                    </Components.RightOverlayPanel>

                </Components.Overlay>
            </Components.OverlayContainer>

        </Components.Container>
    )
}

export default LoginPage;