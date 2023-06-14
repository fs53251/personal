package hr.fer.zemris.java.servleti;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.servleti.model.Band;

/**
 * Servlet dohvaća podatke o sudionicima u glasanju. Podatke iz datoteke sprema u kolekciju bendova.
 * Unaprijed pripremljeni razred Band ima sve potrebne atribute za čuvanje podataka iz datoteke.
 * 
 * Kolekciju bandova postavlja kao parametar zahtjeva.
 * Generiranje sadržaja prosljeđuje na glasanjeIndex.jsp.
 * @author Filip
 *
 */
@WebServlet(name = "glasanje", urlPatterns = { "/glasanje" })
public class GlasanjeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");

		List<Band> bands = Files.readAllLines(Paths.get(fileName)).stream()
				.map(line -> line.split(" "))
				.map(elements -> {
					String name = "";
					for(int i = 1; i <= elements.length - 2; i++) {
						name += elements[i];
					}
					return new Band(Long.parseLong(elements[0]), name, elements[elements.length - 1]);
				})
				.sorted(Comparator.naturalOrder())
				.collect(Collectors.toList());

		req.setAttribute("bendovi", bands);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeIndex.jsp").forward(req, resp);
	}
}
