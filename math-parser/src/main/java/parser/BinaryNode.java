package parser;

import lexer.MathLexer.Token;

public record BinaryNode(Node left, Token operator, Node right) implements Node {
}
