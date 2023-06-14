package hr.fer.zemris.java.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji dostavlja resurs o stilu html stranice.
 * @author Filip
 *
 */
@WebServlet(name = "style", urlPatterns = { "/css/style.jsp" })
public class StyleServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/css;caharset=utf-8");
		req.getRequestDispatcher("/WEB-INF/pages/css/style.jsp").forward(req, resp);
	}
}
