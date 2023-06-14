package webapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOProvider;
import model.BlogUser;

@WebServlet(name = "comment", urlPatterns = { "/servleti/comment/*" })
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestURI = req.getRequestURI();
		String[] uriParts = requestURI.split("/");
		String nick = uriParts[uriParts.length - 1];
		String message = req.getParameter("message");
		Long entryId = (Long) req.getSession().getAttribute("current.entry.id");
		BlogUser blogUser = DAOProvider.getDao().getBlogUserByNick(nick);
		Long entryUsrId = blogUser.getId();

		String email = (String) req.getSession().getAttribute("current.user.email");

		DAOProvider.getDao().createBlogComment(email, message, entryId, entryUsrId);

		resp.sendRedirect("/blog/servleti/author/" + (String) req.getSession().getAttribute("current.user.nick") + "/" + entryId);
	}
}
