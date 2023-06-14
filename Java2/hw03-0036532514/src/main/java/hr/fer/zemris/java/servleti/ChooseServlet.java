package hr.fer.zemris.java.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "choose", urlPatterns = { "/choose" })
public class ChooseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("slika1", "images?slika=slika1.jpg");
		req.setAttribute("slika2", "images?slika=slika2.jpg");
		req.getRequestDispatcher("/WEB-INF/pages/choose.jsp").forward(req, resp);
	}
}
