package oprpp2.hw01.client;

import javax.swing.SwingUtilities;

/**
 * Razred parsira unos argumenata. Stvara instancu pomocnog razreda ClientUtil i poziva iscrtavanje korinsikovog prozora.
 * @author Filip
 *
 */
public class Main {

	public static void main(String[] args) {
		if(args.length != 3) {
			System.out.println("Expected input: host port name");
			return;
		}

		String host = args[0];
		int port = Integer.parseInt(args[1]);
		if(!(port >= 1024 && port <= 65535)) {
			System.out.println("Wrong port number, use port from range [1024 - 65535]");
			return;
		}
		String username = args[2];

		ClientUtil util = new ClientUtil(host, port, username);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Chat(util).setVisible(true);
			}
		});

	}
}
