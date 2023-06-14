package hr.fer.zemris.UDP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

/**
 * Poruka potvrde. Sastoji se od korinsikovog User ID-a, teksta,
 *  koda i rednog broja paketa.
 * @author Filip
 *
 */
public class OUTMSG extends Message {

	long UID;
	String text;

	/**
	 * Konstruktor, za stvaranje poruka OUTMSG.
	 * @param number
	 * @param UID
	 * @param text
	 */
	public OUTMSG(long number, long UID, String text) {
		super.setKod((byte) 4);
		super.setRedniBrojPaketa(number);
		this.UID = UID;
		this.text = text;
	}

	/**
	 * Getter za User ID.
	 * @return
	 */
	public long getUID() {
		return UID;
	}

	/**
	 * Getter teksta.
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * Metoda koja stvara polje bajtova iz svih podataka poruke OUTMSG.
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
			dos.writeUTF(text);
			dos.close();
			return bos.toByteArray();
		} catch(IOException e) {
			System.out.println("Error while creating UDP package content.");
			return null;
		}
	}

	/**
	 * Metoda prima paket koji je primljen i provjerava se sadrzi li paket poruku tipa OUTMSG.
	 * Ako da, onda se iz paketa izvlace samo polje bajtova koje nosi korinu informaciju.
	 * @param recvPacket
	 * @return
	 */
	public static byte[] isOutMsgPacket(DatagramPacket recvPacket) {
		byte[] recvData = new byte[recvPacket.getLength()];
		System.arraycopy(recvPacket.getData(), 0, recvData, 0, recvPacket.getLength());

		if(recvData[0] == (byte) 4) {
			return recvData;
		} else {
			return null;
		}
	}
}
