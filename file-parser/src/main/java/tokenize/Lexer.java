package tokenize;

import util.BufferedString;
import util.ParserUtils;

public final class Lexer {
	private byte[] buffer;
	public int pos = 0, start = 0, end = 0;

	// private int line = 1, colum = 0;

	public Lexer(byte[] buffer) {
		this.buffer = buffer;
	}

	private void skipWhitespace() {
		while (peek() != -1 && ParserUtils.isWhitespace(peek())) pos++;
	}

	public static boolean isSytax(int c) {
		return c == '{'
				|| c == '}'
				|| c == '='
				|| c == '#';
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
			case '=' -> {
				start = pos;
				skip();
				end = pos;
				return Token.EQUAL;
			}
			case '#' -> {
				start = pos;
				while (pos < buffer.length && peek() != '\n' && peek() != '\r') skip();
				end = pos;
			}
		}

		start = pos;
		int current;
		while ((current = peek()) != -1 && !isSytax(current)) skip();
		end = pos;

		while (end > start && ParserUtils.isWhitespace(buffer[end - 1])) end--;
		if (start == end) return next();
		return Token.TEXT;
	}

	public BufferedString token() {
		return new BufferedString(buffer, start, end);
	}

	public void reset(byte[] new_buffer) {
		this.buffer = new_buffer;
		this.pos = 0;
		this.start = 0;
		this.end = 0;
	}
}