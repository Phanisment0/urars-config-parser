package lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import tokenize.Lexer;
import tokenize.Token;

class LexerTest {
	@Test
	public void tokenizeField() {
		byte[] data = "key = value\n".getBytes();
		var lexer = new Lexer(data);

		assertEquals(Token.TEXT, lexer.next());
		assertEquals(Token.EQUAL, lexer.next());
		assertEquals(Token.TEXT, lexer.next());
		
		assertEquals(Token.EOF, lexer.next());
	}
}