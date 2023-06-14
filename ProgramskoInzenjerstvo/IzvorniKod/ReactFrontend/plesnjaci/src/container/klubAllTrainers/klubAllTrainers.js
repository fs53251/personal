import "./klubAllTrainers.css"
import { useState } from "react";
import DataTable from "react-data-table-component";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";
import { getKlub } from "../../utils/axios/backendCalls/clubEndpoints";
import { fetchPrijava, getPotvrdeniTreneriOdKluba, odbijPrijavu } from "../../utils/axios/backendCalls/trenerEndpoints";
import { IoCloseCircleSharp } from "react-icons/io5";
import { createClient } from "@supabase/supabase-js";
import { useCookies } from "react-cookie";

const KlubAllTrainers = () => {
    

    const supabaseUrl = 'https://kegpfrlpsvonohcjccaj.supabase.co'
    const supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtlZ3Bmcmxwc3Zvbm9oY2pjY2FqIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NzIyNTM3NjgsImV4cCI6MTk4NzgyOTc2OH0.xlobxPj-8BkTzyBCWgkBmCKFav8RHrlRLnwuyvtgTxI"
    const supabase = createClient(supabaseUrl, supabaseKey)


    var imeIVlasnik = window.location.pathname.indexOf("club/") + 5;
    imeIVlasnik = window.location.pathname.substring(imeIVlasnik);
    const vlasnik = imeIVlasnik.substring(0, imeIVlasnik.indexOf("/"));
    const imeKluba = imeIVlasnik.substring(imeIVlasnik.indexOf("/") + 1, imeIVlasnik.lastIndexOf("/")).replaceAll("%20", " ");

    const [searchInput, setSearchInput] = useState("");

    const [allTrainers, setAllTrainers] = useState([]);

    const [cookies] = useCookies(['user']);

    const navigate = useNavigate();

    async function fetchAllTrainers() {
        try {
            const responseKlub = await getKlub(imeKluba, vlasnik);
            const response = await getPotvrdeniTreneriOdKluba(responseKlub.klub.klubId);
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
    const handleDelete = async (trener) => {
        
        try {
            const prijava = await fetchPrijava(vlasnik, imeKluba, trener);
            const { data, error } = await supabase.storage.from('trainer-certificates').remove(["public/" + prijava.prijava.potvrda]);
            if (error) {
                console.log("Error deleting file");
                return;
            }
            
            const response = await odbijPrijavu(vlasnik, imeKluba, trener);
            if (response.success) {
                fetchAllTrainers();
                return;
            }
            else {
                console.log("Error declining request");
                return
            }
        }
        catch {
            console.log("Error deleting trainer request");
        }
    }

    useEffect(() => {
        fetchAllTrainers();
        activeHeader(document, "HOME PAGE")
    }, []);

    return (
        <div className="klub-all-events">
            <h1>"{imeKluba}" All Trainers</h1>

            {(cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") && <input className="admin-button" type="button" name="confirm-trainers" value="Confirm trainers" onClick={(e) => navigate("/club/" + vlasnik + "/" + imeKluba + "/confirm-trainers")} />} <br />
            
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
                        (cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") ? 
                            { 
                                selector: row => <label title="Delete trainer" className="tableButton buttonNo" onClick={(e) => handleDelete(row.korisnicko_ime)}><IoCloseCircleSharp /></label>,
                                width: "75px",
                                center: true,
                            } : []
                        ]}
                        data={allTrainers.filter(trener => trenerFilter(trener))}
                        onRowClicked={row => navigate("/profile/" + row.korisnicko_ime)}
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

export default KlubAllTrainers;