package hr.fer.zemris.java.servleti;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 * Servlet generira sliku 500 x 500. Slika prestavlja random podatke prikazane
 * kao pie chart.
 * @author Filip
 *
 */
@WebServlet(name = "chart", urlPatterns = { "/reportImage" })
public class ChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("image/png");

		OutputStream os = resp.getOutputStream();
		JFreeChart ch = createChart();

		ChartUtils.writeChartAsPNG(os, ch, 500, 500);
	}

	/**
	 * Metoda tvornica pie charta, koristi random podatke za stvaranje charta.
	 * @return
	 */
	private JFreeChart createChart() {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		Random random = new Random();
		dataset.setValue("First", random.nextInt(100));
		dataset.setValue("Second", random.nextInt(100));
		dataset.setValue("Third", random.nextInt(100));
		dataset.setValue("Fourth", random.nextInt(100));

		JFreeChart chart = ChartFactory.createPieChart("Chart", dataset, true, true, false);

		chart.setBorderPaint(Color.GREEN);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}
}
