using System;
using System.Text.Json.Serialization;

namespace RPPP_WebApp.ViewModels

{
    public class AutoCompleteVrstaNaplate
    {
        [JsonPropertyName("label")]
        public string Label { get; set; }
        [JsonPropertyName("id")]
        public int Id { get; set; }
        [JsonPropertyName("naziv")]
        public string Naziv { get; set; }
        [JsonPropertyName("vrstaId")]
        public int VrstaId { get; set; }
        public AutoCompleteVrstaNaplate() { }
        public AutoCompleteVrstaNaplate(int id, string label, string naziv, int vrstaId)
        {
            Id = id;
            Label = label;
            Naziv = naziv;
            VrstaId = vrstaId;
        }
    }
}
