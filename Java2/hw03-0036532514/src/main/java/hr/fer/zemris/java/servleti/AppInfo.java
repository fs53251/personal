package hr.fer.zemris.java.servleti;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet nema posebnu logiku, samo na url /appinfo.jsp poziva generiranje traženog resursa.
 * @author Filip
 *
 */
@WebServlet(name = "info", urlPatterns = { "/appinfo.jsp" })
public class AppInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("WEB-INF/pages/appinfo.jsp").forward(req, resp);
	}
}
