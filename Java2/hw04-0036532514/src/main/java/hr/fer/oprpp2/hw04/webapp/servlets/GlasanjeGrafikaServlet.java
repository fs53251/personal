package hr.fer.oprpp2.hw04.webapp.servlets;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.oprpp2.hw04.dao.DAOProvider;
import hr.fer.oprpp2.hw04.model.PollOptions;

/**
 * Servlet koji na temelju rezultata dohvaća preko id-a sve podatke o bandu. Svi podaci su zapisani u datoteci
 * glasanje-definicija.txt pa se ti podaci vade u posebnu kolekciju.
 * Kad se dohvati kolekcija, stvara se pie char na temelju rezultata glasanja i vraća se slika u formatu png.
 * @author Filip
 *
 */
@WebServlet(name = "grafika", urlPatterns = { "/servleti/glasanje-grafika" })
public class GlasanjeGrafikaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int id = Integer.parseInt(req.getParameter("pollID"));

		List<PollOptions> rez = DAOProvider.getDao().filterPollOtionsByPollId(id);

		JFreeChart ch = createChart(rez);

		resp.setContentType("image/png");
		OutputStream out = resp.getOutputStream();
		ChartUtils.writeChartAsPNG(out, ch, 400, 400);
		out.close();

	}

	private JFreeChart createChart(List<PollOptions> rez) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		for(PollOptions b : rez) {
			dataset.setValue(b.getOptionTitle(), b.getVotesCount());
		}

		JFreeChart chart = ChartFactory.createPieChart("Chart", dataset, true, true, false);

		chart.setBorderPaint(Color.GREEN);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}
}
