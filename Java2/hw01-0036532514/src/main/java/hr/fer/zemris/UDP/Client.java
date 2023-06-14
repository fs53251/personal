package hr.fer.zemris.UDP;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Razred koji cuva osnovne podatke o korisniku. 
 * Cuvaju se svi korisnikovi podaci, redovi poruka i potvrda koje 
 * treba izvesti i koje je korisnik primio.
 * Razred se sastoji od konstruktora i niz gettera i settera za 
 * dane atribute.
 * @author Filip
 *
 */
public class Client {

	long randomKey;
	long UID;
	String ime;
	long numberOfLastMessageFromHost;
	long numberOfLastMessageFromClient;
	LinkedBlockingQueue<Message> messages;
	LinkedBlockingQueue<Message> ack;

	public Client(long randomKey, long UID, String ime, long numberOfLastMessageFromHost, long numberOfLastMessageFromClient) {
		this.randomKey = randomKey;
		this.UID = UID;
		this.ime = ime;
		this.numberOfLastMessageFromHost = numberOfLastMessageFromHost;
		this.numberOfLastMessageFromClient = numberOfLastMessageFromClient;
		messages = new LinkedBlockingQueue<>();
		ack = new LinkedBlockingQueue<>();
	}

	public long getRandomKey() {
		return randomKey;
	}

	public void setRandomKey(long randomKey) {
		this.randomKey = randomKey;
	}

	public long getUID() {
		return UID;
	}

	public void setUID(long uID) {
		UID = uID;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public long getNumberOfLastMessageFromHost() {
		return numberOfLastMessageFromHost;
	}

	public void setNumberOfLastMessageFromHost(long numberOfLastMessageFromHost) {
		this.numberOfLastMessageFromHost = numberOfLastMessageFromHost;
	}

	public long getNumberOfLastMessageFromClient() {
		return numberOfLastMessageFromClient;
	}

	public void setNumberOfLastMessageFromClient(long numberOfLastMessageFromClient) {
		this.numberOfLastMessageFromClient = numberOfLastMessageFromClient;
	}

	public LinkedBlockingQueue<Message> getMessages() {
		return messages;
	}

	public void setMessages(LinkedBlockingQueue<Message> messages) {
		this.messages = messages;
	}

	public LinkedBlockingQueue<Message> getAck() {
		return ack;
	}

	public void setAck(LinkedBlockingQueue<Message> ack) {
		this.ack = ack;
	}

	@Override
	public int hashCode() {
		return Objects.hash(UID);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		return UID == other.UID;
	}

}
