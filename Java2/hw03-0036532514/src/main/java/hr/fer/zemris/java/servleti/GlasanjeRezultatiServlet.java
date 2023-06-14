package hr.fer.zemris.java.servleti;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
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
 * Servlet zadužen za prikaz rezultata glasanja. On dohvaća podatke od bandovima isto kao i razred GlasanjeGrafikaServlet
 * te kolekciju bandova prosljeđuje kao parametar zahtjeva. Također uzima kolekciju najboljih bandova i njihovu kolekciju 
 * prosljeđuje kao parametar zahtjeva.
 * 
 * Posao generiranja sadržaja prosljeđuje glasanjeRez.jsp.
 * @author Filip
 *
 */
@WebServlet(name = "glasanjeRez", urlPatterns = { "/glasanje-rezultati" })
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

		File file = new File(fileName);
		if(!file.exists()) {
			file.createNewFile();
		} else {
			List<Band> rez = Files.readAllLines(file.toPath()).stream()
					.map(line -> line.split(" "))
					.map(l -> {
						Long id = Long.parseLong(l[0]);
						Long votes = Long.parseLong(l[1]);

						//nadi ime benda iz filea
						String fname = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");

						List<Band> bands;
						try {
							bands = Files.readAllLines(Paths.get(fname)).stream()
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

							for(Band band : bands) {
								if(band.getId() == id) {
									Band b = new Band(id, band.getName(), band.getSongURL());
									b.setVotes(votes);
									return b;
								}
								continue;
							}
						} catch(IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return null;
					})
					.collect(Collectors.toList());

			List<Band> winners = new ArrayList<>();
			Collections.sort(rez, (o1, o2) -> Long.compare(o2.getVotes(), o1.getVotes()));
			Long najvisiVote = -1l;
			for(Band b : rez) {
				if(najvisiVote == -1l) {
					najvisiVote = b.getVotes();
					winners.add(b);
					continue;
				}
				if(najvisiVote != -1l && b.getVotes() == najvisiVote) {
					winners.add(b);
				}
			}
			req.setAttribute("winners", winners);
			req.setAttribute("rezBand", rez);
			req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
		}

	}
}
