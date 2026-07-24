package urars;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import io.phanisment.urars.lib.file_parser.UrArsFileParser;
import io.phanisment.urars.lib.file_parser.config.ConfigSection;
import io.phanisment.urars.lib.file_parser.util.BufferedString;

class ApiTest {
	@Test
	void test() throws ParseException {
		ConfigSection a = (ConfigSection)UrArsFileParser.loadString("""
		test {
			- test{} @self
			- ok{} ok
		}""");
		var b = a.getLine("test");
		System.out.println(b);
	}

	@Test
	void blankBUffer() {
		var a = new BufferedString("      ");
		assertEquals(true, a.isBlank());
	}
}
