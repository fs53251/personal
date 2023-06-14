package oprpp2.hw01.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import hr.fer.zemris.UDP.ACK;

/**
 * Razred koji predstavlja korinikov prozor.
 * @author Filip
 *
 */
public class Chat extends JFrame {
	private static final long serialVersionUID = 1L;

	ClientUtil clientUtil;
	String lastChat;
	JTextArea textArea;

	public Chat(ClientUtil clientUtil) {
		this.clientUtil = clientUtil;

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Chat client: " + clientUtil.username);
		setSize(500, 200);
		setLocation(0, 0);
		setVisible(true);

		initGUI();
	}

	private void initGUI() {
		Container cp = this.getContentPane();
		JTextField textField = new JTextField();
		cp.add(textField, BorderLayout.NORTH);

		textArea = new JTextArea();
		cp.add(new JScrollPane(textArea), BorderLayout.CENTER);

		clientUtil.setup();
		Thread t = new Thread(new Posao(clientUtil, textArea));
		t.start();
		try {
			clientUtil.sendHello();
		} catch(SocketException e2) {}
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				try {
					clientUtil.sendBye();
					System.out.println("gasim prozor");
					clientUtil.dSocket.close();
					e.getWindow().dispose();
				} catch(SocketException e1) {
					System.out.println("Error while sending BYE");
				}
				System.exit(0);
			}
		});

		textField.addActionListener(e -> {
			String str = textField.getText().trim();
			textField.setText("");

			try {
				clientUtil.sendOutMsg(str);

			} catch(SocketException e1) {
				System.out.println("Error while sending OUTMSG");
				System.exit(1);
			}

		});
	}
}

class Posao implements Runnable {

	ClientUtil clientUtil;
	JTextArea ta;

	public Posao(ClientUtil util, JTextArea ta) {
		this.clientUtil = util;
		this.ta = ta;
	}

	@Override
	public void run() {
		while(true) {
			try {
				if(clientUtil.dSocket.isClosed()) {
					System.exit(0);
				}

				byte[] recvBuffer = new byte[clientUtil.dSocket.getReceiveBufferSize()];
				DatagramPacket recvPacket = new DatagramPacket(recvBuffer, recvBuffer.length);

				try {
					clientUtil.dSocket.receive(recvPacket);
				} catch(IOException e) {
					continue;
				}

				//primi ACK
				if(recvPacket.getLength() == 17 && recvPacket.getData()[0] == (byte) 2) {
					byte[] recvData = recvPacket.getData();

					long recvNumber = ByteBuffer.wrap(Arrays.copyOfRange(recvData, 1, 9)).getLong();
					long recvUID = ByteBuffer.wrap(Arrays.copyOfRange(recvData, 9, 17)).getLong();

					System.out.printf("Primio ACK(%d, %d)%n", recvNumber, recvUID);

					if(!clientUtil.isSetUID) {
						clientUtil.isSetUID = true;
						clientUtil.UID = recvUID;
						System.out.println("Postavljen UID: " + clientUtil.UID);
					}
					if(recvNumber == clientUtil.packetNumber.get() && recvUID == clientUtil.UID) {
						System.out.println("Recieved ACK");
						clientUtil.ackRecieved = true;
						synchronized(clientUtil.mutex) {
							clientUtil.mutex.notify();
						}
					} else {
						System.out.println("Recieved wrong data format.");
					}
				} else if(recvPacket.getData()[0] == (byte) 5) {
					byte[] bytes = recvPacket.getData();
					byte recvCode = bytes[0];
					long recvNumber = ByteBuffer.wrap(Arrays.copyOfRange(bytes, 1, 9)).getLong();

					byte[] textBytes = Arrays.copyOfRange(bytes, 9, bytes.length);

					ByteArrayInputStream bis = new ByteArrayInputStream(textBytes);
					DataInputStream dis = new DataInputStream(bis);

					String name = null;
					String text = null;
					try {
						name = dis.readUTF();
						text = dis.readUTF();
					} catch(IOException e1) {}

					System.out.println(name);
					System.out.println(text);

					if(recvCode == (byte) 5) {
						System.out.println("Recieved INSMSG");

						System.out.printf("Primio INMSG(%d, %s %s)%n", recvNumber, name, text);

						ACK ack = new ACK(recvNumber, clientUtil.UID);
						byte[] data = ack.createPackageData();

						DatagramPacket packet = new DatagramPacket(data, data.length);
						try {
							packet.setSocketAddress(new InetSocketAddress(InetAddress.getByName(clientUtil.host), clientUtil.port));
							clientUtil.dSocket.send(packet);
						} catch(IOException e) {
							System.out.println("Host unknown");
						}
						ChatIspis chatIspis = new ChatIspis(name, text, recvPacket.getAddress().getHostAddress(), Integer.toString(recvPacket.getPort()));
						if(chatIspis != null) {
							System.out.println(chatIspis.toString());

							String te = ta.getText();
							te = chatIspis.toString() + te;
							ta.setText(te);
						}
					} else {
						System.out.println("Recieved wrong data format.");
					}
				}
			} catch(SocketException e) {
				e.printStackTrace();
			}
		}
	}

}
