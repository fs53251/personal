package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		String bg = context.getPersistentParameter("bgcolor");
		if(bg != null) {
			context.setTemporaryParameters("background", bg);
		} else {
			context.setTemporaryParameters("background", "7F7F7F");
		}

		context.getDispatcher().dispatchRequest("private/pages/home.smscr");
	}

}
