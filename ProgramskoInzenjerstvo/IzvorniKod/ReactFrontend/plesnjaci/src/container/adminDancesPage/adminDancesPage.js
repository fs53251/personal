import "./adminDancesPage.css"
import { useCookies } from "react-cookie";
import { useState } from "react";
import { useEffect } from "react";
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { getAllPlesovi } from "../../utils/axios/backendCalls/plesEndpoints";
import { useNavigate } from "react-router-dom";
import DataTable from "react-data-table-component";
import activeHeader from "../../utils/activeHeader";

const AdminDancesPage = () => {
    const [cookies, setCookie] = useCookies(['user'])
    const user = cookies.user;

    const [allDances, setAllDances] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    
    const navigate = useNavigate();

    function redirectIfNotLoggedIn() {
        if (!user) {
            navigate("/login", { state: { from: "admin/dances" } });
        }
    }

    async function fetchAllDances() {
        if (cookies.user.type !== "Administrator") return;
        try {
            const response = await getAllPlesovi();
            setAllDances(response.listaPlesova);
        }
        catch {
            console.log("Error fetching dances");
        }
    }

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    };

    const danceFilter = (ples) => {
        if (ples.naziv.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (ples.opis.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        return false;
    }



    useEffect(() => {
        activeHeader(document, "ADMIN");
        redirectIfNotLoggedIn();
        refreshCookie(cookies, setCookie);
        fetchAllDances();
    }, []);


    return user?.type==="Administrator" ? (
        <div className="admin-dances-page">
            <input className="admin-button" type="button" name="create-dance" value="Create new dance" onClick={e => navigate("/dance/new", { state: { from: "admin/dances" } })} /> <br/>
            <div className="all-dances-search-wrap">
                <input className="all-dances-searchBar" type="search" placeholder="Search..." onChange={handleChange} value={searchInput} />
            </div>
            <div className="all-dances-table">
                <DataTable
                    columns={[
                        {
                            name: "Name",
                            selector: row => row.naziv,
                            width: "200px",
                            center: true,
                        },
                        {
                            name: "Description",
                            selector: row => row.opis,
                            width: "200px",
                            center: true,
                        }
                    ]}
                    data={allDances.filter(dance => danceFilter(dance))}
                    onRowClicked={row => navigate("/dance/" + row.naziv)}
                    highlightOnHover
                    pointerOnHover
                />
            </div>

        </div >
    ) : <h1 className="unauthorized">Unauthorized</h1>;
};
export default AdminDancesPage;