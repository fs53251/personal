package webapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOProvider;

@WebServlet(name = "newEntry", urlPatterns = { "/servleti/new" })
public class NewEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String title = req.getParameter("title");
		String text = req.getParameter("text");
		Long userId = (Long) req.getSession().getAttribute("current.user.id");

		DAOProvider.getDao().createBlogEntry(title, text, userId);

		resp.sendRedirect("/blog/servleti/author/" + (String) req.getSession().getAttribute("current.user.nick"));
	}
}
