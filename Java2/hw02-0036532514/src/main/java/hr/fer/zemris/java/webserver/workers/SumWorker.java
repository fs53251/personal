package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {

		String parameterA = context.getParameter("a");
		Integer a = null;
		if(parameterA == null) {
			a = 1;
		} else {
			try {
				a = Integer.parseInt(parameterA);
			} catch(Exception e) {
				a = 1;
			}
		}

		String parameterB = context.getParameter("b");
		Integer b = null;
		if(parameterB == null) {
			b = 2;
		} else {
			try {
				b = Integer.parseInt(parameterB);
			} catch(Exception e) {
				b = 2;
			}
		}

		int sum = a + b;
		String suma = Integer.toString(sum);

		context.setTemporaryParameters("zbroj", suma);
		context.setTemporaryParameters("varA", Integer.toString(a));
		context.setTemporaryParameters("varB", Integer.toString(b));

		if(sum % 2 == 0) {
			context.setTemporaryParameters("imgName", "slika1");
		} else {
			context.setTemporaryParameters("imgName", "slika2");
		}

		context.getDispatcher().dispatchRequest("private/pages/calc.smscr");
	}

}
