import "./adminConfirmClubsPage.css"
import { useCookies } from "react-cookie";
import { useState } from "react";
import { useEffect } from "react";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { getAllNepotvrdeniKlubovi } from "../../utils/axios/backendCalls/clubEndpoints";
import { useNavigate } from "react-router-dom";
import DataTable from "react-data-table-component";
import { IoCheckmarkCircleSharp, IoCloseCircleSharp } from "react-icons/io5";
import { deleteKlub } from "../../utils/axios/backendCalls/clubEndpoints";
import { potvrdiKlub } from "../../utils/axios/backendCalls/clubEndpoints";
import activeHeader from "../../utils/activeHeader";

const AdminConfirmClubsPage = () => {

    const [cookies, setCookie] = useCookies(['user'])
    const user = cookies.user;

    const [allClubs, setAllClubs] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    
    const [waiting, setWaiting] = useState(false);

    const navigate = useNavigate();

    function redirectIfNotLoggedIn() {
        if (!user) {
            navigate("/login", { state: { from: "admin/clubs" } });
        }
    }

    async function fetchAllClubs() {
        if (cookies.user.type !== "Administrator") return;
        try {
            const response = await getAllNepotvrdeniKlubovi();
            // ovdje zelimo sve NEPOTVRDENE klubove
            setAllClubs(response.listaKlubova);
        }
        catch {
            console.log("Error fetching clubs");
        }
    }

    const confirmClub = async (imeKluba, korisnickoIme) => {
        if (waiting) return;
        setWaiting(true);
        const res = await potvrdiKlub(imeKluba, korisnickoIme);
        if (res) {
            fetchAllClubs();
        }
        setWaiting(false);
    }

    const deleteClub = async (imeKluba, korisnickoIme) => {
        if (waiting) return;
        setWaiting(true);
        const res = await deleteKlub(imeKluba, korisnickoIme);
        if (res) {
            fetchAllClubs();
        }
        setWaiting(false);
    }

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    };

    const clubFilter = (klub) => {
        if (klub.imeKluba.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (klub.vlasnik.korisnicko_ime.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (klub.email.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        return false;
    }



    useEffect(() => {
        activeHeader(document, "ADMIN");
        redirectIfNotLoggedIn();
        refreshCookie(cookies, setCookie);
        fetchAllClubs();
    }, []);


    return user?.type==="Administrator" ? (
        <div className="admin-clubs-page">
            <div className="all-clubs-search-wrap">
                <input className="all-clubs-searchBar" type="search" placeholder="Search..." onChange={handleChange} value={searchInput} />
            </div>

            <div className="all-clubs-table">
                <DataTable
                    columns={[
                        {
                            name: "Club name",
                            selector: row => row.imeKluba,
                            width: "200px",
                            center: true,
                        },
                        {
                            name: "Club email",
                            selector: row => row.email,
                            width: "200px",
                            center: true,
                        },
                        {
                            name: "Owner",
                            selector: row => row.vlasnik.korisnicko_ime,
                            width: "200px",
                            center: true,
                        },
                        {   
                            selector: row => <label className="tableButton" onClick={(e) => confirmClub(row.imeKluba, row.vlasnik.korisnicko_ime)}><IoCheckmarkCircleSharp/></label>,
                            width: "75px",
                            center: true,
                        },
                        {   
                            selector: row => <label className="tableButton buttonNo" onClick={(e) => deleteClub(row.imeKluba, row.vlasnik.korisnicko_ime)}><IoCloseCircleSharp/></label>,
                            width: "75px",
                            center: true,
                        }
                    ]}
                    data={allClubs.filter(club => clubFilter(club))}
                    onRowClicked={row => navigate("/club/" + row.vlasnik.korisnicko_ime + "/" + row.imeKluba)}
                    highlightOnHover
                    pointerOnHover
                />
            </div>

        </div >
    ) : <h1 className="unauthorized">Unauthorized</h1>;
};
export default AdminConfirmClubsPage;