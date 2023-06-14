package hr.fer.oprpp2.hw04.webapp.servlets;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.oprpp2.hw04.dao.DAO;
import hr.fer.oprpp2.hw04.dao.DAOProvider;
import hr.fer.oprpp2.hw04.model.PollOptions;

@WebServlet(name = "glasanjeRez", urlPatterns = { "/servleti/glas/*" })
public class GlasanjeRezultatiServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		int pollID = Integer.parseInt(req.getParameter("pollID"));
		DAO dao = DAOProvider.getDao();
		PollOptions obj = dao.getPollOptionById(id);

		if(req.getRequestURI().endsWith("/dislike")) {
			dao.changeDislikeNumberForPollOption(id);
		} else {
			dao.changeVoteNumberForPollOption(id);
		}

		List<PollOptions> pollOptions = dao.filterPollOtionsByPollId(pollID);

		long maxVotes = pollOptions.stream()
				.max((e1, e2) -> Long.compare(e1.getVotesCount(), e2.getVotesCount()))
				.get().getVotesCount();

		List<PollOptions> winners = pollOptions.stream().filter(e -> e.getVotesCount() == maxVotes).collect(Collectors.toList());
		req.setAttribute("winners", winners);
		req.setAttribute("pollOptions", pollOptions);
		req.getRequestDispatcher("/WEB-INF/pages/glasanjeRez.jsp").forward(req, resp);
	}

}
