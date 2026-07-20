package structure;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import parser.line.LineParser;
import util.BufferedString;

class LineTest {
	@Test
	void lineParse() throws ParseException {
		byte[] raw_line = """
			key{test=a;ok=1} ctx
		""".getBytes();
		var line = new LineParser(new BufferedString(raw_line, 0, raw_line.length));
		line.parse();

		System.out.println(line);
	}
}
