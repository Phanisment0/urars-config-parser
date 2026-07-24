package urars;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.phanisment.urars.lib.file_parser.tokenize.Lexer;
import io.phanisment.urars.lib.file_parser.tokenize.Lexer.Token;

class LexerTest {
	@Test
	public void realTest() {
		byte[] data = """
		this is header {
			this is example {
				and this is bracket inside bracket
			}
		}""".getBytes();
		var lexer = new Lexer(data);

		Token token;
		while ((token = lexer.next()) != Token.EOF) System.out.println(token);
	}

	@Test
	public void tokenizeBlock() {
		byte[] data = "this is header {\n\n}".getBytes();
		var lexer = new Lexer(data);

		assertEquals(Token.TEXT, lexer.next());
		assertEquals(Token.TEXT, lexer.next());
		assertEquals(Token.TEXT, lexer.next());

		assertEquals(Token.L_BRACKET, lexer.next());
		assertEquals(Token.NEW_LINE, lexer.next());
		assertEquals(Token.NEW_LINE, lexer.next());
		assertEquals(Token.R_BRACKET, lexer.next());
	}

	@Test
	public void tokenizeField() {
		byte[] data = "key = value\n".getBytes();
		var lexer = new Lexer(data);

		assertEquals(Token.TEXT, lexer.next());
		assertEquals(Token.EQUAL, lexer.next());
		assertEquals(Token.TEXT, lexer.next());
		assertEquals(Token.NEW_LINE, lexer.next());
		
		assertEquals(Token.EOF, lexer.next());
	}
}