$(document).on('click', '.deleterow', function () {
    event.preventDefault();
    var tr = $(this).parents("tr");
    tr.remove();
    clearOldMessage();
});

$(function () {
    $(".form-control").bind('keydown', function (event) {
        if (event.which === 13) {
            event.preventDefault();
        }
    });

    $("#prolazak").click(function () {
        event.preventDefault();
        dodajProlazak();
    });
});

function dodajProlazak() {
    var sifra = $("#prolazak-sifra").val(); //+
    if (sifra != '') {
        if ($("[name='prolasciVozila[" + sifra + "].Id'").length > 0) {
            alert('Prolazak vec postoji');
            console.log("alert");
            return;
        }

        console.log("ok");

        var kategorija = $("#prolazak-kategorijaVozila").val();                        //+
        var kategorijaId = $("#prolazak-kategorijaId").val();   //+
        var registracija = $("#prolazak-RegistracijskaOznaka").val();   //+
        var vrijeme = $("#prolazak-vrijemeProlaska").val();       //+    //+

        //Alternativa ako su hr postavke sa zarezom //http://haacked.com/archive/2011/03/19/fixing-binding-to-decimals.aspx/
        //ili ovo http://intellitect.com/custom-model-binding-in-asp-net-core-1-0/

        var template = $('#template').html();
        template = template.replace(/--sifra--/g, sifra)
            .replace(/--kategorijaVozila--/g, kategorija)
            .replace(/--kategorijaVozilaId--/g, kategorijaId)
            .replace(/--vrijemeProlaska--/g, vrijeme)
            .replace(/--registracijskaOznaka--/g, registracija)
        $(template).find('tr').insertBefore($("#table-prolasci").find('tr').last());

        var novaSifra = parseInt(sifra) + 1;

        $("#prolazak-sifra").val(novaSifra);
        $("#prolazak-kategorijaVozila").val('');
        $("#prolazak-kategorijaId").val('');
        $("#prolazak-RegistracijskaOznaka").val('');
        $("#prolazak-vrijemeProlaska").val('');
       
        clearOldMessage();
    }
}