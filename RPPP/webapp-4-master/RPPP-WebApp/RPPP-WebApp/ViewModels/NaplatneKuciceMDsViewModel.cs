using RPPP_WebApp.Models;
using System.Collections.Generic;

namespace RPPP_WebApp.ViewModels
{
    public class NaplatneKuciceMDsViewModel
    {
        public IEnumerable<NaplatneKuciceMDViewModel> naplatneKucice { get; set; }
        public PagingInfo PagingInfo { get; set; }
    }
}