import "./klubAllEvents.css"
import { getPlesnjaciOdKluba } from "../../utils/axios/backendCalls/plesnjakEndpoints";
import { useState } from "react";
import DataTable from "react-data-table-component";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";
import { useCookies } from "react-cookie";

const KlubAllEvents = () => {
    
    var imeIVlasnik = window.location.pathname.indexOf("club/") + 5;
    imeIVlasnik = window.location.pathname.substring(imeIVlasnik);
    const vlasnik = imeIVlasnik.substring(0, imeIVlasnik.indexOf("/"));
    const imeKluba = imeIVlasnik.substring(imeIVlasnik.indexOf("/") + 1, imeIVlasnik.lastIndexOf("/")).replaceAll("%20", " ");

    const [searchInput, setSearchInput] = useState("");

    const [allPlesnjaci, setAllPlesnjaci] = useState([]);

    const [cookies] = useCookies(['user']);

    const navigate = useNavigate();

    async function fetchAllEvents() {
        try {
            const response = await getPlesnjaciOdKluba(imeKluba, vlasnik);
            setAllPlesnjaci(response.listaPlesnjaka)
        }
        catch {
            console.log("Error fetching events");
        }
    }

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    };

    const plesnjakFilter = (plesnjak) => {
        if (plesnjak.vrijeme < new Date().toISOString()) {
            return false;
        }
        if (plesnjak.naziv.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (plesnjak.opis.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        return false;
    }

    function addPlesnjak(event){
        navigate("/club/event/new", {
            state: {
                imeKluba: imeKluba,
                vlasnik: vlasnik
            }
        });
    }

    useEffect(() => {
        fetchAllEvents();
        activeHeader(document, "HOME PAGE")
    }, []);

    return (
        <div className="klub-all-events">
            <h1>"{imeKluba}" Upcoming Events</h1>

            {(cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") && <input className="admin-button" type="button" name="confirm-trainers" value="Add new event" onClick={addPlesnjak} />} <br />

            <div className="all-users-search-wrap">
                <input className="all-users-searchBar" type="search" placeholder="Search..." onChange={handleChange} value={searchInput} />
            </div>
            <div className="all-dances-table">
                <DataTable
                        columns={[
                            {
                                name: "Name",
                                selector: row => row.naziv,
                                width: "300px",
                                center: true,
                            },
                            {
                                name: "Description",
                                selector: row => row.opis,
                                width: "300px",
                                center: true,
                            },
                            {
                                id: "startingTime",
                                name: "Starting time",
                                selector: row => row.vrijeme,
                                width: "300px",
                                center: true,
                            },
                        ]}
                        data={allPlesnjaci.filter(plesnjak => plesnjakFilter(plesnjak))}
                        defaultSortFieldId="startingTime"
                        onRowClicked={row => navigate("/club/event/" + row.plesnjakId)}
                        highlightOnHover
                        pointerOnHover
                        rowHeights={40}
                        customStyles={
                            {
                                rows: {
                                    style: {
                                        minHeight: "72px", // override the row height
                                        fontSize: "16px",
                                        fontWeight: "bold",
                                        color: "white",
                                        backgroundColor: "rgb(174, 4, 174)",
                                        "&:hover": {
                                            backgroundColor: "rgba(0, 0, 0, 0.04)",
                                        },
                                    },
                                },
                            }
                        }
                        
                />
            </div>
        </div>
    );
}

export default KlubAllEvents;