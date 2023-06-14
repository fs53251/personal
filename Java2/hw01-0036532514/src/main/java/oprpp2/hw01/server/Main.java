package oprpp2.hw01.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import hr.fer.zemris.UDP.ACK;
import hr.fer.zemris.UDP.BYE;
import hr.fer.zemris.UDP.Client;
import hr.fer.zemris.UDP.HELLO;
import hr.fer.zemris.UDP.INMSG;
import hr.fer.zemris.UDP.Message;
import hr.fer.zemris.UDP.OUTMSG;
import hr.fer.zemris.UDP.Session;

/**
 * Razred posluzitelja, pokrece slusanje na odredenom portu i obraduje sve primljene poruke.
 * @author Filip
 *
 */
public class Main {
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Expected port.");
			return;
		}

		System.out.println("Host listening on port: " + args[0]);

		Random random = new Random();

		/*++++++++++++++++/ STRUKTURA /++++++++++++++++++++++++++++*/
		long UID = Math.abs(random.nextLong());
		List<Session> sessions = new ArrayList<>();
		List<Client> klijenti = new ArrayList<>();
		/*+++++++++++++++++++++++++++++++++++++++++++++*/

		int port = Integer.parseInt(args[0]);

		DatagramSocket dSocket;
		try {
			dSocket = new DatagramSocket(null);
			dSocket.setSoTimeout(5000);
			dSocket.bind(new InetSocketAddress((InetAddress) null, port));
		} catch(SocketException e) {
			System.out.println("Error while creating socket");
			return;
		}

		while(true) {
			byte[] recvBuff;
			DatagramPacket recvPacket;
			try {
				recvBuff = new byte[dSocket.getReceiveBufferSize()];
				recvPacket = new DatagramPacket(recvBuff, recvBuff.length);
			} catch(SocketException e) {
				System.out.println("Error while accessing receiver buffer size.");
				break;
			}

			try {
				dSocket.receive(recvPacket);
			} catch(IOException e) {
				continue;
			}

			if(HELLO.isHelloPacket(recvPacket) != null) {
				byte[] helloPacket = HELLO.isHelloPacket(recvPacket);
				long recvNumber = ByteBuffer.wrap(Arrays.copyOfRange(helloPacket, 1, 9)).getLong();
				String recvUsername = new String(Arrays.copyOfRange(helloPacket, 11, helloPacket.length - 8));
				long recvRandomKey = ByteBuffer.wrap(Arrays.copyOfRange(helloPacket, helloPacket.length - 8, helloPacket.length)).getLong();

				System.out.printf("Primio HELLO(%d, %s, %d)%n", recvNumber, recvUsername, recvRandomKey);

				if(recvNumber != 0) {
					return;
				}

				Session recvSession = new Session(recvPacket.getSocketAddress(), recvRandomKey);

				boolean hasSesssion = false;
				for(Session session : sessions) {
					if(session.equals(recvSession)) {
						hasSesssion = true;
					}
				}

				Client user = null;
				if(hasSesssion) {
					user = klijenti.stream().filter((client) -> client.getRandomKey() == recvRandomKey).findFirst().orElse(null);
					UID = user.getUID();
					System.out.println("Stvorena sessija za novog klijenta");
				} else {
					UID = UID + 1;
					sessions.add(recvSession);
					user = new Client(recvRandomKey, UID, recvUsername, 0, recvNumber);
					System.out.println("Klijentu " + user.getIme() + " dodijeljen UID: " + UID);
					klijenti.add(user);
				}

				ACK message = new ACK(recvNumber, UID);
				byte[] data = message.createPackageData();
				DatagramPacket packet = new DatagramPacket(data, data.length);
				packet.setSocketAddress(recvPacket.getSocketAddress());

				try {
					dSocket.send(packet);
					System.out.printf("Poslao ACK(%d, %d)%n", recvNumber, UID);
					user.setNumberOfLastMessageFromClient(recvNumber);
				} catch(IOException e) {
					System.out.println("Error while sending ACK.");
					continue;
				}

				if(!hasSesssion) {
					Thread thread = new Thread(new ClientChat(recvPacket.getSocketAddress(), user, dSocket));
					thread.start();
				}

			} else if(ACK.isAckPacket(recvPacket) != null) {
				//byte(2), long(number), long(UID)
				byte[] ackPacket = ACK.isAckPacket(recvPacket);
				long recvNumber = ByteBuffer.wrap(Arrays.copyOfRange(ackPacket, 1, 9)).getLong();
				long recvUID = ByteBuffer.wrap(Arrays.copyOfRange(ackPacket, 9, 17)).getLong();

				System.out.printf("Primio ACK(%d, %d)%n", recvNumber, recvUID);
				Client user = klijenti.stream().filter((client) -> client.getUID() == recvUID).findFirst().orElse(null);
				if(user == null) {
					System.out.println("Client with sent UID is not recognised.");
					continue;
				}

				if(recvNumber != user.getNumberOfLastMessageFromHost()) {
					System.out.println("Wrong number of ACK sent.");
					continue;
				}

				user.getAck().add(new ACK(recvNumber, recvUID));

			} else if(BYE.isByePacket(recvPacket) != null) {
				byte[] ackPacket = BYE.isByePacket(recvPacket);
				long recvNumber = ByteBuffer.wrap(Arrays.copyOfRange(ackPacket, 1, 9)).getLong();
				long recvUID = ByteBuffer.wrap(Arrays.copyOfRange(ackPacket, 9, 17)).getLong();

				System.out.printf("Primio BYE(%d, %d)%n", recvNumber, recvUID);
				Client user = klijenti.stream().filter((client) -> client.getUID() == recvUID).findFirst().orElse(null);
				if(user == null) {
					System.out.println("Client with sent UID is not recognised.");
					continue;
				}

				if(recvNumber != user.getNumberOfLastMessageFromClient() + 1) {
					System.out.println("Client sent wrong number of message.");
					continue;
				}

				user.setNumberOfLastMessageFromClient(recvNumber);

				user.getMessages().add(new ACK(recvNumber, recvUID));
				System.out.printf("Poslao ACK(%d, %d)%n", recvNumber, recvUID);

				klijenti.remove(user);
				long removeRandomKey = user.getRandomKey();
				Iterator<Session> it = sessions.iterator();
				while(it.hasNext()) {
					Session session = it.next();
					if(session.getRandomKey() == removeRandomKey) {
						it.remove();
						break;
					}
				}
			} else if(OUTMSG.isOutMsgPacket(recvPacket) != null) {
				byte[] outMsgPacket = OUTMSG.isOutMsgPacket(recvPacket);
				long recvNumber = ByteBuffer.wrap(Arrays.copyOfRange(outMsgPacket, 1, 9)).getLong();
				long recvUID = ByteBuffer.wrap(Arrays.copyOfRange(outMsgPacket, 9, 17)).getLong();
				String text = new String(Arrays.copyOfRange(outMsgPacket, 19, outMsgPacket.length));

				System.out.printf("Primio OUTMSG(%d, %d, %s)%n", recvNumber, UID, text);
				Client user = klijenti.stream().filter((client) -> client.getUID() == recvUID).findFirst().orElse(null);
				if(user == null) {
					System.out.println("Client with sent UID is not recognised.");
					continue;
				}

				if(recvNumber != user.getNumberOfLastMessageFromClient() + 1) {
					System.out.println("Client sent wrong number of message.");
					continue;
				}

				user.setNumberOfLastMessageFromClient(recvNumber);

				for(Client client : klijenti) {
					if(client == user) {
						user.getMessages().add(new ACK(recvNumber, recvUID));
						System.out.printf("Poslao ACK(%d, %d)%n", recvNumber, recvUID);
					}
					System.out.printf("Poslao INMSG(%d, %s, %s)", client.getNumberOfLastMessageFromHost() + 1, user.getIme(), text);
					INMSG message = new INMSG(client.getNumberOfLastMessageFromHost() + 1, user.getIme(), text);
					client.getMessages().add(message);
					client.setNumberOfLastMessageFromHost(client.getNumberOfLastMessageFromHost() + 1);
				}

			} else {
				continue;
			}
		}
	}

	/**
	 * Posao koji trebaju izvoditi dretve koje komuniciraju s klijentima.
	 * @author Filip
	 *
	 */
	static class ClientChat implements Runnable {

		SocketAddress socket;
		Client client;
		DatagramSocket dSocket;

		Message message;

		public ClientChat(SocketAddress socket, Client client, DatagramSocket dSocket) {
			this.socket = socket;
			this.client = client;
			this.dSocket = dSocket;
		}

		@Override
		public void run() {

			while(true) {

				//uzmi poruku s reda
				while(true) {
					try {
						message = client.getMessages().take();
						break;
					} catch(InterruptedException e) {}
				}

				for(int i = 0; i < 10; i++) {
					try {
						byte[] data = message.createPackageData();
						DatagramPacket packet = new DatagramPacket(data, data.length);
						packet.setSocketAddress(socket);
						dSocket.send(packet);
					} catch(IOException e) {
						System.out.println("Packet can not be send.");
						break;
					}

					if(message instanceof ACK) {
						break;

					}

					Message recvMessage;
					try {
						recvMessage = client.getAck().poll(5, TimeUnit.SECONDS);
						if(recvMessage != null && recvMessage.getKod() == (byte) 2 && recvMessage.getRedniBrojPaketa() == message.getRedniBrojPaketa()) {
							System.out.println("Dobio sam ispravnu potvrdu");
							break;
						}
					} catch(InterruptedException e) {
						continue;
					}

				}
			}
		}
	}
}
