package hr.fer.zemris.java.servleti;

import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import hr.fer.zemris.java.servleti.model.Band;

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

		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");

		File file = new File(fileName);
		if(!file.exists()) {
			file.createNewFile();
		} else {
			List<Band> rez = Files.readAllLines(file.toPath()).stream()
					.map(line -> line.split(" "))
					.map(l -> {
						Long id = Long.parseLong(l[0]);
						Long votes = Long.parseLong(l[1]);

						//nadi ime benda iz filea
						String fname = req.getServletContext().getRealPath("WEB-INF/glasanje-definicija.txt");

						List<Band> bands;
						try {
							bands = Files.readAllLines(Paths.get(fname)).stream()
									.map(line -> line.split(" "))
									.map(elements -> {
										String name = "";
										for(int i = 1; i <= elements.length - 2; i++) {
											name += elements[i];
										}
										return new Band(Long.parseLong(elements[0]), name, elements[elements.length - 1]);
									})
									.sorted(Comparator.naturalOrder())
									.collect(Collectors.toList());

							for(Band band : bands) {
								if(band.getId() == id) {
									Band b = new Band(id, band.getName(), band.getSongURL());
									b.setVotes(votes);
									return b;
								}
								continue;
							}
						} catch(IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						return null;
					})
					.collect(Collectors.toList());

			JFreeChart ch = createChart(rez);

			resp.setContentType("image/png");
			OutputStream out = resp.getOutputStream();
			ChartUtils.writeChartAsPNG(out, ch, 400, 400);
			out.close();
		}
	}

	private JFreeChart createChart(List<Band> rez) {
		DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
		for(Band b : rez) {
			dataset.setValue(b.getName(), b.getVotes());
		}

		JFreeChart chart = ChartFactory.createPieChart("Chart", dataset, true, true, false);

		chart.setBorderPaint(Color.GREEN);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}
}
