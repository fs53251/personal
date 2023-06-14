import "./calendarPage.css"
import { useEffect, useState } from "react";
import { useCookies } from 'react-cookie';
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { useNavigate } from "react-router-dom";
import { fetchAllTermini } from "../../utils/axios/backendCalls/tecajEndpoints";
import activeHeader from "../../utils/activeHeader";
import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from "@fullcalendar/interaction"

const CalendarPage = () => {

    const navigate = useNavigate();

    const [terminiKlijent, setTerminiKlijent] = useState([]);
    const [terminiTrener, setTerminiTrener] = useState([]);
    const [renderedOutput, setRenderedOutput] = useState([]);

    const [cookies, setCookie] = useCookies(['user'])

    const getAllTermini = async () => {
        const response = await fetchAllTermini(cookies.user.username);
        if (response.success) {
            setTerminiKlijent(response.terminiKlijent);
            setTerminiTrener(response.terminiTrener);
        }
        let listaEvenata = [];
        response.terminiKlijent.map(termin => listaEvenata.push({ id: termin.tecaj.tecajId, title: termin.tecaj.tipPlesa.naziv + " course", start: termin.vrijeme, color: "blue" }));
        response.terminiTrener.map(termin => listaEvenata.push({ id: termin.tecaj.tecajId, title: termin.tecaj.tipPlesa.naziv + " course", start: termin.vrijeme, color: "red" }));
        setRenderedOutput(listaEvenata);
    }

    function redirectIfNotLoggedIn() {
        if (!cookies.user) {
            navigate("/login", { state: { from: "calendar" } });
        }
    }

    function addOneHour(startTime) {
        let lastChar = startTime.charAt(startTime.length - 1);

        let start = startTime.toUpperCase() + "M";
        let sati = "";
        let minute = "";
        if (startTime.includes(":")) {
            sati = parseInt(startTime.split(":")[0]) + 1;
            minute = ":" + startTime.split(":")[1].substring(0, 2);
        }
        else {
            sati = parseInt(startTime.substring(0, 2)) + 1;
            minute = "";
        }
        

        if (sati > 12) {
            sati = sati - 12;
            let newTime = sati +  minute + (lastChar === "p" ? "AM" : "PM");
            return start + " - " + newTime;
        }
        else {
            let newTime = sati + minute + (lastChar === "p" ? "PM" : "AM");
            return start + " - " + newTime;
        }

    }

    function generateEventContent(eventInfo) {
        return (
            <div className="eventContent" style={{ "background": eventInfo.backgroundColor }} >
                <p className="eventTitle">{eventInfo.event.title}</p>
                <h1 className="eventTime">{addOneHour(eventInfo.timeText)}</h1>
            </div>
        )
    }

    useEffect(() => {
        redirectIfNotLoggedIn();
        activeHeader(document, "MY CALENDAR");
        refreshCookie(cookies, setCookie);
        getAllTermini();
    }, []);


    return (
        <div className="calendar-page-wrapper">
            <div className="legenda">
                <div className="legenda-item" style={{ "background": "red", "color" : "white" }}>
                    <p className="legenda-item-text">As a trainer</p>
                </div>
                <div className="legenda-item" style={{ "background": "blue", "color" : "white" }}>
                    <p className="legenda-item-text">As a client</p>
                </div>
            </div>
            <div className="calendarWrapper">
                <FullCalendar
                    plugins={[interactionPlugin, dayGridPlugin]}
                    headerToolbar={{
                        left: 'prev,next today',
                        center: 'title',
                        right: '',
                    }}
                    initialView="dayGridMonth"
                    events={renderedOutput}
                    eventClick={(e) => navigate("/club/course/" + e.event.id)}
                    eventContent={eventInfo => generateEventContent(eventInfo)}
                />
            </div>
        </div>
    );
};

export default CalendarPage;