package hr.fer.zemris.java.webserver.workers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class VrijemeWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String vrijeme = dtf.format(now);

		Random r = new Random();
		String slika = "";

		String dohvacenaSlika = context.getParameter("slika");
		if(dohvacenaSlika != null) {
			slika = "images/" + dohvacenaSlika + ".jpg";
		} else {
			if(r.nextInt() % 2 == 0) {
				slika = "images/slika1.jpg";
			} else {
				slika = "images/slika2.jpg";
			}
		}

		context.setPersistentParameter("slika", slika);
		context.setTemporaryParameters("vrijeme", vrijeme);

		context.getDispatcher().dispatchRequest("private/pages/vrijeme.smscr");
	}

}
