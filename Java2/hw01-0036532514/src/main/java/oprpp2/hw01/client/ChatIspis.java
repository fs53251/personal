package oprpp2.hw01.client;

/**
 * Razred koristen za ispis na korisnikov prozor. 
 * Nudi konstruktor i osnovne gettere i settere.
 * @author Filip
 *
 */
public class ChatIspis {
	String ime;
	String text;
	String host;
	String port;

	public ChatIspis(String ime, String text, String host, String port) {
		super();
		this.ime = ime;
		this.text = text;
		this.host = host;
		this.port = port;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return String.format("[/%s:%s] Poruka od korisnika: %s%n%s%n%n", host, port, ime, text);
	}

}
