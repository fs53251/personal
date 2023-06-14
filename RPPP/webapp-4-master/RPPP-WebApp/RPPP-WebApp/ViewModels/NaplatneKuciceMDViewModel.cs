using RPPP_WebApp.Models;
using System.Collections.Generic;

namespace RPPP_WebApp.ViewModels
{
    public class NaplatneKuciceMDViewModel
    {
        public int NaplatnaKucicaId { get; set; }
        public string ProlasciVozila = "";
        public string VrstaNaplate;
        public bool Otvorena { get; set; }
        public int VrstaNaplateId { get; set; }
        public int NaplatnaPostajaId { get; set;}
        public string KategorijaVozila { get; set; }

        public PagingInfo PagingInfo { get; set; }
        public NaplatnaKucica NaplatnaKucica;
        public List<ProlazakVozila> prolasciVozila { get; set; }
        public List<ProlazakVozilaTS> prolasciVozilaTS { get; set; }

        public NaplatneKuciceMDViewModel()
        {
            this.prolasciVozila = new List<ProlazakVozila>();
            this.prolasciVozilaTS = new List<ProlazakVozilaTS>();
        }
    }
}