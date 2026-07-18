package tokenize;

public final class Lexer {
	private byte[] buffer;
	public int pos, start, end = 0;

	// private int line;

	public Lexer(byte[] buffer) {
		this.buffer = buffer;
	}

	private boolean isWhitespace(byte c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	private void skipWhitespace() {
		while (peek() != -1 && isWhitespace(peek())) pos++;
	}

	private byte skip() {
		if (pos >= buffer.length) return -1;
		return buffer[pos++];
	}

	private byte peek() {
		if (pos >= buffer.length) return -1;
		return buffer[pos];
	}

	public Token next() {
		skipWhitespace();

		byte c = peek();

		if (c == -1) return Token.EOF;

		switch (c) {
			case '{' -> {
				start = pos;
				skip();
				end = pos;
				return Token.L_BRACKET;
			}
			case '}' -> {
				start = pos;
				skip();
				end = pos;
				return Token.R_BRACKET;
			}
			case '#' -> {
				start = pos;
				while (pos < buffer.length && peek() != '\n' && peek() != '\r') skip();
				end = pos;
			}
		}

		start = pos;
		int current;
		while ((current = peek()) != -1 && current != '{' && current != '}' && current != '#') skip();
		end = pos;

		while (end > start && isWhitespace(buffer[end - 1])) end--;
		if (start == end) return next();
		return Token.TEXT;
	}

	public void reset(byte[] new_buffer) {
		this.buffer = new_buffer;
		this.pos = 0;
		this.start = 0;
		this.end = 0;
	}
}