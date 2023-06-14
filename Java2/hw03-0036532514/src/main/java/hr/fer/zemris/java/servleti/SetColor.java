package hr.fer.zemris.java.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji dohvaća boju koju je korisnik zadao preko parametara zahtjeva.
 * Postavlja tu boju kao parametar sjednice za tog klijenta i generiranje sadržaja 
 * prepušta na index.jsp.
 * @author Filip
 *
 */
@WebServlet(name = "setColor", urlPatterns = { "/setcolor" })
public class SetColor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String color = req.getParameter("color");

		if(color != null) {
			req.getSession().setAttribute("pickedBgCol", color);
		}

		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}
}
