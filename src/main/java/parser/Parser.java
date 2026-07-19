package parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import tokenize.Lexer;
import tokenize.Token;
import util.BufferedString;
import util.ParserUtils;

public class Parser {
	private final Lexer lexer;
	private byte[] buffer;

	public Parser(File file) throws IOException {
		this(file.toPath());
	}

	public Parser(Path path) throws IOException {
		this(Files.readAllBytes(path));
	}

	public Parser(String string) {
		this(string.getBytes(StandardCharsets.UTF_8));
	}

	public Parser(byte[] buffer) {
		this.buffer = buffer;
		this.lexer = new Lexer(buffer);
	}

	public List<Entry> parse() throws ParseException {
		List<Entry> list = new ArrayList<>();

		while (true) {
			Token token = lexer.next();
			if (token == Token.EOF) break;
			if (token != Token.TEXT) throw new ParseException("Expected Header", lexer.pos);

			BufferedString header = token();

			token = lexer.next();
			if (token == Token.L_BRACKET) {
				BufferedString value = parseBlock();
				list.add(new Entry(header, value));
			} else if (token == Token.EQUAL) {
				BufferedString value = parseField();
				list.add(new Entry(header, value));
			} else throw new ParseException("Expected '=' or '{'", lexer.pos);
		}
		return list;
	}

	/*
	 * TEXT EQUAL TEXT
	 */
	private BufferedString parseField() {
		int start = lexer.end, end = lexer.end;
		while (end < buffer.length) {
			int c = buffer[end] & 0xFF;
			if (ParserUtils.isNewLine(c) || Lexer.isSytax(c)) break;
			end++;
		}

		lexer.pos = end + 1;
		return new BufferedString(buffer, start, end);
	}

	/**
	 * L_BRACKET ... R_BRACKET
	 */
	private BufferedString parseBlock() throws ParseException {
		int start = lexer.pos;
		int depth = 1;
		while (depth > 0) {
			Token token = lexer.next();
			if (token == Token.EOF) throw new ParseException("Missing '}'", lexer.pos);
			if (token == Token.L_BRACKET) depth++;
			else if (token == Token.R_BRACKET) depth--;
		}
		return new BufferedString(buffer, start, lexer.start);
	}

	public BufferedString token() {
		return new BufferedString(buffer, lexer.start, lexer.end);
	}

	public void reset(byte[] new_buffer) {
		this.lexer.reset(new_buffer);
		this.buffer = new_buffer;
	}
}
