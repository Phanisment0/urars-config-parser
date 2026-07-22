package eval;

import parser.MathParser.BinaryNode;
import parser.MathParser.Node;
import parser.MathParser.NumberNode;
import parser.MathParser.StringNode;
import util.BufferedString;

public class MathEval {
	public static Object eval(Node node) {
		if (node instanceof NumberNode number) return number.value();
		if (node instanceof StringNode string) return string.value();
		if (node instanceof BinaryNode binary) return evalBinary(binary);
		throw new IllegalArgumentException();
	}

	private static Object evalBinary(BinaryNode node) {
		Object left = eval(node.left());
		Object right = eval(node.right());
		return switch (node.op()) {
			case ADD -> {
				if (left instanceof BufferedString || right instanceof BufferedString) yield left.toString() + right.toString();
				yield (double)left + (double)right;
			}
			case MIN    -> (double)left - (double)right;
			case MUL -> (double)left * (double)right;
			case DIV   -> (double)left / (double)right;
			case MOD   -> (double)left % (double)right;
			default       -> throw new IllegalArgumentException();
		};
	}
}