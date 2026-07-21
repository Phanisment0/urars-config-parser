package parser;

import java.text.ParseException;

import lexer.MathLexer;
import lexer.MathLexer.Token;
import util.BufferedString;

public class MathParser {
	private final MathLexer lexer;
	private int depth;

	private void trace(String text) {
		for (int i = 0; i < depth; i++) System.out.print("│ ");
		System.out.println(text);
	}

	public MathParser(byte[] input) {
		this(new BufferedString(input, 0, input.length));
	}

	public MathParser(BufferedString input) {
		this.lexer = new MathLexer(input);
	}

	public Node parse() throws ParseException {
		Node node = parseExpression(0);
		if (lexer.next() != Token.EOF) throw new ParseException("Unexpected token", 0);
		return node;
	}

	private int bindingPower(Token token) {
	return switch (token) {
		case PLUS -> 10;
		case MULTIPLY -> 20;
		default -> -1;
	};
}

	public Node parseExpression(int minBP) throws ParseException {
		trace("parseExpression(" + minBP + ")");
		depth++;
		Node left = parseNumber();
		while (true) {
			Token operator = lexer.lookahead();
			int bp = bindingPower(operator);
			trace("lookahead = " + operator + " (bp=" + bp + ")");
			if (bp < minBP) break;
			lexer.next();
			trace("enter right");
			Node right = parseExpression(bp + 1);
			trace("right parsed");
			left = new BinaryNode(left, operator, right);
		}
		depth--;
		trace("return");
		return left;
	}

	private Node parseNumber() throws ParseException {
		if (lexer.next() != Token.NUMBER) throw new ParseException("Expected number", 0);
		double value = parseDouble(lexer.token());
		trace("number = " + value);
		return new NumberNode(value);
	}

	public static double parseDouble(BufferedString input) {
		double value = 0;

		int pos = input.start;
		int end = input.end;

		while (pos < end) {
			int c = input.buffer[pos] & 0xFF;
			if (c == '.') break;
			value = value * 10 + (c - '0');
			pos++;
		}
		if (pos == end) return value;
		pos++;
		double decimal = 0.1;
		while (pos < end) {
			int c = input.buffer[pos] & 0xFF;
			value += (c - '0') * decimal;
			decimal *= 0.1;
			pos++;
		}
		return value;
	}
}