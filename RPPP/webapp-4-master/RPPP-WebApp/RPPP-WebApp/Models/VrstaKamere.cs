﻿// <auto-generated> This file has been auto generated by EF Core Power Tools. </auto-generated>
#nullable disable
using System;
using System.Collections.Generic;

namespace RPPP_WebApp.Models
{
    public partial class VrstaKamere
    {
        public VrstaKamere()
        {
            Kamera = new HashSet<Kamera>();
        }

        public int Id { get; set; }
        public string Naziv { get; set; }

        public virtual ICollection<Kamera> Kamera { get; set; }
    }
}