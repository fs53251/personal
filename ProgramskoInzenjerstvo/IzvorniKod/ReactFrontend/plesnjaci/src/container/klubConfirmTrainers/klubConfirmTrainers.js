import "./klubConfirmTrainers.css"
import { useState } from "react";
import DataTable from "react-data-table-component";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";
import { getKlub } from "../../utils/axios/backendCalls/clubEndpoints";
import { getNepotvrdeniTreneriOdKluba } from "../../utils/axios/backendCalls/trenerEndpoints";
import { useCookies } from "react-cookie";

const KlubConfirmTrainers = () => {
    
    var imeIVlasnik = window.location.pathname.indexOf("club/") + 5;
    imeIVlasnik = window.location.pathname.substring(imeIVlasnik);
    const vlasnik = imeIVlasnik.substring(0, imeIVlasnik.indexOf("/"));
    const imeKluba = imeIVlasnik.substring(imeIVlasnik.indexOf("/") + 1, imeIVlasnik.lastIndexOf("/")).replaceAll("%20", " ");

    const [searchInput, setSearchInput] = useState("");

    const [allTrainers, setAllTrainers] = useState([]);

    const [cookies] = useCookies(['user']);

    const navigate = useNavigate();

    async function fetchAllEvents() {
        try {
            const responseKlub = await getKlub(imeKluba, vlasnik);
            const response = await getNepotvrdeniTreneriOdKluba(responseKlub.klub.klubId);
            setAllTrainers(response.listaKlijenata)
        }
        catch {
            console.log("Error fetching events");
        }
    }

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    };

    const trenerFilter = (trener) => {
        const imeIPrezime = trener.ime + " " + trener.prezime;
        if (trener.korisnicko_ime.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (imeIPrezime.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        return false;
    }

    useEffect(() => {
        fetchAllEvents();
        activeHeader(document, "MY CLUBS")
    }, []);

    return (cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") ? (
        <div className="klub-all-events">
            <h1>"{imeKluba}" Trainer requests</h1>
            
            <div className="all-users-search-wrap">
                <input className="all-users-searchBar" type="search" placeholder="Search..." onChange={handleChange} value={searchInput} />
            </div>

            
            <div className="all-dances-table">
                <DataTable
                    columns={[
                        {
                            name: "Username",
                            selector: row => row.korisnicko_ime,
                            width: "250px",
                            center: true,
                        },
                        {
                            name: "First Name",
                            selector: row => row.ime,
                            width: "250px",
                            center: true,
                        },
                        {
                            name: "Last Name",
                            selector: row => row.prezime,
                            width: "250px",
                            center: true,
                        },
                    ]}
                    data={allTrainers.filter(trener => trenerFilter(trener))}
                    defaultSortFieldId="startingTime"
                    onRowClicked={row => navigate("/club/" + vlasnik + "/" + imeKluba + "/confirm-trainer/" + row.korisnicko_ime)}
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
    ) : <div className="error-naslov"> <h1 className="errorMsg">You are not the owner of this club</h1> </div>
};

export default KlubConfirmTrainers;