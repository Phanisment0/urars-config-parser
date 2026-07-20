package util;

public final class ParserUtils {
	private ParserUtils() {
	}

	public static boolean isWhitespace(int c) {
		return c == ' '
				|| c == '\t'
				|| c == '\n'
				|| c == '\r';
	}

	public static boolean isIndent(int c) {
		return c == ' '
				|| c == '\t';
	}

	public static boolean isNewLine(int c) {
		return c == '\n'
				|| c == '\r';
	}
}
