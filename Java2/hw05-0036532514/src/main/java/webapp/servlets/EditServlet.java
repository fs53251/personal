package webapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOProvider;

@WebServlet(name = "edit", urlPatterns = { "/servleti/edit/*" })
public class EditServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String title = req.getParameter("title");
		String text = req.getParameter("text");
		Long entryId = (Long) req.getSession().getAttribute("current.entry.id");
		Long userId = (Long) req.getSession().getAttribute("current.user.id");
		DAOProvider.getDao().editBlogEntry(title, text, userId, entryId);

		resp.sendRedirect("/blog/servleti/author/" + (String) req.getSession().getAttribute("current.user.nick"));
	}
}
