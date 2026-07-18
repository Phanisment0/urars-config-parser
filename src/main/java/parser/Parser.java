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
			if (token != Token.L_BRACKET) throw new ParseException("Expected {", lexer.pos);
			BufferedString value = parseValue();
			list.add(new Entry(header, value));
		}
		return list;
	}

	private BufferedString parseValue() throws ParseException {
		int start = lexer.pos;
		int depth = 1;
		while (depth > 0) {
			Token token = lexer.next();
			switch (token) {
				case L_BRACKET -> depth++;
				case R_BRACKET -> depth--;
				case EOF -> throw new ParseException("Unexpected EOF", lexer.pos);
				default -> {}
			}
		}
		return new BufferedString(buffer, start, lexer.start);
	}

	private BufferedString token() {
		return new BufferedString(buffer, lexer.start, lexer.end);
	}
}
