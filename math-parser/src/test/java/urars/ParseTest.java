package urars;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import eval.MathEval;
import parser.MathParser;

class ParseTest {

	@Test
	public void parse() throws ParseException {
		byte[] input = """
		10 * 1 + 10
		""".getBytes();
		var p = new MathParser(input);
		System.out.println(MathEval.eval(p.parse()));
	}
}
