package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class PozadinaWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String slika1 = "images/slika1.jpg";
		String slika2 = "images/slika2.jpg";

		context.setTemporaryParameters("slika1", slika1);
		context.setTemporaryParameters("slika2", slika2);

		context.getDispatcher().dispatchRequest("private/pages/choose.smscr");

	}

}
