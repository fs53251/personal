package hr.fer.zemris.java.servleti;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji omogućava praćenje rezultata glasanja. Korisnik predaje parametar id i na temelju tog parametra
 * se u datoteci zabilježi glas za taj bend. Ako nije bilo glasova za taj bend u datoteci, dodaje se novi zapis.
 * @author Filip
 *
 */
@WebServlet(name = "glasaj", urlPatterns = { "/glasanje-glasaj" })
public class GlasanjeGlasajServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

		Long id = Long.parseLong(req.getParameter("id"));

		File file = new File(fileName);
		String str = "";
		if(file.exists()) {
			List<String[]> par = Files.readAllLines(file.toPath()).stream()
					.map(line -> line.split(" ")).collect(Collectors.toList());
			boolean nasao = false;
			for(String[] s : par) {
				Long fileID = Long.parseLong(s[0]);
				Long votes = Long.parseLong(s[1]);

				if(id == fileID) {
					str += String.format("%s %s\n", id, votes + 1);
					nasao = true;
				} else {
					str += String.format("%s %s\n", fileID, votes);
				}
			}
			if(!nasao) {
				str += String.format("%s %s\n", id, 1);
			}
		} else {
			str = String.format("%s %s\n", id, 1);
		}

		FileWriter writer = new FileWriter(file, false);
		writer.write(str);

		writer.flush();
		writer.close();
		resp.sendRedirect(req.getContextPath() + "/glasanje-rezultati");
	}
}
