package hr.fer.zemris.UDP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Poruka potvrde. Sastoji se od korinsikovog imena, teksta,
 *  koda i rednog broja paketa.
 * @author Filip
 *
 */
public class INMSG extends Message {

	String ime;
	String text;

	/**
	 * Konstruktor, stvara poruku INMSG.
	 * @param number
	 * @param ime
	 * @param text
	 */
	public INMSG(long number, String ime, String text) {
		super.setKod((byte) 5);
		super.setRedniBrojPaketa(number);
		this.ime = ime;
		this.text = text;
	}

	/**
	 * Getter za ime.
	 * @return
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * Getter za text.
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * Metoda koja stvara polje bajtova iz svih podataka poruke INMSG.
	 * 
	 */
	@Override
	public byte[] createPackageData() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);

		try {
			dos.writeByte(this.kod);
			dos.writeLong((long) this.redniBrojPaketa);
			dos.writeUTF(ime);
			dos.writeUTF(text);
			dos.close();
			return bos.toByteArray();
		} catch(IOException e) {
			System.out.println("Error while creating UDP package content.");
			return null;
		}
	}
}
