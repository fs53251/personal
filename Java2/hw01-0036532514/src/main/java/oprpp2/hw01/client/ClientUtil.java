package oprpp2.hw01.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import hr.fer.zemris.UDP.BYE;
import hr.fer.zemris.UDP.HELLO;
import hr.fer.zemris.UDP.OUTMSG;

/**
 * Pomocni razred u kojem se nalaze implementacije svih potrebnih funkcionalnosti iz zadatka.
 * Ovdje cuvam osnovne podatke potrebne za komunikaciju s klijentima.
 * @author Filip
 *
 */
public class ClientUtil {

	String host;
	int port;
	String username;
	DatagramSocket dSocket;
	long UID;

	AtomicLong packetNumber;

	Object mutex = new Object();
	volatile boolean ackRecieved = false;
	volatile boolean isSetUID = false;

	boolean setupCalled = false;

	/**
	 * Konstruktor
	 * @param host
	 * @param port
	 * @param username
	 */
	public ClientUtil(String host, int port, String username) {
		this.host = host;
		this.port = port;
		this.username = username;
		UID = 0L;
	}

	/**
	 * Metoda koja se poziva na pocetku i ona stvara instancu UDP sokceta.
	 */
	public void setup() {
		try {
			this.setupCalled = true;
			packetNumber = new AtomicLong(0);
			dSocket = new DatagramSocket();
		} catch(SocketException e) {
			System.out.println("Error while opening socket.");
		}
	}

	/**
	 * Metoda koja resetira sve potrebene podatke.
	 */

	public void close() {
		dSocket.close();
		UID = 0L;
		packetNumber = new AtomicLong(0);
		setupCalled = false;
	}

	/**
	 * Metoda koja vrsi slanje poruke HELLO.
	 * @throws SocketException
	 */
	public void sendHello() throws SocketException {
		packetNumber = new AtomicLong(0);

		Random rand = new Random();
		long randomKey;
		randomKey = rand.nextLong();

		HELLO message = new HELLO(packetNumber.get(), username, randomKey);
		byte[] data = message.createPackageData();
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(host), port));
		} catch(UnknownHostException e) {
			System.out.println("Host unknown");
			return;
		}

		for(int i = 0; i < 10; i++) {
			try {
				System.out.printf("Poslao HELLO(%d, %s, %d)%n", packetNumber.get(), username, randomKey);
				dSocket.send(packet);
			} catch(IOException e) {
				System.out.println("Packet can not be send.");
				break;
			}

			synchronized(mutex) {
				while(!ackRecieved) {
					try {
						mutex.wait();
					} catch(InterruptedException e) {

					}
					if(!ackRecieved) {
						break;
					}
				}
			}

			if(ackRecieved) {
				break;
			}
		}

		if(!ackRecieved) {
			System.out.println("Gasim klijenta");
			dSocket.close();
			System.exit(1);
		} else {
			packetNumber.incrementAndGet();
			System.out.println("Novi packetNumber: " + packetNumber);
			ackRecieved = false;
		}
	}

	/**
	 * Metoda koja vrsi slanje poruke BYE.
	 * @throws SocketException
	 */
	public void sendBye() throws SocketException {

		BYE message = new BYE(packetNumber.get(), UID);
		byte[] data = message.createPackageData();
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(host), port));
		} catch(UnknownHostException e) {
			System.out.println("Host unknown");
			return;
		}

		for(int i = 0; i < 10; i++) {
			try {
				dSocket.send(packet);
				System.out.printf("Poslao BYE(%d, %d)%n", packetNumber.get(), UID);
			} catch(IOException e) {
				System.out.println("Packet can not be send.");
				break;
			}

			synchronized(mutex) {
				while(!ackRecieved) {
					try {
						mutex.wait(5000);
					} catch(InterruptedException e) {

					}
					if(!ackRecieved) {
						break;
					}
				}
			}

			if(ackRecieved) {
				break;
			}
		}

		if(!ackRecieved) {
			System.out.println("Turn off client");
			dSocket.close();
			System.exit(1);
		} else {
			System.out.println("Logout from host.");
			ackRecieved = false;
			close();
		}
	}

	/**
	 * Metoda koja vrsi slanje poruke OUTMSG.
	 * @throws SocketException
	 */
	public void sendOutMsg(String text) throws SocketException {

		OUTMSG message = new OUTMSG(packetNumber.get(), UID, text);
		System.out.println("Saljem OUTMSG s packetNumberom: " + packetNumber.get());
		byte[] data = message.createPackageData();
		DatagramPacket packet = new DatagramPacket(data, data.length);
		try {
			packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(host), port));
		} catch(UnknownHostException e) {
			System.out.println("Host unknown");
			return;
		}

		for(int i = 0; i < 10; i++) {
			try {
				dSocket.send(packet);
				System.out.printf("Poslao OUTMSG(%d, %d, %s)%n", packetNumber.get(), UID, text);

			} catch(IOException e) {
				System.out.println("Packet can not be send.");
				break;
			}

			synchronized(mutex) {
				while(!ackRecieved) {
					try {
						mutex.wait(5000);
					} catch(InterruptedException e) {

					}
					if(!ackRecieved) {
						break;
					}
				}
			}

			if(ackRecieved) {
				break;
			}
		}

		if(!ackRecieved) {
			System.out.println("Turn off client");
			dSocket.close();
			System.exit(1);
		} else {
			packetNumber.incrementAndGet();
			System.out.println("Novi packetNumber: " + packetNumber);
			ackRecieved = false;
		}
	}
}
