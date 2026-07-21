package eval;

import parser.BinaryNode;
import parser.Node;
import parser.NumberNode;

public class MathEval {
	public static double eval(Node node) {
		if (node instanceof NumberNode number) return number.value();
		if (node instanceof BinaryNode binary) return evalBinary(binary);
		throw new IllegalArgumentException();
	}

	private static double evalBinary(BinaryNode node) {
		double left = eval(node.left());
		double right = eval(node.right());
		return switch (node.operator()) {
			case PLUS -> left + right;
			case MULTIPLY -> left * right;
			default -> throw new IllegalArgumentException();
		};
	}
}
