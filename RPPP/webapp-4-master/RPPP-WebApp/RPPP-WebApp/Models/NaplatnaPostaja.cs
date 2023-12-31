﻿// <auto-generated> This file has been auto generated by EF Core Power Tools. </auto-generated>
#nullable disable
using System;
using System.Collections.Generic;

namespace RPPP_WebApp.Models
{
    public partial class NaplatnaPostaja
    {
        public NaplatnaPostaja()
        {
            DionicaIzlaznaPostaja = new HashSet<Dionica>();
            DionicaUlaznaPostaja = new HashSet<Dionica>();
            NaplatnaKucica = new HashSet<NaplatnaKucica>();
        }

        public int Id { get; set; }
        public string Naziv { get; set; }
        public int AutocestaId { get; set; }
        public double KoordinataX { get; set; }
        public double KoordinataY { get; set; }
        public int GodinaOtvaranja { get; set; }

        public virtual Autocesta Autocesta { get; set; }
        public virtual ICollection<Dionica> DionicaIzlaznaPostaja { get; set; }
        public virtual ICollection<Dionica> DionicaUlaznaPostaja { get; set; }
        public virtual ICollection<NaplatnaKucica> NaplatnaKucica { get; set; }
    }
}