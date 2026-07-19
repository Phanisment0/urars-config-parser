package structure;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import parser.Parser;

class EntryTest {
	@Test
	public void parsingAll() throws ParseException {
		byte[] data = """
			field =value

			key {
			test
			}""".getBytes();
		var parser = new Parser(data);
		System.out.println(parser.parse());
	}
}