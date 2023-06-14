package hr.fer.zemris.java.servleti;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "image", urlPatterns = { "/images" })
public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String file = req.getParameter("slika");
		String p = req.getServletContext().getRealPath("/WEB-INF/pages/images/" + file);

		byte[] imageBytes = Files.readAllBytes(Paths.get(p));

		resp.setContentType("image/jpeg");
		resp.setContentLength(imageBytes.length);
		try(OutputStream out = resp.getOutputStream()) {
			out.write(imageBytes);
		}
	}
}
