package hr.fer.zemris.java.servleti;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet koji generira random boju i njome oboji text html datoteke.
 * Servlet se brine o generiranju sadržaja ispisuje ga koirsteći writer.
 * @author Filip
 *
 */
@WebServlet(name = "story", urlPatterns = { "/stories/funny.jsp" })
public class StoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		Random random = new Random();
		Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));

		resp.setContentType("text/html");
		resp.getWriter().write("<html>");
		resp.getWriter().write("<head>");
		resp.getWriter().write("<title>Story</title>");
		resp.getWriter().write("<style> .story{color: " + String.format("#%06X", color.getRGB() & 0xFFFFFF) + ";} </style>");
		resp.getWriter().write("<body>");
		resp.getWriter().write("<div class=\"story\">");
		resp.getWriter().write("<h1>My story</h1>");
		resp.getWriter().write("<p>This is a short story about me.</p>");
		resp.getWriter().write("<p>My name is Filip and I am 22 years old.</p>");
		resp.getWriter().write("<p>End.</p>");
		resp.getWriter().write("</div>");
		resp.getWriter().write("</body>");
		resp.getWriter().write("</html>");

		resp.getWriter().flush();
	}
}
