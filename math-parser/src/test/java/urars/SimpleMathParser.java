package urars;

public class SimpleMathParser {
	public static double eval(final String str) {
		return new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) throw new RuntimeException("Karakter tidak dikenal: " + (char)ch);
				return x;
			}

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if      (eat('+')) x += parseTerm(); // Penjumlahan
					else if (eat('-')) x -= parseTerm(); // Pengurangan
					else return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if      (eat('*')) x *= parseFactor(); // Perkalian
					else if (eat('/')) x /= parseFactor(); // Pembagian
					else return x;
				}
			}

			double parseFactor() {
				if (eat('+')) return parseFactor(); 
				if (eat('-')) return -parseFactor(); 

				double x;
				int startPos = this.pos;
				if (eat('(')) { // Kurung buka
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // Angka
					while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else {
					throw new RuntimeException("Karakter tak terduga: " + (char)ch);
				}

				return x;
			}
		}.parse();
	}
}