package utils;

public class Converter {

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();

		for(byte b : bytes) {
			sb.append(String.format("%02X", b));
		}

		return sb.toString();
	}
}
