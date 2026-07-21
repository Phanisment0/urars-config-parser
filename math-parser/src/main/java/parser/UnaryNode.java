package parser;

import lexer.MathLexer.Token;

public record UnaryNode(Token operator, Node value) implements Node {
}
