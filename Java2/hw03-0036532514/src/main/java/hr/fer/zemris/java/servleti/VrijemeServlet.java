package hr.fer.zemris.java.servleti;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "vrijeme", urlPatterns = { "/vrijeme" })
public class VrijemeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String vrijeme = dtf.format(now);

		Random r = new Random();
		String slika = "";

		String dohvacenaSlika = req.getParameter("slika");
		if(dohvacenaSlika != null) {
			slika = "images?slika=" + dohvacenaSlika + ".jpg";
		} else {
			if(r.nextInt() % 2 == 0) {
				slika = "images?slika=slika1.jpg";
			} else {
				slika = "images?slika=slika2.jpg";
			}
		}

		req.getSession().setAttribute("slika", slika);
		req.setAttribute("vrijeme", vrijeme);

		req.getRequestDispatcher("/WEB-INF/pages/vrijeme.jsp").forward(req, resp);
	}
}
