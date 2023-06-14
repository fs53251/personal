import "./adminClientsPage.css"
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { getAllUsers, toggleAdministrator } from "../../utils/axios/backendCalls/userEndpoints";
import { IoTerminalSharp, IoCloseCircleSharp } from "react-icons/io5";
import { useState } from "react";
import DataTable from "react-data-table-component";
import activeHeader from "../../utils/activeHeader";

const AdminClientsPage = () => {
    const [cookies, setCookie] = useCookies(['user'])
    const user = cookies.user;

    const [allClients, setAllClients] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    
    const navigate = useNavigate();

    function redirectIfNotLoggedIn() {
        if (!user) {
            navigate("/login", { state: { from: "admin/clients" } });
        }
    }

    async function fetchAllClients() {
        if (cookies.user.type !== "Administrator") return;
        
        try {
            const response = await getAllUsers();
            setAllClients(response.listaKlijenata);
        }
        catch {
            console.log("Error fetching clients");
        }
    }

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    };

    const clientFilter = (client) => {
        const imeIPrezime = client.ime + " " + client.prezime;
        if (client.korisnicko_ime.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (imeIPrezime.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        return false;
    }

    const toggleAdmin = async (username) => {
        try {
            const response = await toggleAdministrator(username);
            if (response.success) {
                const pogodjeni = allClients.find(client => client.korisnicko_ime === username);
                pogodjeni.tipKorisnika = pogodjeni.tipKorisnika === "Administrator" ? "Klijent" : "Administrator";
                setAllClients([...allClients]);
            } else {
                console.log("Error toggling admin");
            }
        } catch (error) {
            console.log("Error toggling admin");
        }
    }



    useEffect(() => {
        activeHeader(document, "ADMIN");
        redirectIfNotLoggedIn();
        refreshCookie(cookies, setCookie);
        fetchAllClients();
    }, []);


    return user?.type==="Administrator" ? (
        <div className="admin-clients-page">
            <div className="all-users-search-wrap">
                <input className="all-users-searchBar" type="search" placeholder="Search..." onChange={handleChange} value={searchInput} />
            </div>

            <div className="all-users-table">
                <DataTable
                    columns={[
                        {
                            name: "Username",
                            selector: row => row.korisnicko_ime,
                            width: "200px",
                            center: true,
                        },
                        {
                            name: "First Name",
                            selector: row => row.ime,
                            width: "200px",
                            center: true,
                        },
                        {
                            name: "Last Name",
                            selector: row => row.prezime,
                            width: "200px",
                            center: true,
                        },
                        {
                            id: "tipKorisnika",
                            selector: row => row.tipKorisnika,
                            cell: row => row.tipKorisnika === "Administrator" && <label title="Administrator" className="isAdmin"><IoTerminalSharp /></label>,
                            width: "75px",
                            center: true,
                        },
                        {
                            cell: row => row.tipKorisnika === "Administrator" ? <label title="Remove administrator role" className="removeAdminButton" onClick={(e) => toggleAdmin(row.korisnicko_ime)}><IoCloseCircleSharp /></label>
                            : <label title="Grant administrator role" className="grantAdminButton" onClick={(e) => toggleAdmin(row.korisnicko_ime)}><IoTerminalSharp /></label> ,
                            width: "75px",
                            center: true,
                        }
                    ]}
                    data={allClients.filter(client => clientFilter(client))}
                    onRowClicked={row => navigate("/profile/" + row.korisnicko_ime)}
                    defaultSortFieldId="tipKorisnika"
                    highlightOnHover
                    pointerOnHover
                />
            </div>

        </div >
    ) : <h1 className="unauthorized">Unauthorized</h1>;
};

export default AdminClientsPage;