import "./adminClubsPage.css"
import { useCookies } from "react-cookie";
import { useState } from "react";
import { useEffect } from "react";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { getAllPotvrdeniKlubovi } from "../../utils/axios/backendCalls/clubEndpoints";
import { useNavigate } from "react-router-dom";
import DataTable from "react-data-table-component";
import activeHeader from "../../utils/activeHeader";

const AdminClubsPage = () => {

    const [cookies, setCookie] = useCookies(['user'])
    const user = cookies.user;

    const [allClubs, setAllClubs] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    
    const navigate = useNavigate();

    function redirectIfNotLoggedIn() {
        if (!user) {
            navigate("/login", { state: { from: "admin/clubs" } });
        }
    }

    async function fetchAllClubs() {
        if (cookies.user.type !== "Administrator") return;
        try {
            const response = await getAllPotvrdeniKlubovi();
            // ovdje zelimo sve POTVRDENE klubove
            setAllClubs(response.listaKlubova);
        }
        catch {
            console.log("Error fetching clubs");
        }
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
            <input className="admin-button" type="button" name="confirm-clubs" value="Confirm clubs" onClick={e => navigate("/admin/confirm-clubs")} /> <br/>
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
export default AdminClubsPage;