package hr.fer.zemris.java.webserver.workers;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class EchoParams implements IWebWorker {

	@Override
	public synchronized void processRequest(RequestContext context) {
		context.setMimeType("text/html");
		Set<String> params = context.getParameterNames();
		List<String> tijelo = params.stream().map(name -> "<tr><td>" + name + "</td><td>" + context.getParameter(name) + "</td></tr>").collect(Collectors.toList());
		try {
			context.write("<html><body>");
			context.write("<h1>Hello!!!</h1>");
			context.write("<table>");

			context.write("<thead>");
			context.write("<tr>");
			context.write("<th>Parameter Name</th>");
			context.write("<th>Parameter Value</th>");
			context.write("</tr>");
			context.write("</thead>");

			context.write("<tbody>");
			tijelo.stream().forEach(el -> {
				try {
					context.write(el);
				} catch(IOException e) {
					e.printStackTrace();
				}
			});
			context.write("</tbody>");

			context.write("</table>");
			context.write("</body></html>");
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
