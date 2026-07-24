package lexer;

import java.text.ParseException;

import util.BufferedString;

public class MathLexer {
	private final BufferedString input;
	private int pos, start, end;

	public MathLexer(BufferedString input) {
		this.input = input;
		this.pos = input.start;
	}

	private int peek() {
		if (pos >= input.end) return -1;
		return input.buffer[pos] & 0xFF;
	}

	private int skip() {
		if (pos >= input.end) return -1;
		return input.buffer[pos++] & 0xFF;
	}

	private void skipWhitespace() {
		while (peek() != -1 && isWhitespace(peek())) skip();
	}

	private boolean isWhitespace(int c) {
		return c == ' '
				|| c == '\t'
				|| c == '\n'
				|| c == '\r';
	}

	public Token lookahead() throws ParseException {
		int pos = this.pos, start = this.start, end = this.end;

		Token token = next();
		this.pos = pos;
		this.start = start;
		this.end = end;
		return token;
	}

	private boolean isDigit(int c) {
		return c >= '0' && c <= '9';
	}

	public Token next() throws ParseException {
		skipWhitespace();

		int c = peek();
		if (c == -1) return Token.EOF;

		switch (c) {
			case '+' -> {
				start = pos;
				skip();
				end = pos;
				return Token.ADD;
			} 
			case '-' -> {
				start = pos;
				skip();
				end = pos;
				return Token.MIN;
			}
			case '*' -> {
				start = pos;
				skip();
				end = pos;
				return Token.MUL;
			}
			case '/' -> {
				start = pos;
				skip();
				end = pos;
				return Token.DIV;
			}
			case '%' -> {
				start = pos;
				skip();
				end = pos;
				return Token.MOD;
			}
			case '.' -> {
				start = pos;
				skip();
				end = pos;
				return Token.DOT;
			}
			case '(' -> {
				start = pos;
				skip();
				end = pos;
				return Token.L_PAR;
			}
			case ')' -> {
				start = pos;
				skip();
				end = pos;
				return Token.R_PAR;
			}
			case '"' -> {
				skip();
				start = pos;
				while (peek() != -1 && peek() != '"') skip();
				end = pos;
				skip();
				return Token.STR;
			}
		}

		if (isDigit(c) || c == '.') {
			start = pos;
			boolean dot = false;

			while ((c = peek()) != -1) {
				if (isDigit(c)) {
					skip();
					continue;
				}
				if (c == '.' && !dot) {
					dot = true;
					skip();
					continue;
				}
				break;
			}
			end = pos;
			return Token.NUM;
		}

		throw new ParseException("Unexpected character '" + (char)c + "'", pos);
	}

	public BufferedString token() {
		return new BufferedString(input.buffer, start, end);
	}

	public static enum Token {
		STR(null),
		NUM(null),
		ADD("+"),
		MIN("-"),
		MUL("*"),
		DIV("/"),
		MOD("%"),
		DOT("."),
		L_PAR("("),
		R_PAR(")"),
		EOF(null);

		String alias;
		Token(String alias) {
			this.alias = alias;
		}

		public String alias() {
			return alias != null ? alias : name();
		}
	}
}