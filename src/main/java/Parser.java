import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public final class Parser {
	private static final char L_BLOCK = '{';
	private static final char R_BLOCK = '}';

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

	private int expect(char expected) throws ParseException {
		int c = next(); 
		if (c == -1) throw new ParseException("Expected '" + expected + "' but reached EOF", pos);
		if (c != expected) throw new ParseException("Expected '" + expected + "' but got '" + (char)c + "'", pos);
		return c;
	}

	private void skipWhitespace() {
		while (peek() != -1 && Character.isWhitespace(peek())) this.next();
	}

	public List<Entry> parse() throws ParseException {
		List<Entry> result = new ArrayList<>();

		while (peek() != -1) {
			skipWhitespace();

			if (peek() == -1) break;

			BufferedString header = this.parseHeader();

			skipWhitespace();
			
			if (header.length() == 0) {
				skipWhitespace();
				if (peek() == -1) break;
				throw new ParseException("Error: Header cant be empty", pos);
			}
			
			skipWhitespace();
			expect(L_BLOCK);

			BufferedString value = this.parseValue();
			
			result.add(new Entry(header, value));
		}
		return result;
	}

	private BufferedString parseHeader() {
		int start = pos;
		int c;
		while ((c = peek()) != -1) {
			if (c == L_BLOCK || c == R_BLOCK) break;
			next();
		}
		int end = pos;
		while (end > start && Character.isWhitespace(buffer[end - 1])) end--;
		return new BufferedString(buffer, start, end);
	}

	private BufferedString parseValue() throws ParseException {
		int start = pos;
		int depth = 1;
		while (depth > 0) {
			int c = next();
			if (c == -1) throw new ParseException("Error: Curly bracket is not closed", pos);
			if (c == L_BLOCK) depth++;
			else if (c == R_BLOCK) depth--;
		}
		return new BufferedString(buffer, start, pos - 1);
	}
}