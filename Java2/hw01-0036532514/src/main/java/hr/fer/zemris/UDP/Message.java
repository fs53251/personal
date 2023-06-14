package hr.fer.zemris.UDP;

/**
 * Abstract razred koji predstavlja poruku. On je apstrakcija jer mi ne trebaju njegove instance,
 * a vazno mi je da svaka poruka ima implementiranu metodu createPackageData() i svaka poruka 
 * ima kod i redni broj paketa. Stoga su svi ti podaci ovdje izvojeni u Message.
 * @author Filip
 *
 */
public abstract class Message {

	byte kod;
	long redniBrojPaketa;

	public abstract byte[] createPackageData();

	public byte getKod() {
		return kod;
	}

	public void setKod(byte kod) {
		this.kod = kod;
	}

	public long getRedniBrojPaketa() {
		return redniBrojPaketa;
	}

	public void setRedniBrojPaketa(long broj) {
		this.redniBrojPaketa = broj;
	}
}
