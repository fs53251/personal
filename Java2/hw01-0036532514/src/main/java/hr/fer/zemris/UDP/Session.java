package hr.fer.zemris.UDP;

import java.net.SocketAddress;
import java.util.Objects;

/**
 * Razred koji pamti korinikovu sjednicu. Korisnik stvara random key kad salje poruku HELLO.
 * Prilikom prvog primanja poruke hello od korisnika, spremam njegove podatke u instancu 
 * razreda Session i Client.
 * @author Filip
 *
 */
public class Session {

	SocketAddress socketId;
	long randomKey;

	public Session(SocketAddress socketId, long randomKey) {
		super();
		this.socketId = socketId;
		this.randomKey = randomKey;
	}

	public SocketAddress getSocketId() {
		return socketId;
	}

	public long getRandomKey() {
		return randomKey;
	}

	@Override
	public int hashCode() {
		return Objects.hash(randomKey, socketId);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		return randomKey == other.randomKey && Objects.equals(socketId, other.socketId);
	}
}
