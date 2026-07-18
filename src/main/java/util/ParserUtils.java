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
}
