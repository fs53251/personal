using RPPP_WebApp.Models;
using iTextSharp.text;
using iTextSharp.text.pdf;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using PdfRpt.ColumnsItemsTemplates;
using PdfRpt.Core.Contracts;
using PdfRpt.Core.Helper;
using PdfRpt.FluentInterface;
using System;
using System.Collections.Generic;
using Microsoft.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;
using System.IO;
using Microsoft.AspNetCore.Hosting;
using OfficeOpenXml;
using RPPP_WebApp.Extensions;
using System.Drawing;
using RPPP_WebApp.ModelsPartial;

namespace MVC.Controllers {
    public class SiktarReportController : Controller {
        private readonly RPPP04Context ctx;
        private readonly IWebHostEnvironment environment;
        private const string ExcelContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        public SiktarReportController(RPPP04Context ctx, IWebHostEnvironment environment) {
            this.ctx = ctx;
            this.environment = environment;
        }

        public IActionResult Index() {
            return View();
        }

        public async Task<IActionResult> Incidenti() {
            string naslov = "Popis incidenata";
            var incidenti = await ctx.Incident
                                      .Include(co => co.VrstaIncidenta)
                                      .Include(co => co.Dionica)
                                      .AsNoTracking()
                                      .OrderBy(co => co.Datum)
                                      .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer => {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header => {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader => {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(incidenti));

            report.MainTableColumns(columns => {
                columns.AddColumn(column => {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Incident>(co => co.Opis);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Opis", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Incident>(co => co.Datum);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(4);
                    column.Width(2);
                    column.HeaderCell("Datum", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Incident>(co => co.MeteoroloskiUvjeti);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(2);
                    column.Width(1);
                    column.HeaderCell("Meteorološki uvjeti", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Incident>(co => co.StanjeNaCesti);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(3);
                    column.Width(1);
                    column.HeaderCell("Stanje na cesti", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Incident>(co => co.Prohodnost);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(4);
                    column.Width(1);
                    column.HeaderCell("Prohodnost", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Incident>(co => co.Dionica.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(4);
                    column.Width(1);
                    column.HeaderCell("Dionica", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Incident>(co => co.VrstaIncidenta.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(4);
                    column.Width(1);
                    column.HeaderCell("Vrsta incidenta", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null) {
                Response.Headers.Add("content-disposition", "inline; filename=incidenti.pdf");
                return File(pdf, "application/pdf");
                //return File(pdf, "application/pdf", "drzave.pdf"); //Otvara save as dialog
            } else {
                return NotFound();
            }
        }

        public async Task<IActionResult> VrsteIncidenta() {
            string naslov = "Popis vrsta incidenata";
            var vrsteIncidenta = await ctx.VrstaIncidenta
                                    .AsNoTracking()
                                    .OrderBy(d => d.Naziv)
                                    .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer => {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header => {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader => {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(vrsteIncidenta));

            report.MainTableColumns(columns => {
                columns.AddColumn(column => {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column => {
                    column.PropertyName<VrstaIncidenta>(d => d.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Naziv vrste incidenta", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<VrstaIncidenta>(d => d.OpisPravilaPonasanja);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Opis pravila ponašanja", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null) {
                Response.Headers.Add("content-disposition", "inline; filename=vrsteIncidenata.pdf");
                return File(pdf, "application/pdf");
                //return File(pdf, "application/pdf", "drzave.pdf"); //Otvara save as dialog
            } else {
                return NotFound();
            }
        }

        public async Task<IActionResult> Reakcija() {
            string naslov = "Popis reakcija";
            var reakcije = await ctx.Reakcija
                                    .AsNoTracking()
                                    .Include(d => d.VrstaReakcije)
                                    .Include(d => d.Incident)
                                    .Include(d => d.Incident.VrstaIncidenta)
                                    .OrderBy(d => d.Datum)
                                    .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer => {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header => {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader => {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(reakcije));

            report.MainTableColumns(columns => {
                columns.AddColumn(column => {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Reakcija>(d => d.Opis);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Opis", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Reakcija>(oo => oo.Datum);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Datum", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Reakcija>(oo => oo.Incident.VrstaIncidenta.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(2);
                    column.Width(1);
                    column.HeaderCell("Incident", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<Reakcija>(oo => oo.VrstaReakcije.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(3);
                    column.Width(1);
                    column.HeaderCell("Vrsta reakcije", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null) {
                Response.Headers.Add("content-disposition", "inline; filename=vrsteReakcija.pdf");
                return File(pdf, "application/pdf");
                //return File(pdf, "application/pdf", "drzave.pdf"); //Otvara save as dialog
            } else {
                return NotFound();
            }
        }

        public async Task<IActionResult> VrsteReakcije() {
            string naslov = "Vrste reakcija";
            var vrste = await ctx.VrstaReakcije
                                    .AsNoTracking()
                                    .OrderBy(d => d.Naziv)
                                    .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer => {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header => {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader => {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(vrste));

            report.MainTableColumns(columns => {
                columns.AddColumn(column => {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column => {
                    column.PropertyName<VrstaReakcije>(vo => vo.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Naziv vrste reakcije", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<VrstaReakcije>(vo => vo.BrojTelefona);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(2);
                    column.Width(1);
                    column.HeaderCell("Broj telefona", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null) {
                Response.Headers.Add("content-disposition", "inline; filename=vrstaReakcije.pdf");
                return File(pdf, "application/pdf");
                //return File(pdf, "application/pdf", "drzave.pdf"); //Otvara save as dialog
            } else {
                return NotFound();
            }
        }

        #region Export u Excel
        public async Task<IActionResult> ExcelSimpleIncidenti() {
            var incidenti = await ctx.Incident
                                      .Include(co => co.VrstaIncidenta)
                                      .Include(co => co.Dionica)
                                      .AsNoTracking()
                                      .OrderBy(co => co.Datum)
                                      .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage()) {
                excel.Workbook.Properties.Title = "Popis incidenata";
                excel.Workbook.Properties.Author = "Filip incidenti";
                var worksheet = excel.Workbook.Worksheets.Add("Incidenti");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Opis";
                worksheet.Cells[1, 2].Value = "Datum";
                worksheet.Cells[1, 3].Value = "Meteorološki uvjeti";
                worksheet.Cells[1, 4].Value = "Stanje na cesti";
                worksheet.Cells[1, 5].Value = "Prohodnost";
                worksheet.Cells[1, 6].Value = "Dionica";
                worksheet.Cells[1, 7].Value = "Vrsta Incidenta";


                for (int i = 0; i < incidenti.Count; i++) {
                    worksheet.Cells[i + 2, 1].Value = incidenti[i].Opis;
                    worksheet.Cells[i + 2, 2].Value = incidenti[i].Datum;
                    worksheet.Cells[i + 2, 3].Value = incidenti[i].MeteoroloskiUvjeti;
                    worksheet.Cells[i + 2, 4].Value = incidenti[i].StanjeNaCesti;
                    worksheet.Cells[i + 2, 5].Value = incidenti[i].Prohodnost;
                    worksheet.Cells[i + 2, 6].Value = incidenti[i].Dionica.Naziv;
                    worksheet.Cells[i + 2, 7].Value = incidenti[i].VrstaIncidenta.Naziv;
                }

                worksheet.Cells[1, 1, incidenti.Count + 1, 7].AutoFitColumns();

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "incidenti.xlsx");
        }

        public async Task<IActionResult> ExcelSimpleVrsteIncidenata() {
            var vrsteIncidenta = await ctx.VrstaIncidenta
                                    .AsNoTracking()
                                    .OrderBy(d => d.Naziv)
                                    .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage()) {
                excel.Workbook.Properties.Title = "Popis vrsta incidenata";
                excel.Workbook.Properties.Author = "Filip vrste incidenata";
                var worksheet = excel.Workbook.Worksheets.Add("Vrste incidenata");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Naziv";
                worksheet.Cells[1, 2].Value = "Opis pravila ponašanja";

                for (int i = 0; i < vrsteIncidenta.Count; i++) {
                    worksheet.Cells[i + 2, 1].Value = vrsteIncidenta[i].Naziv;
                    worksheet.Cells[i + 2, 2].Value = vrsteIncidenta[i].OpisPravilaPonasanja;
                }

                worksheet.Cells[1, 1, vrsteIncidenta.Count + 1, 2].AutoFitColumns();

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "dionice.xlsx");
        }

        public async Task<IActionResult> ExcelSimpleReakcija() {
            var reakcije = await ctx.Reakcija
                                      .AsNoTracking()
                                      .Include(d => d.VrstaReakcije)
                                      .Include(d => d.Incident)
                                      .Include(d => d.Incident.VrstaIncidenta)
                                      .OrderBy(d => d.Datum)
                                      .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage()) {
                excel.Workbook.Properties.Title = "Popis reakcija";
                excel.Workbook.Properties.Author = "Filip";
                var worksheet = excel.Workbook.Worksheets.Add("Reakcije");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Opis";
                worksheet.Cells[1, 2].Value = "Datum";
                worksheet.Cells[1, 3].Value = "Incident";
                worksheet.Cells[1, 4].Value = "Vrsta Reakcije";


                for (int i = 0; i < reakcije.Count; i++) {
                    worksheet.Cells[i + 2, 1].Value = reakcije[i].Opis;
                    worksheet.Cells[i + 2, 2].Value = reakcije[i].Datum;
                    worksheet.Cells[i + 2, 3].Value = reakcije[i].Incident.VrstaIncidenta.Naziv;
                    worksheet.Cells[i + 2, 4].Value = reakcije[i].VrstaReakcije.Naziv;
                }

                worksheet.Cells[1, 1, reakcije.Count + 1, 4].AutoFitColumns();

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "odrzavanjaObjekata.xlsx");
        }

        public async Task<IActionResult> ExcelSimpleVrsteReakcija() {
            var vrste = await ctx.VrstaReakcije
                                   .AsNoTracking()
                                   .OrderBy(d => d.Naziv)
                                   .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage()) {
                excel.Workbook.Properties.Title = "Popis vrsta reakcija";
                excel.Workbook.Properties.Author = "Filip";
                var worksheet = excel.Workbook.Worksheets.Add("Vrste reakcija");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Naziv";
                worksheet.Cells[1, 2].Value = "Broj Telefona";
              

                for (int i = 0; i < vrste.Count; i++) {
                    worksheet.Cells[i + 2, 1].Value = vrste[i].Naziv;
                    worksheet.Cells[i + 2, 2].Value = vrste[i].BrojTelefona;
                }

                worksheet.Cells[1, 1, vrste.Count + 1, 2].AutoFitColumns();

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "vrsteReakcija.xlsx");
        }

        public async Task<IActionResult> ExcelComplexIncidenit() {
            var incidenti = await ctx.Incident
                                        .Include(a => a.VrstaIncidenta)
                                        .Include(a => a.Dionica)
                                        .ToListAsync();

            byte[] content;
            using (ExcelPackage excel = new ExcelPackage()) {
                excel.Workbook.Properties.Title = "MD incidenti";
                excel.Workbook.Properties.Author = "Filip";
                var worksheet = excel.Workbook.Worksheets.Add("MD incidenti");
                worksheet.Cells[2, 1].Value = "Incident";
                worksheet.Cells[4, 1].Value = "Reakcija";
                worksheet.Cells[2, 1].AutoFitColumns();
                worksheet.Cells[4, 1].AutoFitColumns();


                //First add the headers
                for (int i = 0; i < incidenti.Count; i++) {
                    worksheet.Cells[1, i * 12 + 2].Value = "Opis";
                    worksheet.Cells[1, i * 12 + 3].Value = "Datum";
                    worksheet.Cells[1, i * 12 + 4].Value = "Meteorološki uvjeti";
                    worksheet.Cells[1, i * 12 + 5].Value = "Stanje na cesti";
                    worksheet.Cells[1, i * 12 + 6].Value = "Prohodnost";
                    worksheet.Cells[1, i * 12 + 7].Value = "Dionica";
                    worksheet.Cells[1, i * 12 + 8].Value = "Vrsta Incidenta";
                    worksheet.Cells[2, i * 12 + 2].Value = incidenti[i].Opis;
                    worksheet.Cells[2, i * 12 + 3].Value = incidenti[i].Datum;
                    worksheet.Cells[2, i * 12 + 4].Value = incidenti[i].MeteoroloskiUvjeti;
                    worksheet.Cells[2, i * 12 + 5].Value = incidenti[i].StanjeNaCesti;
                    worksheet.Cells[2, i * 12 + 6].Value = incidenti[i].Prohodnost;
                    worksheet.Cells[2, i * 12 + 7].Value = incidenti[i].Dionica.Naziv;
                    worksheet.Cells[2, i * 12 + 8].Value = incidenti[i].VrstaIncidenta.Naziv;

                    var reakcije = await ctx.Reakcija.
                                    Where(d => d.IncidentId == incidenti[i].Id)
                                    .Include(d => d.Incident)
                                    .Include(d => d.VrstaReakcije)
                                    .ToListAsync();

                    worksheet.Cells[4, i * 12 + 2].Value = "Opis";
                    worksheet.Cells[4, i * 12 + 3].Value = "Datum";
                    worksheet.Cells[4, i * 12 + 4].Value = "Incident";
                    worksheet.Cells[4, i * 12 + 5].Value = "Vrsta Reakcije";
                    worksheet.Cells[4, i * 12 + 6].Value = " ";
                    worksheet.Cells[4, i * 12 + 2].AutoFitColumns();
                    worksheet.Cells[4, i * 12 + 3].AutoFitColumns();
                    worksheet.Cells[4, i * 12 + 4].AutoFitColumns();
                    worksheet.Cells[4, i * 12 + 5].AutoFitColumns();
                    worksheet.Cells[4, i * 12 + 6].AutoFitColumns();

                    for (int j = 0; j < reakcije.Count; j++) {
                        worksheet.Cells[j + 5, i * 12 + 2].Value = reakcije[j].Opis;
                        worksheet.Cells[j + 5, i * 12 + 3].Value = reakcije[j].Datum;
                        worksheet.Cells[j + 5, i * 12 + 4].Value = reakcije[j].Incident;
                        worksheet.Cells[j + 5, i * 12 + 5].Value = reakcije[j].VrstaReakcije.Naziv;

                        worksheet.Cells[j + 5, i * 12 + 2].AutoFitColumns();
                        worksheet.Cells[j + 5, i * 12 + 3].AutoFitColumns();
                        worksheet.Cells[j + 5, i * 12 + 4].AutoFitColumns();
                        worksheet.Cells[j + 5, i * 12 + 5].AutoFitColumns();
                    }
                }

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "FilipSiktarMDIncidenti.xlsx");
        }

        #endregion

        public async Task<IActionResult> MDIncidenti() {
            string naslov = "Filip MD Incidenti";
            var incidenti = await ctx.Incident
                                    .Include(a => a.VrstaIncidenta)
                                    .Include(a => a.Dionica)
                                    .Include(a => a.VrstaIncidenta)
                                    .ToListAsync();
            List<IncidentReakcija> incidentReakcije = new List<IncidentReakcija>();
            foreach (var a in incidenti) {
                List<Reakcija> reakcije = await ctx.Reakcija
                        .Where(d => d.IncidentId == a.Id)
                        .Include(d => d.VrstaReakcije)
                        .ToListAsync();
                foreach (var reakcija in reakcije) {
                    incidentReakcije.Add(new IncidentReakcija {
                        Id = a.Id,
                        Opis = a.Opis,
                        Datum = a.Datum,
                        MeteoroloskiUvjeti = a.MeteoroloskiUvjeti,
                        StanjeNaCesti = a.StanjeNaCesti,
                        Prohodnost = a.Prohodnost,
                        Dionica = a.Dionica.Naziv,
                        VrstaIncidenta = a.VrstaIncidenta.Naziv,
                        DatumReakcije = reakcija.Datum,
                        OpisReakcije = reakcija.Opis,
                        VrstaReakcije = reakcija.VrstaReakcije.Naziv
                    });
                }
            }
            incidentReakcije.OrderBy(ad => ad.Id).OrderBy(ad => ad.Datum);
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer => {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header => {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.CustomHeader(new MasterDetailsHeaders(naslov) {
                    PdfRptFont = header.PdfFont
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(incidentReakcije));

            report.MainTableColumns(columns => {
                #region Stupci po kojima se grupira
                columns.AddColumn(column => {
                    column.PropertyName<IncidentReakcija>(ad => ad.Id);
                    column.Group(
                        (val1, val2) => {
                            return (int)val1 == (int)val2;
                        });
                });
                #endregion

                columns.AddColumn(column => {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });
                columns.AddColumn(column => {
                    column.PropertyName<IncidentReakcija>(ad => ad.DatumReakcije);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Width(2);
                    column.HeaderCell("Datum reakcije", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<IncidentReakcija>(ad => ad.OpisReakcije);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Width(1);
                    column.HeaderCell("Opis reakcije", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column => {
                    column.PropertyName<IncidentReakcija>(ad => ad.VrstaReakcije);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Width(1);
                    column.HeaderCell("Vrsta reakcije", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null) {
                Response.Headers.Add("content-disposition", "inline; filename=MDIncidenti.pdf");
                return File(pdf, "application/pdf");
            } else
                return NotFound();
        }

        #region Master-detail header
        public class MasterDetailsHeaders : IPageHeader {
            private string naslov;
            public MasterDetailsHeaders(string naslov) {
                this.naslov = naslov;
            }
            public IPdfFont PdfRptFont { set; get; }

            public PdfGrid RenderingGroupHeader(Document pdfDoc, PdfWriter pdfWriter, IList<CellData> newGroupInfo, IList<SummaryCellData> summaryData) {
                var opis = newGroupInfo.GetSafeStringValueOf(nameof(IncidentReakcija.Opis));
                var datum = newGroupInfo.GetSafeStringValueOf(nameof(IncidentReakcija.Datum));
                var meteoroloskiUvjeti = newGroupInfo.GetValueOf(nameof(IncidentReakcija.MeteoroloskiUvjeti));
                var stanjeNaCesti = newGroupInfo.GetValueOf(nameof(IncidentReakcija.StanjeNaCesti));
                var prohodnost = newGroupInfo.GetValueOf(nameof(IncidentReakcija.Prohodnost));
                var dionica = newGroupInfo.GetValueOf(nameof(IncidentReakcija.Dionica));
                var vrstaIncidenta = newGroupInfo.GetValueOf(nameof(IncidentReakcija.VrstaIncidenta));

                var table = new PdfGrid(relativeWidths: new[] { 2f, 0.5f, 2f, 3f, 2f, 3f, 3f, 2f, 3f, 2f, 2f, 2f, 2f, 3f, 3f, 3f }) { WidthPercentage = 100 };

                table.AddSimpleRow(
                    (cellData, cellProperties) => {
                        cellData.Value = "Oznaka:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.TableRowData = newGroupInfo; //postavi podatke retka za ćeliju
                        var cellTemplate = new HyperlinkField(BaseColor.Black, false) {
                            TextPropertyName = nameof(AutocestaDionica.Oznaka),
                            BasicProperties = new CellBasicProperties {
                                HorizontalAlignment = HorizontalAlignment.Left,
                                PdfFontStyle = DocumentFontStyle.Bold,
                                PdfFont = PdfRptFont
                            }
                        };

                        cellData.CellTemplate = cellTemplate;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = "Opis:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = opis;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = "Datum:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = datum;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = "Meteorološki Uvjeti:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = meteoroloskiUvjeti;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = "Stanje na cesti:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = stanjeNaCesti;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = "Prohodnost:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = prohodnost;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = "Dionica";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = dionica;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = "Vrsta Incidenta:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) => {
                        cellData.Value = vrstaIncidenta;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    });

                return table.AddBorderToTable(borderColor: BaseColor.LightGray, spacingBefore: 5f);
            }

            public PdfGrid RenderingReportHeader(Document pdfDoc, PdfWriter pdfWriter, IList<SummaryCellData> summaryData) {
                var table = new PdfGrid(numColumns: 1) { WidthPercentage = 100 };
                table.AddSimpleRow(
                    (cellData, cellProperties) => {
                        cellData.Value = naslov;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Center;
                    });
                return table.AddBorderToTable();
            }
        }



        #endregion

        private PdfReport CreateReport(string naslov) {
            var pdf = new PdfReport();

            pdf.DocumentPreferences(doc => {
                doc.Orientation(PageOrientation.Portrait);
                doc.PageSize(PdfPageSize.A4);
                doc.DocumentMetadata(new DocumentMetadata {
                    Author = "Najjaca Grupa 04",
                    Application = "RPPP_WebApp.MVC Core",
                    Title = naslov
                });
                doc.Compression(new CompressionSettings {
                    EnableCompression = true,
                    EnableFullCompression = true
                });
            })
            //fix za linux https://github.com/VahidN/PdfReport.Core/issues/40
            .DefaultFonts(fonts => {
                fonts.Path(Path.Combine(environment.WebRootPath, "fonts", "verdana.ttf"),
                           Path.Combine(environment.WebRootPath, "fonts", "tahoma.ttf"));
                fonts.Size(7);
                fonts.Color(System.Drawing.Color.Black);
            })
            //
            .MainTableTemplate(template => {
                template.BasicTemplate(BasicTemplate.ProfessionalTemplate);
            })
            .MainTablePreferences(table => {
                table.ColumnsWidthsType(TableColumnWidthType.Relative);
                //table.NumberOfDataRowsPerPage(20);
                table.GroupsPreferences(new GroupsPreferences {
                    GroupType = GroupType.HideGroupingColumns,
                    RepeatHeaderRowPerGroup = true,
                    ShowOneGroupPerPage = true,
                    SpacingBeforeAllGroupsSummary = 5f,
                    NewGroupAvailableSpacingThreshold = 150,
                    SpacingAfterAllGroupsSummary = 5f
                });
                table.SpacingAfter(4f);
            });

            return pdf;
        }
    } 
}
