﻿// <auto-generated> This file has been auto generated by EF Core Power Tools. </auto-generated>
#nullable disable
using System;
using System.Collections.Generic;

namespace RPPP_WebApp.Models
{
    public partial class PopratniSadrzaj
    {
        public int Id { get; set; }
        public string Naziv { get; set; }
        public TimeSpan RadnimDanomOd { get; set; }
        public TimeSpan RadninDanomDo { get; set; }
        public TimeSpan VikendimaDo { get; set; }
        public TimeSpan VikendimaOd { get; set; }
        public byte[] Slika { get; set; }
        public int OdmoristeId { get; set; }
        public int VrstaSadrzajaId { get; set; }

        public virtual Odmoriste Odmoriste { get; set; }
        public virtual VrstaPopratnog VrstaSadrzaja { get; set; }
    }
}