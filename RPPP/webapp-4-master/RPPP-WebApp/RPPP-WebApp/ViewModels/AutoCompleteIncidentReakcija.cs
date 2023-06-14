using System.Text.Json.Serialization;

namespace RPPP_WebApp.ViewModels {
    public class AutoCompleteIncidentReakcija {
        [JsonPropertyName("label")]
        public string Label { get; set; }

        [JsonPropertyName("id")]
        public int Id { get; set; }

        [JsonPropertyName("datum")]
        public DateTime? DatumReakcije { get; set; }

        [JsonPropertyName("opis")]
        public string Opis { get; set; }

        [JsonPropertyName("incident")]
        public int IncidentId { get; set; }

        [JsonPropertyName("vrstaReakcije")]
        public int? VrstaReakcijeId { get; set; }

        public AutoCompleteIncidentReakcija() { }
        public AutoCompleteIncidentReakcija(int id, string label, DateTime datum, string opis,
                                           int incidentId, int vrstaReakcijeId) {
            Id = id;
            Label = label;
            DatumReakcije = datum;
            Opis = opis;
            IncidentId = incidentId;
            VrstaReakcijeId = vrstaReakcijeId;
        }
    }
}
