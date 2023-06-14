package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.regex.Pattern;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		String bg = context.getParameter("bgcolor");

		if(!Pattern.matches("^[0-9a-fA-F]{6}$", bg)) {
			context.setMimeType("text/html");
			try {
				context.write("<html><body>");
				context.write("<h1>Color is not changed!</h1>");
				context.write("<a href=/index2.html>index2.html</a>");
				context.write("</body></html>");
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		} else {
			context.setPersistentParameter("bgcolor", bg);

			context.setMimeType("text/html");
			try {
				context.write("<html><body>");
				context.write("<h1>Color is changed!</h1>");
				context.write("<a href=/index2.html>index2.html</a>");
				context.write("</body></html>");
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
