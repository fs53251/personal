import "./appHeader.css";
import {activeitem,openmenu} from "./activeitem.js"
import { getHeaderItems } from "./headerItems";
import { Link } from "react-router-dom";
import { useEffect } from "react";
import { IoReorderThreeOutline } from "react-icons/io5";
import { useCookies } from 'react-cookie'
import {IoLogInOutline, IoLogOutOutline, IoTerminalOutline} from "react-icons/io5";

const AppHeader = () => {
    const headerItems = getHeaderItems;

    const toggle = (
        <div className='toggle'>
            <Link className="toggle-icon" onClick={openmenu}>
                <span className="icon"><IoReorderThreeOutline/></span>
            </Link>
        </div>
    );

    const showSidebar = () => {
        if(localStorage.getItem("opensidebar")===null){
            localStorage.setItem("opensidebar", "false");
        }
        if(localStorage.getItem("opensidebar") === "true"){
            document.querySelector('.menubar').classList.add("active");
        }else{
            document.querySelector('.menubar').classList.remove("active");
        }
    }

    const [cookies, setCookie, removeCookie] = useCookies(['user']);

    const user = cookies.user;

    const logout = () => {
        removeCookie('user');
        window.location.reload();
    }

    const loginButton = () => {
    
        if (user) {
            return (
                <div className="nth-item" key={"/logout"}>
                
                    <Link
                        className="menu-item"
                        to="/logout"
                        aria-label="LOGOUT"
                    >
                        <b></b>
                        <b></b>
                        <span className="icon"><IoLogOutOutline /></span>
                        <span className="title">LOGOUT</span>
                    </Link>

                </div>
            );
        } else {
            return (<div className="nth-item" key={"/login"}>
                
                <Link
                    className="menu-item"
                    to="/login"
                    aria-label="LOGIN"
                >
                    <b></b>
                    <b></b>
                    <span className="icon"><IoLogInOutline /></span>
                    <span className="title">LOGIN</span>
                </Link>
            </div>);
        }
    }
    const adminButton = () => {
        if (user && user.type=== "Administrator") {
            return (<div className="nth-item" key={"/admin"}>
                
                <Link
                    className="menu-item"
                    to="/admin"
                    aria-label="ADMIN"
                >
                    <b></b>
                    <b></b>
                    <span className="icon"><IoTerminalOutline /></span>
                    <span className="title">ADMIN</span>
                </Link>
            </div>);
        }
    }
    
    useEffect(() => {
        showSidebar();
        activeitem();
    }, []);
    
    
    return (
        <div className="app-header">
            <div className="menubar noanimation">
                <nav className="header-content">
                    {toggle}
                    {adminButton()}
                    {headerItems.map(item => {
                        return (
                            <div className="nth-item" key={item.link}>
                                <Link
                                    className="menu-item"
                                    to={item.link}
                                    aria-label={item.label}
                                    id={item.id}
                                >
                                    <b></b>
                                    <b></b>
                                    <span className="icon">{item.icon}</span>
                                    <span className="title">{item.label}</span>
                                </Link>
                            </div>
                        );
                    })}
                    {loginButton()}
                </nav>
            </div>
        </div>
    );
}

export default AppHeader;