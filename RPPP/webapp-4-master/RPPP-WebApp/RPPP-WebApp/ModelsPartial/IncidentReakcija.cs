namespace RPPP_WebApp.ModelsPartial {
    public class IncidentReakcija {
        public int Id { get; set; }
        public string Opis { get; set; }
        public DateTime? Datum { get; set; }
        public string MeteoroloskiUvjeti { get; set; }
        public string StanjeNaCesti { get; set; }
        public string Prohodnost { get; set; }
        public string Dionica { get; set; }
        public string VrstaIncidenta { get; set; }

        public DateTime? DatumReakcije { get; set; }
        public string OpisReakcije { get; set; }
        public string VrstaReakcije { get; set; }
    }
}
