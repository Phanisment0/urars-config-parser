package tokenize;

import util.BufferedString;
import util.ParserUtils;

public final class Lexer {
	private byte[] buffer;
	public int pos = 0, start = 0, end = 0;

	public Lexer(byte[] buffer) {
		this.buffer = buffer;
	}

	public static boolean isSytax(int c) {
		return c == '{'
				|| c == '}'
				|| c == '='
				|| c == '#';
	}

	public static boolean isWhitespace(int c) {
		return c == ' '
				|| c == '\t'
				|| c == '\r';
	}

	public static boolean isNewLine(int c) {
		return c == '\n'
				|| c == '\r';
	}

	private void skipWhitespace() {
		while (peek() != -1 && isWhitespace(peek())) pos++;
	}

	private int skip() {
		if (pos >= buffer.length) return -1;
		return buffer[pos++] & 0xFF;
	}

	private int peek() {
		if (pos >= buffer.length) return -1;
		return buffer[pos] & 0xFF;
	}

	public Token lookahead() {
		int pos = this.pos, start = this.start, end = this.end;
		
		Token token = next();
		
		this.pos = pos;
		this.start = start;
		this.end = end;
		return token;
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
			case '\n' -> {
				start = pos;
				skip();
				end = pos;
				return Token.NEW_LINE;
			}
			case '#' -> { // Commnet will not represent as token
				while (peek() != -1 && !ParserUtils.isNewLine(peek())) skip();
				return next();
			}
		}

		start = pos;
		int current;
		while ((current = peek()) != -1 && !isSytax(current) && !isWhitespace(current) && current != '\n') skip();
		end = pos;
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

	public static enum Token {
		L_BRACKET,
		R_BRACKET,
		NEW_LINE,
		EQUAL,
		TEXT,
		EOF
	}
}