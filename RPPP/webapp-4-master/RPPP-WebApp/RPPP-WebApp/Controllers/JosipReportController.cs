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
using System.Security.Permissions;
using RPPP_WebApp.ViewModels;

namespace MVC.Controllers
{
    public class JosipReportController : Controller
    {
        private readonly RPPP04Context ctx;
        private readonly IWebHostEnvironment environment;
        private const string ExcelContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        public JosipReportController(RPPP04Context ctx, IWebHostEnvironment environment)
        {
            this.ctx = ctx;
            this.environment = environment;
        }

        public IActionResult Index()
        {
            return View();
        }

        public async Task<IActionResult> NaplatnaKucica()
        {
            string naslov = "Popis naplatnih kuæica";
            var naplatneKucice = await ctx.NaplatnaKucica
                                      .Include(nk => nk.NaplatnaPostaja)
                                      .Include(nk => nk.VrstaNaplate)
                                      .AsNoTracking()
                                      .OrderBy(nk => nk)
                                      .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer =>
            {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header =>
            {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader =>
                {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(naplatneKucice));

            report.MainTableColumns(columns =>
            {
                columns.AddColumn(column =>
                {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<NaplatnaKucica>(nk => nk.NaplatnaPostaja.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Naplatna postaja", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<NaplatnaKucica>(nk => nk.VrstaNaplate.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(4);
                    column.Width(2);
                    column.HeaderCell("Vrsta naplate", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<NaplatnaKucica>(nk => nk.Otvorena);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(2);
                    column.Width(1);
                    column.HeaderCell("Otvorena", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null)
            {
                Response.Headers.Add("content-disposition", "inline; filename=naplatneKucice.pdf");
                return File(pdf, "application/pdf");
            }
            else
            {
                return NotFound();
            }
        }

        public async Task<IActionResult> VrstaNaplate()
        {
            string naslov = "Popis vrsta naplate";
            var vrstaNaplate = await ctx.VrstaNaplate
                                    .AsNoTracking()
                                    .OrderBy(vn => vn.Naziv)
                                    .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer =>
            {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header =>
            {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader =>
                {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(vrstaNaplate));

            report.MainTableColumns(columns =>
            {
                columns.AddColumn(column =>
                {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<VrstaNaplate>(vn => vn.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Naziv", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null)
            {
                Response.Headers.Add("content-disposition", "inline; filename=vrstaNaplate.pdf");
                return File(pdf, "application/pdf");
                //return File(pdf, "application/pdf", "drzave.pdf"); //Otvara save as dialog
            }
            else
            {
                return NotFound();
            }
        }

        public async Task<IActionResult> ProlazakVozila()
        {
            string naslov = "Popis prolazaka vozila";
            var prolasciVozila = await ctx.ProlazakVozila
                                    .AsNoTracking()
                                    .Include(pv => pv.KategorijaVozila)
                                    .Include(pv => pv.NaplatnaKucica)
                                    .OrderBy(d => d.Id)
                                    .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer =>
            {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header =>
            {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader =>
                {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(prolasciVozila));

            report.MainTableColumns(columns =>
            {
                columns.AddColumn(column =>
                {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<ProlazakVozila>(pv => pv.RegistracijskaOznaka);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Registracijska oznaka", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<ProlazakVozila>(pv => pv.KategorijaVozila.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Kategorija vozila", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<ProlazakVozila>(pv => pv.VrijemeProlaska);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(2);
                    column.Width(1);
                    column.HeaderCell("Vrijeme prolaska", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<ProlazakVozila>(pv => pv.NaplatnaKucicaId);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(3);
                    column.Width(1);
                    column.HeaderCell("Naplatna kucica", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null)
            {
                Response.Headers.Add("content-disposition", "inline; filename=odrzavanjeObjekata.pdf");
                return File(pdf, "application/pdf");
                //return File(pdf, "application/pdf", "drzave.pdf"); //Otvara save as dialog
            }
            else
            {
                return NotFound();
            }
        }

        public async Task<IActionResult> KategorijaVozila()
        {
            string naslov = "Kategorije vozila";
            var kategorije = await ctx.KategorijaVozila
                                    .AsNoTracking()
                                    .OrderBy(kv => kv.Naziv)
                                    .ToListAsync();
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer =>
            {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header =>
            {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.DefaultHeader(defaultHeader =>
                {
                    defaultHeader.RunDirection(PdfRunDirection.LeftToRight);
                    defaultHeader.Message(naslov);
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(kategorije));

            report.MainTableColumns(columns =>
            {
                columns.AddColumn(column =>
                {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Order(0);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<KategorijaVozila>(kv => kv.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Order(1);
                    column.Width(2);
                    column.HeaderCell("Kategorija vozila", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null)
            {
                Response.Headers.Add("content-disposition", "inline; filename=vrsteOdrzavanja.pdf");
                return File(pdf, "application/pdf");
                //return File(pdf, "application/pdf", "drzave.pdf"); //Otvara save as dialog
            }
            else
            {
                return NotFound();
            }
        }

        private PdfReport CreateReport(string naslov)
        {
            var pdf = new PdfReport();

            pdf.DocumentPreferences(doc =>
            {
                doc.Orientation(PageOrientation.Portrait);
                doc.PageSize(PdfPageSize.A4);
                doc.DocumentMetadata(new DocumentMetadata
                {
                    Author = "Najjaca Grupa 04",
                    Application = "RPPP_WebApp.MVC Core",
                    Title = naslov
                });
                doc.Compression(new CompressionSettings
                {
                    EnableCompression = true,
                    EnableFullCompression = true
                });
            })
            .DefaultFonts(fonts =>
            {
                fonts.Path(Path.Combine(environment.WebRootPath, "fonts", "verdana.ttf"),
                           Path.Combine(environment.WebRootPath, "fonts", "tahoma.ttf"));
                fonts.Size(7);
                fonts.Color(System.Drawing.Color.Black);
            })
            //
            .MainTableTemplate(template =>
            {
                template.BasicTemplate(BasicTemplate.ProfessionalTemplate);
            })
            .MainTablePreferences(table =>
            {
                table.ColumnsWidthsType(TableColumnWidthType.Relative);
                table.GroupsPreferences(new GroupsPreferences
                {
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

        public async Task<IActionResult> MDNaplatneKucice()
        {
            string naslov = "Josip MD naplatne kucice";
            var naplatneKucice = await ctx.NaplatnaKucica.Include(nk => nk.VrstaNaplate).Include(nk => nk.NaplatnaPostaja).ToListAsync();
            List<NaplatnaKucicaMD> kucicaMD = new List<NaplatnaKucicaMD>();
            foreach (var nk in naplatneKucice)
            {
                List<ProlazakVozila> prolasci = await ctx.ProlazakVozila.Where(pv => pv.NaplatnaKucicaId == nk.Id).Include(pv => pv.KategorijaVozila).ToListAsync();
                Boolean zeroAdded = true;
                foreach (var prolazak in prolasci)
                {
                    NaplatnaKucicaMD newMD = new NaplatnaKucicaMD();

                    newMD.NaplatnaKucica = nk;
                    newMD.ProlazakVozila = prolazak;

                    zeroAdded = false;

                    kucicaMD.Add(newMD);
                }

                if(zeroAdded)
                {
                    NaplatnaKucicaMD newMD = new NaplatnaKucicaMD();
                    newMD.NaplatnaKucica = nk;
                    ProlazakVozila pk = new ProlazakVozila();
                    pk.KategorijaVozila = new KategorijaVozila();
                    newMD.ProlazakVozila = pk;
                    kucicaMD.Add(newMD);
                }
            }
            kucicaMD.OrderBy(km => km.NaplatnaKucica.Id).OrderBy(km => km.NaplatnaKucica.NaplatnaPostaja.Naziv);
            PdfReport report = CreateReport(naslov);
            #region Podnožje i zaglavlje
            report.PagesFooter(footer =>
            {
                footer.DefaultFooter(DateTime.Now.ToString("dd.MM.yyyy."));
            })
            .PagesHeader(header =>
            {
                header.CacheHeader(cache: true); // It's a default setting to improve the performance.
                header.CustomHeader(new MasterDetailsHeaders(naslov)
                {
                    PdfRptFont = header.PdfFont
                });
            });
            #endregion
            #region Postavljanje izvora podataka i stupaca
            report.MainTableDataSource(dataSource => dataSource.StronglyTypedList(kucicaMD));

            report.MainTableColumns(columns =>
            {
                #region Stupci po kojima se grupira
                columns.AddColumn(column =>
                {
                    column.PropertyName<NaplatnaKucicaMD>(km => km.NaplatnaKucica.Id);
                    column.Group(
                        (val1, val2) =>
                        {
                            return (int)val1 == (int)val2;
                        });
                });
                #endregion

                columns.AddColumn(column =>
                {
                    column.IsRowNumber(true);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Width(1);
                    column.HeaderCell("#", horizontalAlignment: HorizontalAlignment.Right);
                });
                columns.AddColumn(column =>
                {
                    column.PropertyName<NaplatnaKucicaMD>(km => km.ProlazakVozila.RegistracijskaOznaka);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Center);
                    column.IsVisible(true);
                    column.Width(2);
                    column.HeaderCell("Registracijska oznaka", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<NaplatnaKucicaMD>(km => km.ProlazakVozila.KategorijaVozila.Naziv);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Width(1);
                    column.HeaderCell("Kategorija vozila", horizontalAlignment: HorizontalAlignment.Center);
                });

                columns.AddColumn(column =>
                {
                    column.PropertyName<NaplatnaKucicaMD>(km => km.ProlazakVozila.VrijemeProlaska);
                    column.CellsHorizontalAlignment(HorizontalAlignment.Right);
                    column.IsVisible(true);
                    column.Width(1);
                    column.HeaderCell("Vrijeme prolaska", horizontalAlignment: HorizontalAlignment.Center);
                });
            });

            #endregion
            byte[] pdf = report.GenerateAsByteArray();

            if (pdf != null)
            {
                Response.Headers.Add("content-disposition", "inline; filename=MDCestovniObjekti.pdf");
                return File(pdf, "application/pdf");
            }
            else
                return NotFound();
        }

        #region Export u Excel
        public async Task<IActionResult> ExcelSimpleNaplatnaKucica()
        {
            var naplatnaKucica = await ctx.NaplatnaKucica
                                  .Include(nk => nk.NaplatnaPostaja)
                                  .Include(nk => nk.VrstaNaplate)
                                  .AsNoTracking()
                                  .OrderBy(nk => nk.Id)
                                  .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage())
            {
                excel.Workbook.Properties.Title = "Popis naplatnih kucica";
                excel.Workbook.Properties.Author = "Josip naplatne kucice";
                var worksheet = excel.Workbook.Worksheets.Add("Naplatne kucice");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Id";
                worksheet.Cells[1, 2].Value = "Vrsta naplate";
                worksheet.Cells[1, 3].Value = "Naplatna postaja";
                worksheet.Cells[1, 4].Value = "Otvorena";

                for (int i = 0; i < naplatnaKucica.Count; i++)
                {
                    worksheet.Cells[i + 2, 1].Value = naplatnaKucica[i].Id;
                    worksheet.Cells[i + 2, 2].Value = naplatnaKucica[i].VrstaNaplate.Naziv;
                    worksheet.Cells[i + 2, 3].Value = naplatnaKucica[i].NaplatnaPostaja.Naziv;
                    worksheet.Cells[i + 2, 4].Value = naplatnaKucica[i].Otvorena;
                }

                worksheet.Cells[1, 1, naplatnaKucica.Count + 1, 11].AutoFitColumns();

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "naplatneKucice.xlsx");
        }

        public async Task<IActionResult> ExcelSimpleVrsteNaplate()
        {
            var vrsteNaplate = await ctx.VrstaNaplate
                                    .AsNoTracking()
                                    .OrderBy(vn => vn.Naziv)
                                    .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage())
            {
                excel.Workbook.Properties.Title = "Popis vrsta naplate";
                excel.Workbook.Properties.Author = "Josip vrste naplate";
                var worksheet = excel.Workbook.Worksheets.Add("Vrste naplate");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Id";
                worksheet.Cells[1, 2].Value = "Naziv";

                for (int i = 0; i < vrsteNaplate.Count; i++)
                {
                    worksheet.Cells[i + 2, 1].Value = vrsteNaplate[i].Id;
                    worksheet.Cells[i + 2, 2].Value = vrsteNaplate[i].Naziv;
                }

                worksheet.Cells[1, 1, vrsteNaplate.Count + 1, 9].AutoFitColumns();

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "vrsteNaplate.xlsx");
        }

        public async Task<IActionResult> ExcelSimpleProlazakVozila()
        {
            var prolasci = await ctx.ProlazakVozila
                                    .Include(pv => pv.KategorijaVozila)
                                    .AsNoTracking()
                                    .OrderBy(pv => pv.RegistracijskaOznaka)
                                    .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage())
            {
                excel.Workbook.Properties.Title = "Popis prolazaka vozila";
                excel.Workbook.Properties.Author = "Josip";
                var worksheet = excel.Workbook.Worksheets.Add("Prolazak vozila");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Id";
                worksheet.Cells[1, 2].Value = "Registracijska oznaka";
                worksheet.Cells[1, 3].Value = "Vrijeme prolaska";
                worksheet.Cells[1, 4].Value = "Kategorija vozila";
                worksheet.Cells[1, 5].Value = "Naplatna kucica";

                for (int i = 0; i < prolasci.Count; i++)
                {
                    worksheet.Cells[i + 2, 1].Value = prolasci[i].Id;
                    worksheet.Cells[i + 2, 2].Value = prolasci[i].RegistracijskaOznaka;
                    worksheet.Cells[i + 2, 3].Value = prolasci[i].VrijemeProlaska.ToString();
                    worksheet.Cells[i + 2, 4].Value = prolasci[i].KategorijaVozila.Naziv;
                    worksheet.Cells[i + 2, 5].Value = prolasci[i].NaplatnaKucicaId;

                    worksheet.Cells[1, 1, prolasci.Count + 1, 9].AutoFitColumns();

                }
                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "prolasciVozila.xlsx");
        }

        public async Task<IActionResult> ExcelSimpleKategorijaVozila()
        {
            var kategorije = await ctx.KategorijaVozila
                                    .AsNoTracking()
                                    .OrderBy(kv => kv.Naziv)
                                    .ToListAsync();
            byte[] content;
            using (ExcelPackage excel = new ExcelPackage())
            {
                excel.Workbook.Properties.Title = "Popis kategorija vozila";
                excel.Workbook.Properties.Author = "Josip";
                var worksheet = excel.Workbook.Worksheets.Add("Kategorije vozila");

                //First add the headers
                worksheet.Cells[1, 1].Value = "Id";
                worksheet.Cells[1, 2].Value = "Naziv";

                for (int i = 0; i < kategorije.Count; i++)
                {
                    worksheet.Cells[i + 2, 1].Value = kategorije[i].Id;
                    worksheet.Cells[i + 2, 2].Value = kategorije[i].Naziv;
                }

                worksheet.Cells[1, 1, kategorije.Count + 1, 5].AutoFitColumns();

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "kategorijeVozila.xlsx");
        }

        public async Task<IActionResult> ExcelComplexNaplatnaKucica()
        {
            var naplatnaKucica = await ctx.NaplatnaKucica.Include(nk => nk.VrstaNaplate).Include(nk => nk.NaplatnaPostaja).ToListAsync();

            byte[] content;
            using (ExcelPackage excel = new ExcelPackage())
            {
                excel.Workbook.Properties.Title = "MD naplatne kucice";
                excel.Workbook.Properties.Author = "Josip";
                var worksheet = excel.Workbook.Worksheets.Add("MD naplatne kucice");
                worksheet.Cells[2, 1].Value = "Naplatna kucica";
                worksheet.Cells[4, 1].Value = "Prolasci vozila";
                worksheet.Cells[2, 1].AutoFitColumns();
                worksheet.Cells[4, 1].AutoFitColumns();


                //First add the headers
                for (int i = 0; i < naplatnaKucica.Count; i++)
                {
                    worksheet.Cells[1, i * 11 + 2].Value = "Id";
                    worksheet.Cells[1, i * 11 + 3].Value = "Naplatna postaja";
                    worksheet.Cells[1, i * 11 + 4].Value = "Vrsta naplate";
                    worksheet.Cells[1, i * 11 + 5].Value = "Otvorena";
                    worksheet.Cells[1, i * 11 + 6].Value = "|";
                    worksheet.Cells[2, i * 11 + 2].Value = naplatnaKucica[i].Id;
                    worksheet.Cells[2, i * 11 + 3].Value = naplatnaKucica[i].NaplatnaPostaja.Naziv;
                    worksheet.Cells[2, i * 11 + 4].Value = naplatnaKucica[i].VrstaNaplate.Naziv;
                    worksheet.Cells[2, i * 11 + 5].Value = naplatnaKucica[i].Otvorena;
                    worksheet.Cells[2, i * 11 + 6].Value = "|";

                    var prolasci = await ctx.ProlazakVozila.Where(pv => pv.NaplatnaKucicaId == naplatnaKucica[i].Id).Include(pv => pv.KategorijaVozila).ToListAsync();
                    worksheet.Cells[4, i * 11 + 2].Value = "Registracijska oznaka";
                    worksheet.Cells[4, i * 11 + 3].Value = "Kategorija vozila";
                    worksheet.Cells[4, i * 11 + 4].Value = "Vrijeme prolaska";
                    worksheet.Cells[4, i * 11 + 2].AutoFitColumns();
                    worksheet.Cells[4, i * 11 + 3].AutoFitColumns();
                    worksheet.Cells[4, i * 11 + 4].AutoFitColumns();
                    for (int j = 0; j < prolasci.Count; j++)
                    {
                        worksheet.Cells[j + 5, i * 11 + 2].Value = prolasci[j].RegistracijskaOznaka;
                        worksheet.Cells[j + 5, i * 11 + 3].Value = prolasci[j].KategorijaVozila.Naziv;
                        worksheet.Cells[j + 5, i * 11 + 4].Value = prolasci[j].VrijemeProlaska.ToString();
                        worksheet.Cells[j + 5, i * 11 + 2].AutoFitColumns();
                        worksheet.Cells[j + 5, i * 11 + 3].AutoFitColumns();
                        worksheet.Cells[j + 5, i * 11 + 4].AutoFitColumns();
                    }
                }

                content = excel.GetAsByteArray();
            }
            return File(content, ExcelContentType, "NaplatneKucice.xlsx");
        }

        #endregion

        #region Master-detail header
        public class MasterDetailsHeaders : IPageHeader
        {
            private string naslov;
            public MasterDetailsHeaders(string naslov)
            {
                this.naslov = naslov;
            }
            public IPdfFont PdfRptFont { set; get; }

            public PdfGrid RenderingGroupHeader(Document pdfDoc, PdfWriter pdfWriter, IList<CellData> newGroupInfo, IList<SummaryCellData> summaryData)
            {
                var naplatnaKucica = (NaplatnaKucica) newGroupInfo.GetValueOf(nameof(NaplatnaKucicaMD.NaplatnaKucica));


                var table = new PdfGrid(relativeWidths: new[] { 2f, 4f, 2f, 4f }) { WidthPercentage = 100 };

                table.AddSimpleRow(
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = "Id:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = naplatnaKucica.Id;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = "Naplatna postaja:";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = naplatnaKucica.NaplatnaPostaja.Naziv;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    });

                table.AddSimpleRow(
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = "Vrsta placanja";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = naplatnaKucica.VrstaNaplate.Naziv;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = "Otvorena";
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    },
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = naplatnaKucica.Otvorena;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Left;
                    });
                return table.AddBorderToTable(borderColor: BaseColor.LightGray, spacingBefore: 5f);
            }

            public PdfGrid RenderingReportHeader(Document pdfDoc, PdfWriter pdfWriter, IList<SummaryCellData> summaryData)
            {
                var table = new PdfGrid(numColumns: 1) { WidthPercentage = 100 };
                table.AddSimpleRow(
                    (cellData, cellProperties) =>
                    {
                        cellData.Value = naslov;
                        cellProperties.PdfFont = PdfRptFont;
                        cellProperties.PdfFontStyle = DocumentFontStyle.Bold;
                        cellProperties.HorizontalAlignment = HorizontalAlignment.Center;
                    });
                return table.AddBorderToTable();
            }
        }
    }
        #endregion
}
