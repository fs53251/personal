package webapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOProvider;

@WebServlet(name = "logout", urlPatterns = { "/servleti/logout" })
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("users", DAOProvider.getDao().getAllBlogUsers());

		req.getSession().setAttribute("current.user.id", null);
		req.getSession().setAttribute("current.user.fn", null);
		req.getSession().setAttribute("current.user.ln", null);
		req.getSession().setAttribute("current.user.nick", null);
		req.getSession().setAttribute("current.user.email", null);

		resp.sendRedirect("/blog/servleti/main");
	}
}
