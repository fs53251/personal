package webapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import dao.DAOProvider;
import model.BlogUser;

@WebServlet(name = "reg", urlPatterns = { "/servleti/register" })
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");

		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		String nick = req.getParameter("nick");
		String password = req.getParameter("password");

		DAO dao = DAOProvider.getDao();
		BlogUser blogUser = dao.getBlogUserByNickByPassword(nick, password);

		if(blogUser != null) {
			String message = "User already exists!";
			req.getSession().setAttribute("error", message);
		} else {
			if(dao.createBlogUser(firstName, lastName, nick, email, password)) {
				blogUser = dao.getBlogUserByNick(nick);

				req.getSession().setAttribute("current.user.id", blogUser.getId());
				req.getSession().setAttribute("current.user.fn", blogUser.getFirstName());
				req.getSession().setAttribute("current.user.ln", blogUser.getLastName());
				req.getSession().setAttribute("current.user.nick", blogUser.getNick());
				req.getSession().setAttribute("current.user.email", blogUser.getEmail());

				req.getRequestDispatcher("/WEB-INF/pages/successRegistration.jsp").forward(req, resp);
				return;
			} else {
				String message = "Try another nick or password!";
				req.getSession().setAttribute("error", message);
			}
		}

		req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, resp);
	}
}
