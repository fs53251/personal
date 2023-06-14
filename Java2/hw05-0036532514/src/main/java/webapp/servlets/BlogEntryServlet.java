package webapp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DAO;
import dao.DAOProvider;
import model.BlogEntry;
import model.BlogUser;

@WebServlet(name = "blogEntry", urlPatterns = { "/servleti/author/*" })
public class BlogEntryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String requestURI = req.getRequestURI();
		String[] uriParts = requestURI.split("/");

		String nick = uriParts[4];

		if(requestURI.endsWith("/new")) {
			req.getRequestDispatcher("/WEB-INF/pages/new.jsp").forward(req, resp);

		} else if(requestURI.endsWith("/edit")) {
			req.getRequestDispatcher("/WEB-INF/pages/edit.jsp").forward(req, resp);

		} else if(uriParts.length == 5) {
			System.out.println("Dohvacam usera" + nick);
			BlogUser blogUser = DAOProvider.getDao().getBlogUserByNick(nick);
			System.out.println(blogUser.getFirstName());
			List<BlogEntry> entries = blogUser.getEntires();

			if(nick.equals((String) req.getSession().getAttribute("current.user.nick"))) {
				req.setAttribute("addEntry", nick);
			} else {
				req.setAttribute("addEntry", null);
			}

			req.setAttribute("entries", entries);
			req.setAttribute("nick", nick);

			req.getRequestDispatcher("/WEB-INF/pages/nick.jsp").forward(req, resp);

		} else if(uriParts.length == 6) {
			Long eid = Long.parseLong(uriParts[5]);
			System.out.println(eid);
			DAO dao = DAOProvider.getDao();
			BlogUser blogUser = dao.getBlogUserByNick(nick);
			BlogEntry blogEntry = dao.getBlogEntryByIdByUserId(eid, blogUser.getId());

			req.setAttribute("nick", nick);
			req.setAttribute("blogEntry", blogEntry);
			req.getSession().setAttribute("current.entry.id", eid);
			req.getSession().setAttribute("current.entry.title", blogEntry.getTitle());
			req.getSession().setAttribute("current.entry.text", blogEntry.getText());
			req.setAttribute("comments", blogEntry.getComments());

			req.getRequestDispatcher("/WEB-INF/pages/entryId.jsp").forward(req, resp);
		}
	}

}
