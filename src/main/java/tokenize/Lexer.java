package tokenize;

import util.ParserUtils;

public final class Lexer {
	private byte[] buffer;
	public int pos, start, end = 0;

	// private int line;

	public Lexer(byte[] buffer) {
		this.buffer = buffer;
	}

	private void skipWhitespace() {
		while (peek() != -1 && ParserUtils.isWhitespace(peek())) pos++;
	}

	private int skip() {
		if (pos >= buffer.length) return -1;
		return buffer[pos++] & 0xFF;
	}

	private int peek() {
		if (pos >= buffer.length) return -1;
		return buffer[pos] & 0xFF;
	}

	public Token next() {
		skipWhitespace();

		int c = peek();

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
		while ((current = peek()) != -1 && current != '{' && current != '#') skip();
		end = pos;

		while (end > start && ParserUtils.isWhitespace(buffer[end - 1])) end--;
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