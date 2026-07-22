package parser;

import java.text.ParseException;

import lexer.MathLexer;
import lexer.MathLexer.Token;
import util.BufferedString;

public class MathParser {
	private final MathLexer lexer;

	public MathParser(String input) {
		this(input.getBytes());
	}

	public MathParser(byte[] input) {
		this(new BufferedString(input, 0, input.length));
	}

	public MathParser(BufferedString input) {
		this.lexer = new MathLexer(input);
	}

	public Node parse() throws ParseException {
		Node node = expression(0);
		if (lexer.next() != Token.EOF) throw new ParseException("Unexpected token", 0);
		return node;
	}

	private void expect(Token expect) throws ParseException {
		Token actual = lexer.next();
		if (actual != expect) throw new ParseException("Expected '" + expect + "' but found '" + actual + "'", 0);
	}

	private double binding(Token token) {
		return switch (token) {
			case ADD, MIN -> 1.0;
			case MUL, DIV, MOD -> 2.0;
			default -> -1.0;
		};
	}

	private Node prefix() throws ParseException {
		Token token = lexer.next();
		return switch (token) {
			case NUM -> new NumberNode(lexer.token().toDouble());
			case STR -> new StringNode(lexer.token());
			case L_PAR -> {
				Node node = expression(0);
				expect(Token.R_PAR);
				yield node;
			}
			default -> throw new ParseException("Unexpected token " + token, 0);
		};
	}

	public Node expression(double limit) throws ParseException {
		Node left = prefix();
		while (true) {
			Token operator = lexer.lookahead();
			double bp = binding(operator);
			if (bp < limit) break;

			lexer.next();

			Node right = expression(bp + 0.1);
			left = new BinaryNode(left, operator, right);
		}
		return left;
	}

	public static interface Node {}

	public static record StringNode(BufferedString value) implements Node {
		@Override
		public String toString() {
			return value.toString();
		}
	}
	public static record NumberNode(double value) implements Node {
		@Override
		public String toString() {
			return String.valueOf(value);
		}
	}
	public static record BinaryNode(Node left, Token op, Node right) implements Node {
		@Override
		public String toString() {
			return "(" + left + " " + op.alias() + " " + right + ")";
		}
	}
}