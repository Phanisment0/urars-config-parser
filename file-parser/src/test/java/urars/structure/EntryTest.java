package urars.structure;

import java.text.ParseException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import io.phanisment.urars.lib.file_parser.parser.Parser;

class EntryTest {
	@Test
	public void parsingAll() throws ParseException {
		byte[] data = "key { block example } # Comment\n".getBytes();
		var parser = new Parser(data);
		System.out.println(parser.parse());
	}

	@Test
	public void parseKey() throws ParseException {
		byte[] data = """
		test = ok
		skill example
			@cooldown: 1
		{ 
			block
		}
		skill example { 
			block
		}""".getBytes();
		var parser = new Parser(data);
		
		System.out.println(parser.parse());
	}

	@Test
	public void comment(TestReporter reporter) throws ParseException {
		byte[] data = """
		key {
			block example
		} # Comment
		""".getBytes();
		var parser = new Parser(data);
		
		reporter.publishEntry(parser.parse().toString());
	}

	@Test
	public void checkMechanic(TestReporter reporter) throws ParseException {
		byte[] data = """
		key {
			- message{m=ok;a=server} @self{}
		} # Comment
		field = value
		""".getBytes();
		var parser = new Parser(data);
		
		reporter.publishEntry(parser.parse().toString());
	}
}