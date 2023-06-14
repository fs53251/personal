package hr.fer.zemris.java.servleti.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener koji prati context aplikacije. Svaki puta kada je aplikacija inicijalizirana,
 * postavi se atribut "timeStarted" i vrijeme inicijalizacije.
 * Atribut je trajan i čuva se dokle god aplikacija "živi".
 * @author Filip
 *
 */
@WebListener
public class ContextServletListener implements ServletContextListener {

	/**
	 * Nadjačana metoda koja se poziva inicijaliziranjem aplikacije, odnosno njenim pokretanjem.
	 * Sprema se atribut u context parametre.
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute("timeStarted", System.currentTimeMillis());
	}

	/**
	 * Metoda nije nadjačana ovdje, ne radi ništa.
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
