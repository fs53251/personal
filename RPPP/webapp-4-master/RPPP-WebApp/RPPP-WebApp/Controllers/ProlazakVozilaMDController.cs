using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Options;
using RPPP_WebApp.Extensions;
using RPPP_WebApp.Extensions.Selectors;
using RPPP_WebApp.Models;
using RPPP_WebApp.ViewModels;
using System;
using System.Text.Json;
using System.Threading.Tasks;

namespace RPPP_WebApp.Controllers
{
    public class ProlazakVozilaMDController : Controller
    {
        private readonly RPPP04Context ctx;
        private readonly ILogger<ProlazakVozilaMDController> logger;
        private readonly AppSettings appData;

        public ProlazakVozilaMDController(RPPP04Context ctx, IOptionsSnapshot<AppSettings> options, ILogger<ProlazakVozilaMDController> logger)
        {
            appData = options.Value;
            this.ctx = ctx;
            this.logger = logger;
        }

        public async Task<IActionResult> Index(int page = 1, int sort = 1, bool ascending = true)
        {

            var query = ctx.NaplatnaKucica.AsQueryable();
            int count = await query.CountAsync();

            int pagesize = appData.PageSize;
            var pagingInfo = new PagingInfo
            {
                CurrentPage = page,
                Sort = sort,
                Ascending = ascending,
                ItemsPerPage = pagesize,
                TotalItems = count
            };


            if (page < 1 || page > pagingInfo.TotalPages)
            {
                return RedirectToAction(nameof(Index), new { page = 1, sort = sort, ascending = ascending });
            }

            query = query.ApplySort(sort, ascending);

            var naplatneKucice = await query
                                      .Select(nk => new NaplatneKuciceMDViewModel
                                      {
                                          NaplatnaKucicaId = nk.Id,
                                          VrstaNaplate = nk.VrstaNaplate.Naziv
                                      })
                                      .Skip((page - 1) * pagesize)
                                      .Take(pagesize)
                                      .ToListAsync();

            foreach (var nk in naplatneKucice)
            {
                var listaProlazaka = await ctx.ProlazakVozila
                                      .Where(pv => pv.NaplatnaKucicaId == nk.NaplatnaKucicaId)
                                      .Select(pv => pv.RegistracijskaOznaka)
                                      .ToListAsync();
                string nazivi = string.Join(", ", listaProlazaka);

                nk.ProlasciVozila = nazivi;
            }

            var model = new NaplatneKuciceMDsViewModel
            {
                naplatneKucice = naplatneKucice,
                PagingInfo = pagingInfo
            };

            return View(model);
        }

        public async Task<IActionResult> Show(int id, string filter, int page = 1, int sort = 1, bool ascending = true, string viewName = nameof(Show))
        {
            NaplatneKuciceMDViewModel naplatnaKucica = await ctx.NaplatnaKucica
                                    .Where(nk => nk.Id == id)
                                    .Include(nk => nk.NaplatnaPostaja)
                                    .Include(nk => nk.VrstaNaplate)
                                    .Select(nk => new NaplatneKuciceMDViewModel
                                    {
                                        NaplatnaKucicaId = nk.Id,
                                        VrstaNaplate = nk.VrstaNaplate.Naziv,
                                        NaplatnaKucica = nk
                                    })
                                    .FirstOrDefaultAsync();

            var pagingInfo = new PagingInfo
            {
                CurrentPage = page,
                Sort = sort,
                Ascending = ascending
            };

            if (naplatnaKucica != null)
            {
                List<ProlazakVozila> prolasciVozila = await ctx.ProlazakVozila
                                      .Where(pv => pv.NaplatnaKucicaId == naplatnaKucica.NaplatnaKucicaId)
                                      .Include(pv => pv.KategorijaVozila)
                                      .OrderBy(d => d.Id)
                                      .ToListAsync();

                naplatnaKucica.prolasciVozila = prolasciVozila;

                naplatnaKucica.PagingInfo = pagingInfo;

                ViewBag.Page = page;
                ViewBag.Sort = sort;
                ViewBag.Ascending = ascending;
                ViewBag.Filter = filter;

                return View(viewName, naplatnaKucica);
            }
            else
            {
                return NotFound($"Naplatna kucica s id = {id} ne postoji");
            }
        }

        [HttpGet]
        public async Task<IActionResult> Create()
        {
            await PrepareDropdownLists();
            var naplatneKuciceMDViewModel = new NaplatneKuciceMDViewModel();
            return View(naplatneKuciceMDViewModel);
        }


        [HttpPost]
        public async Task<IActionResult> Create(NaplatneKuciceMDViewModel masterDetailViewModel)
        {
            if (ModelState.IsValid)
            {

                Console.WriteLine(masterDetailViewModel.prolasciVozilaTS.Count);
                NaplatnaKucica naplatnaKucica = new NaplatnaKucica();
                CopyValuesMaster(naplatnaKucica, masterDetailViewModel);

                var prolasciVozila = new List<ProlazakVozila>();

                foreach(var pvTS in masterDetailViewModel.prolasciVozilaTS)
                {
                    prolasciVozila.Add(pvTS.ConvertToProlazakVozila());
                }

                foreach (var prolazakVozila in prolasciVozila)
                {
                    if (prolazakVozila == null)
                        continue;
                    ProlazakVozila newProlazakVozila = new ProlazakVozila();
                    CopyValuesDetail(newProlazakVozila, prolazakVozila);
                    naplatnaKucica.ProlazakVozila.Add(prolazakVozila);
                }

                try
                {
                    ctx.Add(naplatnaKucica);
                    await ctx.SaveChangesAsync();
                    logger.LogInformation(new EventId(1000), $"MD naplatna kucica {naplatnaKucica.Id} dodana.");
                    TempData[Constants.Message] = $"MD naplatna kucica uspješno dodana. Id={naplatnaKucica.Id}";
                    TempData[Constants.ErrorOccurred] = false;
                    return RedirectToAction(nameof(Index), new { id = naplatnaKucica.Id });

                }
                catch (Exception exc)
                {
                    logger.LogError("Pogreška prilikom dodavanje nove naplatne kucice: {0}", exc.CompleteExceptionMessage() + " " + naplatnaKucica.NaplatnaPostajaId + naplatnaKucica.Otvorena + naplatnaKucica.VrstaNaplateId);
                    ModelState.AddModelError(string.Empty, exc.CompleteExceptionMessage());
                    return View(masterDetailViewModel);
                }
            }
            else
            {
                foreach(var error in ModelState.Values.SelectMany(v => v.Errors))
                {
                    Console.WriteLine(error.ErrorMessage);
                }
                return View(masterDetailViewModel);
            }
        }

        [HttpDelete]
        public async Task<IActionResult> Delete(int id)
        {
            var naplatnaKucica = await ctx.NaplatnaKucica
                   .Include(nk => nk.ProlazakVozila).Where(nk => nk.Id == id).SingleOrDefaultAsync();
            ActionResponseMessage responseMessage;
            if (naplatnaKucica != null)
            {
                try
                {
                    string naziv = naplatnaKucica.Id.ToString();

                    foreach(var prolazakVozila in naplatnaKucica.ProlazakVozila)
                    {
                        if (prolazakVozila == null)
                            continue;
                        ctx.Remove(prolazakVozila);
                        await ctx.SaveChangesAsync();
                    }

                    ctx.Remove(naplatnaKucica);
                    await ctx.SaveChangesAsync();
                    logger.LogInformation($"Naplatna kucica: {naziv} uspješno obrisana");
                    TempData[Constants.Message] = $"Naplatna kucica: {naziv} uspješno obrisana";
                    TempData[Constants.ErrorOccurred] = false;
                    responseMessage = new ActionResponseMessage(MessageType.Success, $"Naplatna kucica {naziv} sa šifrom {id} uspješno obrisana.");
                }
                catch (Exception exc)
                {
                    logger.LogError("Pogreška prilikom brisanja naplatne kucice: " + exc.CompleteExceptionMessage());
                    TempData[Constants.Message] = "Pogreška prilikom brisanja naplatne kucice: " + exc.CompleteExceptionMessage();
                    TempData[Constants.ErrorOccurred] = true;
                    responseMessage = new ActionResponseMessage(MessageType.Error, $"Pogreška prilikom brisanja naplatne kucice: {exc.CompleteExceptionMessage()}");

                }
            }
            else
            {
                responseMessage = new ActionResponseMessage(MessageType.Error, $"Naplatna kucica sa šifrom {id} ne postoji");
            }

            Response.Headers["HX-Trigger"] = JsonSerializer.Serialize(new { showMessage = responseMessage });
            return responseMessage.MessageType == MessageType.Success ? new EmptyResult() : new EmptyResult();
        }

        [HttpGet]
        public async Task<IActionResult> Edit(int id, int page = 1, int sort = 1, bool ascending = true)
        {
            var model = new NaplatneKuciceMDViewModel();

            var naplatnaKucica = await ctx.NaplatnaKucica.FindAsync(id);
            if (naplatnaKucica != null)
            {
                model.NaplatnaKucica = naplatnaKucica;
                model.NaplatnaKucicaId = naplatnaKucica.Id;
                model.Otvorena = naplatnaKucica.Otvorena;
                model.NaplatnaPostajaId = naplatnaKucica.NaplatnaPostajaId;
                model.VrstaNaplateId = naplatnaKucica.VrstaNaplateId;

                var prolasciVozila = ctx.ProlazakVozila.Include(pv => pv.KategorijaVozila).Where(pv => pv.NaplatnaKucicaId == naplatnaKucica.Id).ToList();
                model.prolasciVozila = prolasciVozila;
                model.prolasciVozilaTS = ProlazakVozila.ConvertToProlazakVozilaTS(prolasciVozila);

                await PrepareDropdownLists();
                ViewBag.Page = page;
                ViewBag.Sort = sort;
                ViewBag.Ascending = ascending;
                return View(model);
            }
            else
            {
                return NotFound($"Neispravan id naplatne kucice: {id}");
            }
        }

        [HttpPost]
        public async Task<IActionResult> Edit(NaplatneKuciceMDViewModel masterDetailViewModel, int id, int? position, string filter, int page = 1, int sort = 1, bool ascending = true)
        {
            ViewBag.Page = page;
            ViewBag.Sort = sort;
            ViewBag.Ascending = ascending;
            ViewBag.Filter = filter;
            ViewBag.Position = position;
            masterDetailViewModel.NaplatnaKucicaId = id;

            Console.WriteLine(masterDetailViewModel.prolasciVozilaTS.Count);

            if (ModelState.IsValid)
            {
                var naplatnaKucica = await ctx.NaplatnaKucica
                                        .Include(nk => nk.ProlazakVozila)
                                        .Where(nk => nk.Id == masterDetailViewModel.NaplatnaKucicaId)
                                        .FirstOrDefaultAsync();
                if (naplatnaKucica == null)
                {
                    logger.LogWarning("Ne postoji naplatna kucica s oznakom: {0} ", masterDetailViewModel.NaplatnaKucicaId);
                    return NotFound("Ne postoji naplatna kucica s id-om: " + masterDetailViewModel.NaplatnaKucicaId);
                }

                CopyValuesMaster(naplatnaKucica, masterDetailViewModel);
                ctx.RemoveRange(naplatnaKucica.ProlazakVozila.Where(pv => pv.NaplatnaKucicaId == naplatnaKucica.Id));

                foreach (var prolazakVozila in masterDetailViewModel.prolasciVozilaTS)
                {
                    if (prolazakVozila == null)
                        continue;
                    ProlazakVozila newProlazakVozila;
                    if (prolazakVozila.Id > 0)
                    {
                        newProlazakVozila = await ctx.ProlazakVozila.Where(pv => pv.Id == prolazakVozila.Id).SingleOrDefaultAsync();
                    }
                    else
                    {
                        newProlazakVozila = new ProlazakVozila();
                    }
                    CopyValuesDetail(newProlazakVozila, prolazakVozila.ConvertToProlazakVozila());
                    naplatnaKucica.ProlazakVozila.Add(newProlazakVozila);
                    ctx.Update(newProlazakVozila);
                }

                try
                {
                    ctx.Update(naplatnaKucica);
                    await ctx.SaveChangesAsync();

                    TempData[Constants.Message] = $"(MD) Naplatna kucica s id: {naplatnaKucica.Id} uspješno ažurirana.";
                    TempData[Constants.ErrorOccurred] = false;
                    return RedirectToAction(nameof(Edit), new
                    {
                        id = naplatnaKucica.Id,
                        position,
                        filter,
                        page,
                        sort,
                        ascending
                    });
                }
                catch (Exception exc)
                {
                    ModelState.AddModelError(string.Empty, exc.CompleteExceptionMessage());
                    return View(masterDetailViewModel);
                }
            }
            else
            {
                return View(masterDetailViewModel);
            }
        }

        private async Task PrepareDropdownLists()
        {
            var naplatnePostaje = await ctx.NaplatnaPostaja
                            .Select(d => new { d.Naziv, d.Id })
                            .ToListAsync();
            ViewBag.NaplatnaPostaja = new SelectList(naplatnePostaje, nameof(NaplatnaPostaja.Id), nameof(NaplatnaPostaja.Naziv));

            var vrsteNaplate = await ctx.VrstaNaplate
                                .Select(d => new { d.Naziv, d.Id })
                                .ToListAsync();
            ViewBag.VrstaNaplate = new SelectList(vrsteNaplate, nameof(VrstaNaplate.Id), nameof(VrstaNaplate.Naziv));
        }

        private static void CopyValuesMaster(NaplatnaKucica naplatnaKucica, NaplatneKuciceMDViewModel naplatnaKucicaDto)
        {
            naplatnaKucica.NaplatnaPostajaId = naplatnaKucicaDto.NaplatnaPostajaId;
            naplatnaKucica.VrstaNaplateId = naplatnaKucicaDto.VrstaNaplateId;
            naplatnaKucica.Otvorena = naplatnaKucicaDto.Otvorena;
        }

        private static void CopyValuesDetail(ProlazakVozila newProlazakVozila, ProlazakVozila prolazakVozilaDto)
        {
            newProlazakVozila.RegistracijskaOznaka = prolazakVozilaDto.RegistracijskaOznaka;
            newProlazakVozila.KategorijaVozilaId = prolazakVozilaDto.KategorijaVozilaId;
            newProlazakVozila.NaplatnaKucicaId = prolazakVozilaDto.NaplatnaKucicaId;
            newProlazakVozila.VrijemeProlaska = prolazakVozilaDto.VrijemeProlaska;
        }


    }
}