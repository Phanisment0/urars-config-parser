package urars.structure;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import io.phanisment.urars.lib.file_parser.parser.LineParser;
import io.phanisment.urars.lib.file_parser.util.BufferedString;

class LineTest {
	@Test
	void lineParse() throws ParseException {
		byte[] raw_line = """
			key{c=[
				foo{ok=lol}
				uhh
			]} ctx
		""".getBytes();
		var line = new LineParser(new BufferedString(raw_line, 0, raw_line.length));
		line.parse();
		var cfg = line.config();

		System.out.println(cfg.getLine("c"));
	}
}
