﻿// <auto-generated> This file has been auto generated by EF Core Power Tools. </auto-generated>
#nullable disable
using System;
using System.Collections.Generic;



namespace RPPP_WebApp.Models
{
    public partial class Autocesta
    {
        public Autocesta()
        {
            Dionica = new HashSet<Dionica>();
            NaplatnaPostaja = new HashSet<NaplatnaPostaja>();
        }

        public int Id { get; set; }
        public string Oznaka { get; set; }
        public string Naziv { get; set; }
        public int KoncesionarId { get; set; }

        //public int? IdPrethAutoceste { get; set; }

        public virtual Koncesionar Koncesionar { get; set; }
        public virtual ICollection<Dionica> Dionica { get; set; }
        public virtual ICollection<NaplatnaPostaja> NaplatnaPostaja { get; set; }
    }
}