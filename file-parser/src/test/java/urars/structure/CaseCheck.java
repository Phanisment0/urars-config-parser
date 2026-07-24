package urars.structure;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import io.phanisment.urars.lib.file_parser.parser.Parser;

public class CaseCheck {

	/*
	 * =========================
	 * Normal
	 * =========================
	 */

	@Test
	public void caseSimpleField() throws ParseException {
		byte[] data = """
		test = ok
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	@Test
	public void caseSimpleBlock() throws ParseException {
		byte[] data = """
		test {
			hello
		}
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	@Test
	public void caseMultiEntry() throws ParseException {
		byte[] data = """
		a = 1
		b = 2
		c {
			test
		}
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	@Test
	public void caseHeaderWithSpaces() throws ParseException {
		byte[] data = """
		skill fire ball = ok
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	/*
	 * =========================
	 * Field
	 * =========================
	 */

	@Test
	public void caseEmptyField() throws ParseException {
		byte[] data = """
		test =
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	@Test
	public void caseMultiWordField() throws ParseException {
		byte[] data = """
		message = hello world parser
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	@Test
	public void caseWhitespaceField() throws ParseException {
		byte[] data = """
		test        =         hello
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	/*
	 * =========================
	 * Block
	 * =========================
	 */

	@Test
	public void caseNestedBlock() throws ParseException {
		byte[] data = """
		root {
			child {
				value
			}
		}
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	@Test
	public void caseEmptyBlock() throws ParseException {
		byte[] data = """
		test {
		}
		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	/*
	 * =========================
	 * New Line
	 * =========================
	 */

	@Test
	public void caseBlankLine() throws ParseException {
		byte[] data = """

		test = ok


		test2 {
		}

		""".getBytes();

		System.out.println(new Parser(data).parse());
	}

	/*
	 * =========================
	 * Invalid
	 * =========================
	 */

	@Test
	public void caseMissingBracket() {
		byte[] data = """
		test {
		""".getBytes();

		try {
			System.out.println(new Parser(data).parse());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void caseOnlyEqual() {
		byte[] data = """
		=
		""".getBytes();

		try {
			System.out.println(new Parser(data).parse());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void caseOnlyRightBracket() {
		byte[] data = """
		}
		""".getBytes();

		try {
			System.out.println(new Parser(data).parse());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void caseHeaderWithoutValue() {
		byte[] data = """
		test
		""".getBytes();

		try {
			System.out.println(new Parser(data).parse());
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
	}
}