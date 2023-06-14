package hr.fer.zemris.java.servleti;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Servlet koji generira excel file sa n stranica. Klijent šalje parametre a, b, i n, a na temelju njih
 * servlet stvara n stranica i na svakoj generira brojeve od a do b i njihove potencije.
 * Potencije ovise o broju stranice.
 * @author Filip
 *
 */
@WebServlet(name = "powers", urlPatterns = "/powers")
public class PowersServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Integer a = -200;
		Integer b = -200;
		Integer n = -200;
		try {
			a = Integer.parseInt(req.getParameter("a"));
			b = Integer.parseInt(req.getParameter("b"));
			n = Integer.parseInt(req.getParameter("n"));

			if((a < -100 || a > 100) || (b < -100 || b > 100) || (n < 1 || n > 5)) {
				req.setAttribute("error", "Invalid parameters. Expected a (integer from [-100,100]) b (integer from [-100,100]) and n (where n>=1 and n<=5).");
				req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req, resp);
			}
		} catch(Exception e) {
			req.setAttribute("error", "Invalid parameters. Expected a (integer from [-100,100]) b (integer from [-100,100]) and n (where n>=1 and n<=5).");
			req.getRequestDispatcher("WEB-INF/pages/error.jsp").forward(req, resp);
		}

		resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");
		try(HSSFWorkbook workbook = new HSSFWorkbook()) {
			for(int i = 1; i <= n; i++) {
				HSSFSheet sheet = workbook.createSheet("sheet " + i);
				for(int j = a, brojac = 1; j <= b; j++, brojac++) {
					HSSFRow row = sheet.createRow(brojac - 1);
					row.createCell(0).setCellValue(j);
					row.createCell(1).setCellValue(Math.pow(j, i));

				}
			}

			OutputStream os = resp.getOutputStream();
			workbook.write(os);
			os.close();
		}
	}

}
