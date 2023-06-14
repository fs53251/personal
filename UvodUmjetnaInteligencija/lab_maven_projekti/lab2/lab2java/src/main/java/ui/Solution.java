package ui;

public class Solution {
	public static void main(String... args) {
		String algorithm = args[0];
		switch(algorithm) {
			case "resolution":
				RezolucijaOpovrgavanjem.pokreniAlgoritam(args[1]);
				break;
			case "cooking":
				RezolucijaOpovrgavanjem.pokreniAlgoritam(args[1], args[2]);
				break;
			default:
				break;
		}

	}
}
