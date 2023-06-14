import DataTable from "react-data-table-component";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";
import "./klubAllCourses.css"
import { getTecajeviOdKluba } from "../../utils/axios/backendCalls/tecajEndpoints";
import { getKlub } from "../../utils/axios/backendCalls/clubEndpoints";
import { useCookies } from "react-cookie";

const KlubAllCourses = () => {
    var imeIVlasnik = window.location.pathname.indexOf("club/") + 5;
    imeIVlasnik = window.location.pathname.substring(imeIVlasnik);
    const vlasnik = imeIVlasnik.substring(0, imeIVlasnik.indexOf("/"));
    const imeKluba = imeIVlasnik.substring(imeIVlasnik.indexOf("/") + 1, imeIVlasnik.lastIndexOf("/")).replaceAll("%20", " ");

    const [allTecajevi, setAllTecajevi] = useState([]);
    const [searchInput, setSearchInput] = useState("");

    const [cookies] = useCookies(['user']);

    const navigate = useNavigate();

    async function fetchAllCourses() {
        try {
            const response = await getTecajeviOdKluba(imeKluba, vlasnik);
            setAllTecajevi(response.listaTecajeva)
        }
        catch {
            console.log("Error fetching courses");
        }
    }

    const handleChange = (e) => {
        e.preventDefault();
        setSearchInput(e.target.value);
    };

    const tecajFilter = (tecaj) => {
        if (tecaj.rokPrijave < new Date().toISOString()) {
            return false;
        }

        const imeIPrezime = tecaj.trener.ime + " " + tecaj.trener.prezime;
        if (tecaj.ogranicenja.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (imeIPrezime.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        if (tecaj.tipPlesa.naziv.toLowerCase().includes(searchInput.toLowerCase())) {
            return true;
        }
        return false;
    }

    async function addCourse(event) {
        try {
            const response = await getKlub(imeKluba, vlasnik);
            if (response.success) {
                navigate("/club/course/new", {
                    state: {
                        imeKluba: imeKluba,
                        vlasnik: vlasnik,
                        klubId: response.klub.klubId,
                        adresa: response.klub.dvorana.adresa
                    }
                });
            } else {
                console.log("Error redirecting.")
            }
        } catch {
            console.log("Error redirecting.")
        }
    }


    useEffect(() => {
        fetchAllCourses();
        activeHeader(document, "HOME PAGE")
    }, []);

    return (
        <div className="klub-all-events">
            <h1>"{imeKluba}" Upcoming Courses</h1>

            {(cookies.user?.username === vlasnik || cookies.user?.type === "Administrator") && <input className="admin-button" type="button" name="confirm-trainers" value="Add new course" onClick={addCourse} />} <br />

            <div className="all-users-search-wrap">
                <input className="all-users-searchBar" type="search" placeholder="Search..." onChange={handleChange} value={searchInput} />
            </div>
            <div className="all-courses-table">
                <DataTable
                        columns={[
                            {
                                name: "Dance type",
                                selector: row => row.tipPlesa.naziv,
                                width: "200px",
                                center: true,
                            },
                            {
                                name: "Capacity",
                                selector: row => row.kapacitetGrupe,
                                width: "100px",
                                center: true,
                            },
                            {
                                name: "Limitations",
                                selector: row => row.ogranicenja,
                                width: "200px",
                                center: true,
                        },
                            {
                                name: "Application deadline",
                                selector: row => row.rokPrijave,
                                width: "200px",
                                center: true,
                            },
                            {
                                name: "Trainer",
                                selector: row => row.trener.ime + " " + row.trener.prezime,
                                width: "300px",
                                center: true,
                            },
                    ]}
                        data={allTecajevi.filter(tecaj => tecajFilter(tecaj))}
                        defaultSortFieldId="startingTime"
                        onRowClicked={row => navigate("/club/course/" + row.tecajId)}
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

export default KlubAllCourses;