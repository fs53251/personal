package hr.fer.oprpp2.hw04.webapp.servlets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import hr.fer.oprpp2.hw04.dao.DAOProvider;
import hr.fer.oprpp2.hw04.model.PollOptions;

@WebServlet(name = "glasXls", urlPatterns = { "/servleti/glasanje-xls" })
public class GlasanjeXlsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		int id = Integer.parseInt(req.getParameter("pollID"));

		List<PollOptions> rez = DAOProvider.getDao().filterPollOtionsByPollId(id);
		resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		resp.setHeader("Content-Disposition", "attachment; filename=\"rezultati.xls\"");
		try(HSSFWorkbook workbook = new HSSFWorkbook()) {
			HSSFSheet sheet = workbook.createSheet("sheet one");
			int ro = 0;
			for(PollOptions b : rez) {
				HSSFRow row = sheet.createRow(ro++);
				row.createCell(0).setCellValue(b.getId());
				row.createCell(1).setCellValue(b.getOptionTitle());
				row.createCell(2).setCellValue(b.getOptionLink());
				row.createCell(3).setCellValue(b.getPollID());
				row.createCell(4).setCellValue(b.getVotesCount());
			}
			OutputStream os = resp.getOutputStream();
			workbook.write(os);
			os.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

}
