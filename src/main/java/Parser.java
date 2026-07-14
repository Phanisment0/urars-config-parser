import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class Parser {
	private final char[] buffer;
	private final int length;

	private int pos = 0;

	public Parser(char[] buffer, int length) {
		this.buffer = buffer;
		this.length = length;
	}
	
	private int next() {
		if (pos >= length) return -1;	
		return buffer[pos++];
	}

	private int peek() {
		if (pos >= length) return -1;
		return buffer[pos];
	}

	private void expect(char expected) throws ParseException {
		int c = next(); 
		if (c == -1) throw new ParseException("Expected '" + expected + "' but reached EOF", pos);
		if (c != expected) throw new ParseException("Expected '" + expected + "' but got '" + (char)c + "'", pos);
	}

	private void skipWhitespace() {
		while (peek() != -1 && Character.isWhitespace(peek())) this.next();
	}

	public List<Entry> parse() throws ParseException {
		List<Entry> result = new ArrayList<>();

		while (peek() != -1) {
			skipWhitespace();

			if (peek() == -1) break;

			BufferedString key = this.parseText();

			skipWhitespace();
			
			if (key.length() == 0) {
				skipWhitespace();
				if (peek() == -1) break;
				throw new ParseException("Error: Key cant be empty", pos);
			}
			
			skipWhitespace();
			expect('{');

			BufferedString value = this.parseValue();
			
			result.add(new Entry(key, value));
		}
		return result;
	}

	public BufferedString parseText() {
		int start = pos;
		while (true) {
			int c = peek();
			if (c == -1) break;
			if (Character.isWhitespace(c)) break;
			if (c == '{' || c == '}') break;
			next();
		}
		return new BufferedString(buffer, start, pos);
	}

	public BufferedString parseValue() throws ParseException {
		int start = pos;
		int depth = 1;
		while (depth > 0) {
			int c = next();
			if (c == -1) throw new ParseException("Error: Curly bracket is not closed", pos);

			if (c == '{') depth++;
			else if (c == '}') depth--;
		}
		return new BufferedString(buffer, start, pos - 1);
	}
}