package hr.fer.zemris.UDP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Poruka potvrde. Sastoji se od korisnikovog imena, random kljuca,
 *  koda i rednog broja paketa.
 * @author Filip
 *
 */
public class HELLO extends Message {

	String name;
	long randomKey;

	/**
	 * Konstruktor, zadaje vrijednosti potrebne za poruku tipa HELLO.
	 * @param number
	 * @param name
	 * @param randomKey
	 */
	public HELLO(long number, String name, long randomKey) {
		super.setKod((byte) 1);
		super.setRedniBrojPaketa(number);
		this.name = name;
		this.randomKey = randomKey;
	}

	/**
	 * Getter za ime korisnika.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Getter za random kljcu korisnika.
	 * @return
	 */
	public long getRandomKey() {
		return randomKey;
	}

	/**
	 * Metoda koja stvara polje bajtova iz svih podataka poruke HELLO.
	 * 
	 */
	@Override
	public byte[] createPackageData() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try {
			dos.writeByte(this.kod);
			dos.writeLong((long) 0);
			dos.writeUTF(name);
			dos.writeLong(randomKey);
			dos.close();
			return bos.toByteArray();
		} catch(IOException e) {
			System.out.println("Error while creating UDP package content.");
			return null;
		}
	}

	/**
	 * Metoda prima paket koji je primljen i provjerava se sadrzi li paket poruku tipa HELLO.
	 * Ako da, onda se iz paketa izvlace samo polje bajtova koje nosi korinu informaciju.
	 * @param recvPacket
	 * @return
	 */
	public static byte[] isHelloPacket(DatagramPacket recvPacket) {
		byte[] recvData = new byte[recvPacket.getLength()];
		System.arraycopy(recvPacket.getData(), 0, recvData, 0, recvPacket.getLength());

		if(recvData[0] == (byte) 1) {
			return recvData;
		} else {
			return null;
		}
	}
}
