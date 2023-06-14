import { useEffect, useState } from "react";
import DataTable from "react-data-table-component";
import { IoCheckmarkSharp, IoCloseCircleSharp } from "react-icons/io5";
import { useNavigate } from "react-router-dom";
import activeHeader from "../../utils/activeHeader";
import { getTecaj, odbijUpisZaTecaj, prihvatiUpisZaTecaj, sviUpisiZaTecaj } from "../../utils/axios/backendCalls/tecajEndpoints";
import "./tecajEnrolled.css"
import { useCookies } from "react-cookie";
const TecajEnrolled = () => {

    const navigate = useNavigate();


    const tecajId = window.location.pathname.split("/")[3];

    const [tecaj, setTecaj] = useState({})
    const [upisi, setUpisi] = useState([]);
    const [preostaloMjesta, setPreostaloMjesta] = useState(0);
    const [isDisabled, setIsDisabled] = useState(false);

    const [waiting, setWaiting] = useState(false);

    const [searchInput, setSearchInput] = useState("");

    const [cookies] = useCookies(['user']);

    
    const fetchTecaj = async () => {
        try {
            const tecaj = await getTecaj(tecajId);
            if (tecaj == null) {
                return;
            }
            setTecaj(tecaj.tecaj);
            setPreostaloMjesta(tecaj.tecaj.kapacitetGrupe);
            setIsDisabled(new Date(tecaj.tecaj.rokPrijave.replace(" ", "T")) > new Date());

            fetchUpisi(tecaj.tecaj);
        } catch (error) {
            console.log(error);
        }
    }

    const fetchUpisi = async (tecaj) => {
        try {
            const upisi = await sviUpisiZaTecaj(tecajId);
            if (upisi == null) {
                return;
            }
            setUpisi(upisi.listaUpisaTecaja);

            let brojMjesta = tecaj.kapacitetGrupe;
            upisi.listaUpisaTecaja.forEach(element => {
                if (element.jePotvrden === true) {
                    brojMjesta = brojMjesta - 1;
                }
            });
            setPreostaloMjesta(brojMjesta);
        } catch (error) {
            console.log(error);
        }
    }


    const prihvatiUpis = async (upisId) => {
        if (preostaloMjesta <= 0 || isDisabled || waiting) {
            return;
        }
        try {
            setWaiting(true);
            const prihvat = await prihvatiUpisZaTecaj(upisId);
            if (prihvat == null) {
                setWaiting(false);
                return;
            }
            const kopijaUpisa = [...upisi];
            const pogodjeni = kopijaUpisa.find(upis => upis.upisTecajId === upisId);
            pogodjeni.jePotvrden = true;
            setUpisi(kopijaUpisa);
            setPreostaloMjesta(preostaloMjesta - 1);
            setWaiting(false);
        } catch (error) {
            setWaiting(false);
            console.log(error);
        }
    }

    const odbijUpis = async (upisId) => {
        if (isDisabled || waiting) {
            return;
        }
        try {
            setWaiting(true);
            const prihvat = await odbijUpisZaTecaj(upisId);
            if (prihvat == null) {
                setWaiting(false);
                return;
            }
            const kopijaUpisa = [...upisi];
            const pogodjeni = kopijaUpisa.find(upis => upis.upisTecajId === upisId);
            pogodjeni.jePotvrden = false;
            setUpisi(kopijaUpisa);
            setPreostaloMjesta(preostaloMjesta + 1);
            setWaiting(false);
        } catch (error) {
            setWaiting(false);
            console.log(error);
        }
    }

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



    useEffect(() => {
        fetchTecaj();
        activeHeader(document, "MY CLUBS");
    }, []);

    return (cookies.user?.username === tecaj.klub?.vlasnik.korisnicko_ime || cookies.user?.type === "Administrator" || cookies.user?.username === tecaj.trener?.korisnicko_ime) ? (
        <div className="course-all-enrolled">
            {isDisabled && <div className="course-all-enrolled-header">
                <h1 className="redtext">Applications are still in progress</h1>
            </div>}
            <div className="course-all-enrolled-header">
                <h1>Remaining space: {preostaloMjesta}</h1>
            </div>
            <div className="all-users-search-wrap">
                <input className="all-users-searchBar" type="search" placeholder="Search..." onChange={(e) => setSearchInput(e.target.value)} value={searchInput} />
            </div>
            <div className="all-dances-table">
                <DataTable
                            columns={[
                                {
                                    name: "Username",
                                    selector: row => "@" + row.korisnik.korisnicko_ime,
                                    width: "250px",
                                    center: true,
                                },
                                {
                                    name: "First Name",
                                    selector: row => row.korisnik.ime,
                                    width: "250px",
                                    center: true,
                                },
                                {
                                    name: "Last Name",
                                    selector: row => row.korisnik.prezime,
                                    width: "250px",
                                    center: true,
                                },
                                {
                                    id: "potvrden",
                                    selector: row => row.jePotvrden ? "Accepted" : "Pending",
                                    cell: row => row.jePotvrden === true && <label title="Enrolled" className="isEnrolled" onClick={(e) => navigate("/profile/" + row.korisnik.korisnicko_ime)}><IoCheckmarkSharp /></label>,
                                    width: "150px",
                                    center: true,
                                },
                                {
                                    cell: row => row.jePotvrden === false ? <label title="Enroll" className="enroll" disabled={ preostaloMjesta<=0 || isDisabled || waiting } onClick={(e) => prihvatiUpis(row.upisTecajId)}><IoCheckmarkSharp /></label>
                                        : <label title="Disenroll" className="cancelEnroll" disabled={ isDisabled || waiting } onClick={(e) => odbijUpis(row.upisTecajId)}><IoCloseCircleSharp /></label> ,
                                    width: "75px",
                                    center: true,
                                },
                            ]}
                            data={upisi.filter(upis => clientFilter(upis.korisnik))}
                            defaultSortFieldId="potvrden"
                            onRowClicked={row => navigate("/profile/" + row.korisnik.korisnicko_ime)}
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
                                            backgroundColor: "purple",
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
    ) : <div className="error-naslov"> <h1 className="errorMsg">You are not the owner of this club or a trainer</h1> </div>
};

export default TecajEnrolled;