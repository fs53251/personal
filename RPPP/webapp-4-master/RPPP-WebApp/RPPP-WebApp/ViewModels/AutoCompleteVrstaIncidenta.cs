using System.Text.Json.Serialization;

namespace RPPP_WebApp.ViewModels {
    public class AutoCompleteVrstaIncidenta {
        [JsonPropertyName("label")]
        public string Label { get; set; }
        [JsonPropertyName("id")]
        public int Id { get; set; }

        public AutoCompleteVrstaIncidenta() { }
        public AutoCompleteVrstaIncidenta(int id, string label) {
            Id = id;
            Label = label;
        }
    }
}
