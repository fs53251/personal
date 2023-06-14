package hr.fer.zemris.UDP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Poruka odjave. Sastoji se od korinsikovog User ID-a, koda i rednog broja paketa.
 * @author Filip
 *
 */
public class BYE extends Message {

	long UID;

	/**
	 * Konstruktor zadaje kod i redni broj paketa kroz setterske metode razreda kojeg
	 * nasljeduje.
	 * @param number
	 * @param UID
	 */
	public BYE(long number, long UID) {
		super.setKod((byte) 3);
		super.setRedniBrojPaketa(number);
		this.UID = UID;
	}

	/**
	 * Getter za User ID.
	 * @return
	 */
	public long getUID() {
		return UID;
	}

	/**
	 * Metoda koja stvara polje bajtova iz svih podataka poruke BYE.
	 * 
	 */
	@Override
	public byte[] createPackageData() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try {
			dos.writeByte(this.kod);
			dos.writeLong(this.redniBrojPaketa);
			dos.writeLong(UID);
			dos.close();
			return bos.toByteArray();
		} catch(IOException e) {
			System.out.println("Error while creating UDP package content.");
			return null;
		}
	}

	/**
	 * Metoda prima paket koji je primljen i provjerava se sadrzi li paket poruku tipa BYE.
	 * Ako da, onda se iz paketa izvlace samo polje bajtova koje nosi korinu informaciju.
	 * @param recvPacket
	 * @return
	 */
	public static byte[] isByePacket(DatagramPacket recvPacket) {
		byte[] recvData = new byte[recvPacket.getLength()];
		System.arraycopy(recvPacket.getData(), 0, recvData, 0, recvPacket.getLength());

		if(recvData[0] == (byte) 3) {
			return recvData;
		} else {
			return null;
		}
	}
}
