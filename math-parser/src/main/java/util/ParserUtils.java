package util;

public final class ParserUtils {
	public static double parseDouble(BufferedString input) {
		double value = 0;
		int pos = input.start;
		int end = input.end;
		while (pos < end) {
			int c = input.buffer[pos] & 0xFF;
			if (c == '.') break;
			value = value * 10 + (c - '0');
			pos++;
		}
		if (pos == end) return value;
		pos++;
		double decimal = 0.1;
		while (pos < end) {
			int c = input.buffer[pos] & 0xFF;
			value += (c - '0') * decimal;
			decimal *= 0.1;
			pos++;
		}
		return value;
	}
}
