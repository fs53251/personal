import "./homePage.css"
import { useEffect, useState, useRef} from "react";
import { useCookies } from 'react-cookie';
import { refreshCookie } from "../../utils/validation/refreshCookie";
import { MapContainer, TileLayer, Marker } from 'react-leaflet'
import { getAllPotvrdeniKlubovi } from "../../utils/axios/backendCalls/clubEndpoints";
import L from "leaflet";
import { getAllPlesovi } from "../../utils/axios/backendCalls/plesEndpoints";
import { getAllPlesnjaci } from "../../utils/axios/backendCalls/plesnjakEndpoints";
import { getPlesnjaciPoPlesu } from "../../utils/axios/backendCalls/plesnjakEndpoints";
import activeHeader from "../../utils/activeHeader";
import { filterTecajByPles } from "../../utils/axios/backendCalls/tecajEndpoints";

const HomePage =() => {
    const mapRef = useRef();
    console.warn = () => {};
    const [filterClubs, setFilterClubs] = useState(true);

    const [cookies, setCookie] = useCookies(['user'])

    if(localStorage.getItem("clubsOrEvents")===null){
        localStorage.setItem("clubsOrEvents","Clubs");
    }
    function setFiltered(){
        if(localStorage.getItem("clubsOrEvents")==="Clubs"){
            setFilterClubs(true);
        }else{
            setFilterClubs(false);
        }
    }
    
    async function GetFiltered(){
        let filter = document.getElementById("filter-options");
        if(filter.value==="Events"){
            localStorage.setItem("clubsOrEvents","Events");
            setFilterClubs(false);
        }else{
            localStorage.setItem("clubsOrEvents","Clubs");
            setFilterClubs(true);
        }
    }
    if(localStorage.getItem("checked")===null){
        localStorage.setItem("checked",["all"]);
    }
    const [allDances, setAllDances] = useState([]);
    const [checkedD, setChecked] = useState(localStorage.getItem("checked").split(","));

    async function fetchAllDances() {
        try {
            const response = await getAllPlesovi();
            setAllDances(response.listaPlesova);
        }
        catch {
            console.log("Error fetching plesova");
        }
    }
    const [filteredDances, setFilteredDances] = useState([]);
    function refresh(){
        window.location.reload(true);
    }
    function filterPles(event){
        if(event.target.value==="all"){
            setChecked(["all"]);
            localStorage.setItem("checked",["all"]);
            setFilteredDances(allDances);
        }else{
            var newList=[];
            var newList2=[];
            if(checkedD.includes("all")){
                newList.push(event.target.value.toString());
                setChecked([event.target.value.toString()]);
                localStorage.setItem("checked",newList);
                setFilteredDances([event.target.value.toString()]);
            }else{
                for(var i=0;i<checkedD.length;i++){
                    newList.push(checkedD[i]);
                    newList2.push(filteredDances[i]);
                }
                if (newList.includes(event.target.value.toString())) {
                    newList.splice(newList.indexOf(event.target.value.toString()), 1);
                    newList2.splice(newList2.indexOf(event.target.value.toString()), 1);
                } else {
                    newList.push(event.target.value.toString());
                    newList2.push(event.target.value.toString());
                }
                setChecked(newList);
                localStorage.setItem("checked", newList);
                setFilteredDances(newList2);
            }
        }
        refresh();
    }

    const getLocClubs = async (item) => {
        const locClubs = new Object();
        const filtrirani = await filterTecajByPles(item.tipPlesaId);
        var clubList=[];
        
        filtrirani.listaTecajeva.map(async item2 => {
            
            const klubid = item2.klub.klubId;
            const tipPlesa = item2.tipPlesa.tipPlesaId;
            if (!clubList.includes(klubid)) {
                clubList.push(klubid);
                const location = item2.klub.dvorana.adresa;
                const redirect = '/club/' + item2.klub.vlasnik.korisnicko_ime + '/' + item2.klub.imeKluba.replaceAll(" ", "%20");
                if (locClubs[location]) {
                    locClubs[location] += "<br>" + '<a href=' + redirect + ">" + item2.klub.imeKluba + '</a>';
                    
                } else {
                    locClubs[location] = '<a href=' + redirect + ">" + item2.klub.imeKluba + '</a>';
                }
            }
        })
        return locClubs;
    }

    const getPlesoviPoKlubu = (item, responseLocClubs, map, plesoviPoKlubu) => {
        for (var key in responseLocClubs) {
            const location = key.split(" ");
            const lat = parseFloat(location[0]);
            const lng = parseFloat(location[1]);
            if (!plesoviPoKlubu[item.naziv]) {
                plesoviPoKlubu[item.naziv] = [];
            }
            var marker = L.marker([lat, lng]).bindPopup(responseLocClubs[key])
                .on('mouseover', function (e) {
                    this.openPopup();
                }).on('click', function (e) {
                    map.target.setView([lat, lng], 15);
                });
            plesoviPoKlubu[item.naziv].push(marker);
        }
        return plesoviPoKlubu;
    }
    
    useEffect(() => {
        setFiltered()
        fetchAllDances();
        activeHeader(document, "HOME PAGE");
        refreshCookie(cookies, setCookie);
    }, []);
    
    return (
        <div className="filter-home">
            <div className="home-map-div">
                {filterClubs &&
                <MapContainer ref={mapRef} className="homemap" center={[45.801245, 15.970876]} zoom={12} doubleClickZoom={false} scrollWheelZoom={true}
                    
                    whenReady={async (map) => {
                                
                                const dict=new Object();
                                const allKlubovi= await getAllPotvrdeniKlubovi();
                                
                                const plesoviPoKlubu= new Object();
                                allKlubovi.listaKlubova.map(item => {
                                    const dvorana=item.dvorana;
                                    if(dvorana && dvorana.adresa!=="null null"){
                                        let redirect = '/club/' + item.vlasnik.korisnicko_ime + '/' + item.imeKluba.replaceAll(" ", "%20");
                                        if(dict[dvorana.adresa]){
                                            dict[dvorana.adresa]+="<br>"+'<a href=' + redirect + ">" + item.imeKluba + '</a>';
                                            
                                        }else{
                                            dict[dvorana.adresa] = '<a href=' + redirect + ">" + item.imeKluba + '</a>';
                                        }
                                        
                                    }
                                })
                                plesoviPoKlubu["all"]=[];
                                for (var key in dict) {
                                    const location=key.split(" ");
                                    const lat=parseFloat(location[0]);
                                    const lng=parseFloat(location[1]);
                                    var marker=L.marker([lat, lng]).bindPopup(dict[key])
                                    .on('mouseover', function (e) {
                                        this.openPopup();
                                    }).on('click',function (e) {
                                        map.target.setView([lat,lng], 15);
                                    });
                                    //.on('mouseout', function (e) {
                                        //this.closePopup();
                                    //});
                                    plesoviPoKlubu["all"].push(marker);
                                }
                                
                                const dances= await getAllPlesovi();
                                dances.listaPlesova.map(async item => {
                                    const responseLocClubs = await getLocClubs(item);
                                    const responsePlesoviPoKlubu = await getPlesoviPoKlubu(item, responseLocClubs, map, plesoviPoKlubu);
                                    
                                    
                                    for (var key in responsePlesoviPoKlubu) {
                                        responsePlesoviPoKlubu[key].forEach(item => {
                                            item.removeFrom(map.target);
                                        })
                                    }
                                    if(checkedD.includes("all")){
                                        responsePlesoviPoKlubu["all"].forEach(item => {
                                            item.addTo(map.target);
                                        })
                                    }else{
                                        checkedD.forEach(item => {
                                            if(item.length!==0){
                                                if(responsePlesoviPoKlubu[item]){
                                                    responsePlesoviPoKlubu[item].forEach(item2 => {
                                                        item2.addTo(map.target);
                                                    })
                                                }
                                            }
                                        })
                                    }
                                })
                    }}>

                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                    
                    
                </MapContainer>
                }
                
                {
                !filterClubs &&
                <MapContainer ref={mapRef} className="homemap" center={[45.801245, 15.970876]} zoom={12} doubleClickZoom={false} scrollWheelZoom={true}
                whenReady={async (map) => {
                    const plesovi= new Object();
                    const dict=new Object();
                    const allEvents= await getAllPlesnjaci();
                    allEvents.listaPlesnjaka.map(item => {
                        const dvorana=item.dvorana;
                        if (dvorana && dvorana.adresa !== "null null") {
                            let redirect = "/club/event/" + item.plesnjakId;
                            if(dict[dvorana.adresa]){
                                dict[dvorana.adresa]+="<br>"+'<a href=' + redirect + '>' + item.naziv + '</a>';
                                
                            }else{
                                dict[dvorana.adresa] = '<a href=' + redirect + '>' + item.naziv + '</a>';
                            }
                            
                        }
                    })
                    
                    var newlist=[];
                    for (var key in dict) {
                        const location=key.split(" ");
                        const lat=parseFloat(location[0]);
                        const lng=parseFloat(location[1]);
                        var marker=L.marker([lat, lng]).addTo(map.target).bindPopup(dict[key])
                        .on('mouseover', function (e) {
                            this.openPopup();
                        }).on('click',function (e) {
                            map.target.setView([lat,lng], 15);
                        });
                        newlist.push(marker);
                    }
                    plesovi["all"]=newlist
                    plesovi["noviall"]=newlist
                    plesovi["prazni"]=[]
                    const dances= await getAllPlesovi();
                    const plesovi2= new Object();
                    dances.listaPlesova.map(async item => {
                        const dict2=new Object();
                        const filtrirani=await getPlesnjaciPoPlesu(item.naziv);
                        filtrirani.listaPlesnjaka.map(async item2 =>{
                            const dvorana=item2.dvorana;
                            if(dvorana && dvorana.adresa!=="null null"){
                                if(dict2[dvorana.adresa]){
                                    dict2[dvorana.adresa]+="<br>"+'<a href="/club/event/' + item2.plesnjakId + '">' + item2.naziv + '</a>';
                                    
                                }else{
                                    dict2[dvorana.adresa] = '<a href="/club/event/' + item2.plesnjakId + '">' + item2.naziv + '</a>';
                                }
                                
                            }
                        })
                        plesovi2[item.naziv]=[];
                        for (var key in dict2) {
                            const location=key.split(" ");
                            const lat=parseFloat(location[0]);
                            const lng=parseFloat(location[1]);
                            var marker=L.marker([lat, lng]).bindPopup(dict2[key])
                            .on('mouseover', function (e) {
                                this.openPopup();
                            }).on('click',function (e) {
                                map.target.setView([lat,lng], 15);
                            });
                            plesovi2[item.naziv].push(marker);
                        }
                        for (var key in plesovi2) {
                            plesovi[key]=plesovi2[key]
                        }
                        for (var key in plesovi) {
                            plesovi[key].forEach(item => {
                                item.removeFrom(map.target);
                            })
                        }
                        if(checkedD.includes("all")){
                            plesovi["all"].forEach(item => {
                                item.addTo(map.target);
                            })
                        }else{
                            checkedD?.forEach(item => {
                                plesovi[item]?.forEach(item2 => {
                                    item2.addTo(map.target);
                                })
                            })
                        }
                    })
                    
                }}>
                    <TileLayer
                        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                    />
                </MapContainer>
                
            }
            </div>
            <div className="filter">
                <div className="filter-select">
                    <div className="filterAndCheckbox">
                        <div className = "selectWrapper">
                            <select id="filter-options" onChange={GetFiltered} defaultValue={localStorage.getItem("clubsOrEvents")==="Clubs"?"Clubs":"Events"}>
                                <option value="Clubs">Clubs</option>
                                <option value="Events" >Events</option>
                            </select>
                        </div>
                        <div className="twoColumnCheckboxes">
                            <div className="checkboxes">
                                <h1 className="danceTypeSelect">{filterClubs === true ? "Course for:" : "Dance type:"}</h1>
                                <div className="ples">
                                    <label className="custom-checkbox">
                                        <input type="checkbox" name="tipPlesa" value="all" checked={checkedD.includes("all")} onChange={filterPles} />
                                            <label>All</label>

                                    </label>
                                </div>
                                {allDances.map(item => {
                                    return (
                                        <div className="ples" key={item.naziv}>
                                            <label className="custom-checkbox">
                                                <input type="checkbox" name={item.naziv} value={item.naziv} checked={checkedD.includes(item.naziv)} onChange={filterPles} />
                                                <label>{item.naziv}</label>

                                            </label>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default HomePage;