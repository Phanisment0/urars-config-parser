package parser.line;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import util.BufferedString;
import util.ParserUtils;

/**
 * IDENTIFIER{KEY=VALUE;KEY=VALUE} CONTEXT
 */
public class LineParser {
	private final BufferedString line;

	private BufferedString key;
	private List<BufferedString> arguments;
	private BufferedString context;

	private int pos;
	private int start, end = 0;

	public LineParser(BufferedString line) {
		this.line = line;
		this.pos = line.start;
	}

	private int next() {
		if (pos >= line.length) return -1;
		return line.buffer[pos++] & 0xFF;
	}

	private int peek() {
		if (pos >= line.length) return -1;
		return line.buffer[pos] & 0xFF;
	}

	private void skipWhitespace() {
		while (peek() != -1 && ParserUtils.isWhitespace(peek())) next();
	}

	public void parse() throws ParseException {
		skipWhitespace();
		if (peek() == -1) return;

			// Key
		start = pos;
		while (peek() != -1 && peek() != '{' && !ParserUtils.isWhitespace(peek())) next();
		end = pos;

		key = token();

		skipWhitespace();
		if (peek() == -1) return;

		// Arguments
		arguments = new ArrayList<>();
		if (peek() == '{') {
			next();
			start = pos;
			int depth = 1;
			while (depth > 0) {
				int c = next();
				if (c == -1) throw new ParseException("Missing '}'", pos);
				if (c == '{') depth++;
				else if (c == '}') depth--;
			}
			end = pos;

			split(start, end - 1);
		}

		// Context
		context = new BufferedString(line.buffer, end, line.end);
	}

	private void split(int start, int end) {
		int depth = 0;
		for (int i = start; i < end; i++) {
			int c = line.buffer[i] & 0xFF;
			if (c == '{' || c == '[') depth++;
			else if (c == '}' || c == ']') depth--;
			else if (c == ';' && depth == 0) {
				arguments.add(new BufferedString(line.buffer, start, i));
				start = i + 1;
			}
		}
		if (start < end) arguments.add(new BufferedString(line.buffer, start, end));
	}

	private BufferedString token() {
		return new BufferedString(line.buffer, start, end);
	}

	@Override
	public String toString() {
		return key + " " + arguments + " " + context;
	}
}