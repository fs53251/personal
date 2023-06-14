package webapp.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAOProvider;
import model.BlogUser;

@WebServlet(name = "login", urlPatterns = { "/servleti/login" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html");

		String nick = req.getParameter("nick");
		String pass = req.getParameter("userPass");

		System.out.println("Primio sam nick: " + nick + " i pass: " + pass);

		BlogUser blogUser = DAOProvider.getDao().getBlogUserByNickByPassword(nick, pass);

		if(blogUser == null) {
			String message = "User doesnt exist or wrong credentials!";
			req.getSession().setAttribute("error", message);
		} else {
			System.out.println("Blog user id: " + blogUser.getId());
			req.getSession().setAttribute("current.user.id", blogUser.getId());
			req.getSession().setAttribute("current.user.fn", blogUser.getFirstName());
			req.getSession().setAttribute("current.user.ln", blogUser.getLastName());
			req.getSession().setAttribute("current.user.nick", blogUser.getNick());
			req.getSession().setAttribute("current.user.email", blogUser.getEmail());
			req.getSession().setAttribute("error", null);
		}

		resp.sendRedirect("/blog/servleti/main");
	}
}
