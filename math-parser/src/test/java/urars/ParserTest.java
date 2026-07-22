package urars;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import parser.MathParser;

class ParserTest {
	private MathParser parser(String input) {
		return new MathParser(input);
	}

	@Test
	void parseInteger() throws Exception {
		assertEquals(
				"123.0",
				parser("123").parse().toString());
	}

	@Test
	void parseDecimal() throws Exception {
		assertEquals(
				"12.5",
				parser("12.5").parse().toString());
	}

	@Test
	void parseString() throws Exception {
		assertEquals(
				"hello",
				parser("\"hello\"").parse().toString());
	}

	@Test
	void parseAddition() throws Exception {
		assertEquals(
				"(1.0 + 2.0)",
				parser("1+2").parse().toString());
	}

	@Test
	void parseSubtraction() throws Exception {
		assertEquals(
				"(8.0 - 3.0)",
				parser("8-3").parse().toString());
	}

	@Test
	void parseMultiplication() throws Exception {
		assertEquals(
				"(2.0 * 4.0)",
				parser("2*4").parse().toString());
	}

	@Test
	void parseDivision() throws Exception {
		assertEquals(
				"(9.0 / 3.0)",
				parser("9/3").parse().toString());
	}

	@Test
	void parseModulo() throws Exception {
		assertEquals(
				"(10.0 % 3.0)",
				parser("10%3").parse().toString());
	}

	@Test
	void precedenceMultiplyBeforeAdd() throws Exception {
		assertEquals(
				"(1.0 + (2.0 * 3.0))",
				parser("1+2*3").parse().toString());
	}

	@Test
	void precedenceDivideBeforeSubtract() throws Exception {
		assertEquals(
				"(10.0 - (6.0 / 2.0))",
				parser("10-6/2").parse().toString());
	}

	@Test
	void leftAssociativeAddition() throws Exception {
		assertEquals(
				"((1.0 + 2.0) + 3.0)",
				parser("1+2+3").parse().toString());
	}

	@Test
	void leftAssociativeMultiply() throws Exception {
		assertEquals(
				"((2.0 * 3.0) * 4.0)",
				parser("2*3*4").parse().toString());
	}

	@Test
	void stringConcatenationExpression() throws Exception {
		assertEquals(
				"(hello + world)",
				parser("\"hello\"+\"world\"").parse().toString());
	}

	@Test
	void mixedStringNumberExpression() throws Exception {
		assertEquals(
				"(hello + 123.0)",
				parser("\"hello\"+123").parse().toString());
	}

	@Test
	void ignoreWhitespace() throws Exception {
		assertEquals(
				"(1.0 + (2.0 * 3.0))",
				parser(" 1 + 2   * 3 ").parse().toString());
	}

	@Test
	void parenthesis() throws Exception {
		assertEquals(
				"((1.0 + 2.0) * 3.0)",
				parser("(1+2)*3").parse().toString());
	}

	@Test
	void nestedParenthesis() throws Exception {
		assertEquals(
				"(1.0 + ((2.0 + 3.0) * 4.0))",
				parser("1+(2+3)*4").parse().toString());
	}

	@Test
	void invalidCharacter() {
		assertThrows(
				ParseException.class,
				() -> parser("1&2").parse());
	}

	@Test
	void missingRightOperand() {
		assertThrows(
				ParseException.class,
				() -> parser("1+").parse());
	}

	@Test
	void unexpectedToken() {
		assertThrows(
				ParseException.class,
				() -> parser("+1").parse());
	}

	@Test
	void extraToken() {
		assertThrows(
				ParseException.class,
				() -> parser("1 2").parse());
	}

	@Test
	void emptyInput() {
		assertThrows(
				ParseException.class,
				() -> parser("").parse());
	}
}