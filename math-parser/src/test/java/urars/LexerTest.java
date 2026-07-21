package urars;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import lexer.MathLexer;
import lexer.MathLexer.Token;
import util.BufferedString;

class LexerTest {
	@Test
	void testLexer() throws ParseException {
		byte[] input = """
		10.2 + 1
		""".getBytes();
		var l = new MathLexer(new BufferedString(input, 0, input.length));
		
		assertEquals(Token.NUMBER, l.next());
		System.out.println(l.token());

		assertEquals(Token.PLUS, l.next());
		System.out.println(l.token());

		assertEquals(Token.NUMBER, l.next());
		System.out.println(l.token());
	}
}