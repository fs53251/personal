package hr.fer.zemris.scripting.lexer;

import hr.fer.zemris.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.scripting.elems.ElementFunction;
import hr.fer.zemris.scripting.elems.ElementOperator;
import hr.fer.zemris.scripting.elems.ElementString;
import hr.fer.zemris.scripting.elems.ElementVariable;
import hr.fer.zemris.scripting.elems.ElementsConstantInteger;
import hr.fer.zemris.scripting.parser.SmartScriptingParserException;

/**
 * Razred <code>Lexer1</code> predstavlja leksički analizator.
 * Nad primljenim tekstom, radi grupiranje i
 * vraća leksičke jedinke (Tokene). 
 *
 * @author Filip
 *
 */
public class Lexer1 {
	/**
	 * Polje koje čuva cijeli ulaz leksičkog analizatora.
	 */
	private char[] data;

	/**
	 * Varijabla koja čuva leksičku jedinku (Token)
	 */
	private Token1 token;

	/**
	 * Cijeli broj koji prati početak neobrađenog ulaznog niza.
	 */
	private int currentIndex;

	/**
	 * Inicijalno stanje leksičkog analizatora.
	 */
	private LexerState1 l = LexerState1.BASIC;

	/**
	 * Konstruktor
	 * 
	 * @param text ulaz leksičkog analizatora
	 */
	public Lexer1(String text) {
		this.data = text.toCharArray();
	}

	/**
	 * Metoda kojom određujemo stanje leksičkog analizatora.
	 * 
	 * @param state stanje na koje postavljamo lexer.
	 * @throws NullPointerException vraća <code>null</code> ako je predano stanje null
	 */
	public void setState(LexerState1 state) {
		if(state == null)
			throw new NullPointerException();
		this.l = state;
	}

	/**
	 * Metoda koja vraća trenutni token koji je analiziran.
	 * Ne provodi novu analizu!
	 * 
	 * @return Token analizirani token
	 */
	public Token1 getToken() {
		return this.token;
	}

	/**
	 * Metoda koja grupira daljnje znakove ulaznog niza i stvara token (leksičku jedinku).
	 * 
	 * @return Token vraća novi token na temelju zakova ulaza
	 * @throws SmartScriptingParserException ako je na ulazu neispravna leksička jedinka.
	 * 			Zbog naputka u zadatku je ova iznimka, a nije LexerException koja bi bila prikladnija.
	 */
	public Token1 nextToken() {

		if(this.token != null && token.getType() == TokenType1.EOF) {
			throw new SmartScriptingParserException();
		}

		if(currentIndex >= data.length) {
			this.token = new Token1(TokenType1.EOF, null);
			return this.token;
		}

		if(data[currentIndex] == '{' && data[currentIndex + 1] == '$') {
			this.setState(l.EXTENDED);
			currentIndex += 2;
		}

		//Basic text
		if(l == LexerState1.BASIC) {
			StringBuilder sb = new StringBuilder();
			do {

				if(currentIndex + 1 < data.length && data[currentIndex] == '\\') {
					if(data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '{') {
						currentIndex++;
					} else {
						throw new SmartScriptingParserException();
					}
				} else if(currentIndex + 1 < data.length && data[currentIndex] == '{' && data[currentIndex + 1] == '$') {
					currentIndex += 2;
					this.l = LexerState1.EXTENDED;
					return new Token1(TokenType1.STRING, new ElementString(sb.toString()));
				}

				sb.append(data[currentIndex]);
				currentIndex++;
			}
			while(currentIndex < data.length);
			return new Token1(TokenType1.STRING, new ElementString(sb.toString()));

		} else { //Extended tags
			skipBlanksInTAGS();

			if(currentIndex + 1 < data.length && data[currentIndex] == '$' && data[currentIndex + 1] == '}') {
				currentIndex += 2;
				this.l = LexerState1.BASIC;
				return new Token1(TokenType1.STRING, new ElementString("$}"));
			}

			//FOR
			if(currentIndex + 2 < data.length && (new String(data, currentIndex, 3)).toLowerCase().compareTo("for") == 0) {
				currentIndex += 3;
				return new Token1(TokenType1.TAG, new ElementString("FOR"));
			}

			//END
			if(currentIndex + 2 < data.length && (new String(data, currentIndex, 3)).toLowerCase().compareTo("end") == 0) {
				currentIndex += 3;
				return new Token1(TokenType1.TAG, new ElementString("END"));
			}

			//"="
			if(data[currentIndex] == '=') {
				currentIndex++;
				return new Token1(TokenType1.TAG, new ElementString("="));
			}

			//operator
			if("+-*/^".indexOf(data[currentIndex]) != -1) {
				if(!(data[currentIndex] == '-' && currentIndex + 1 < data.length && Character.isDigit(data[currentIndex + 1]))) {

					return new Token1(TokenType1.OPERATOR,
							new ElementOperator(Character.toString(data[currentIndex++])));
				}
			}

			//function name
			if(currentIndex + 1 < data.length && data[currentIndex] == '@' && Character.isLetter(data[currentIndex + 1])) {
				currentIndex++;
				StringBuilder sb = new StringBuilder();
				while(currentIndex < data.length && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
					sb.append(data[currentIndex++]);
				}
				return new Token1(TokenType1.FUNKCIJA, new ElementFunction(sb.toString()));
			}

			//constant double && constant int
			if(Character.isDigit(data[currentIndex]) || data[currentIndex] == '-') {
				StringBuilder sb = new StringBuilder();
				sb.append(data[currentIndex++]);
				while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
					sb.append(data[currentIndex++]);
				}

				if(data[currentIndex] == '.') {
					if(currentIndex + 1 < data.length && Character.isDigit(data[currentIndex + 1])) {
						sb.append('.');
						currentIndex++;
						while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
							sb.append(data[currentIndex++]);
						}
						return new Token1(TokenType1.CONSTANTDOUBLE,
								new ElementConstantDouble(Double.valueOf(sb.toString())));
					} else {
						throw new SmartScriptingParserException();
					}
				} else {
					return new Token1(TokenType1.CONSTANTINTEGER,
							new ElementsConstantInteger(Integer.valueOf(sb.toString())));
				}
			}

			//variable
			if(Character.isLetter(data[currentIndex])) {
				StringBuilder sb = new StringBuilder();
				sb.append(data[currentIndex++]);
				while(currentIndex < data.length && (Character.isLetter(data[currentIndex]) || Character.isDigit(data[currentIndex]) || data[currentIndex] == '_')) {
					sb.append(data[currentIndex++]);
				}
				return new Token1(TokenType1.VARIJABLA, new ElementVariable(sb.toString()));
			}

			//string
			if(data[currentIndex] == '"') {
				StringBuilder sb = new StringBuilder();
				currentIndex++;
				while(currentIndex < data.length && data[currentIndex] != '"') {

					if(currentIndex + 1 < data.length && data[currentIndex] == '\\') {
						if(data[currentIndex + 1] == '\\' || data[currentIndex + 1] == '"') {
							currentIndex++;
						} else if("nrt".indexOf(data[currentIndex + 1]) != -1) {
							if(data[currentIndex + 1] == 'n')
								sb.append('\n');
							if(data[currentIndex + 1] == 'r')
								sb.append('\r');
							if(data[currentIndex + 1] == 't')
								sb.append('\r');
							currentIndex += 2;
							continue;
						} else {
							throw new SmartScriptingParserException();
						}
					}
					sb.append(data[currentIndex++]);
				}
				currentIndex++;
				return new Token1(TokenType1.STRING, new ElementString(sb.toString()));
			}
			throw new SmartScriptingParserException();
		}
	}

	/**
	 * Metoda kojom preskačemo prazna mjesta, tabulatore, prelaske u novi red.
	 */
	private void skipBlanksInTAGS() {
		while(currentIndex < data.length) {
			char c = data[currentIndex];
			if(c == ' ' || c == '\t' || c == '\r' || c == '\n') {
				currentIndex++;
				continue;
			}
			break;
		}
	}
}
