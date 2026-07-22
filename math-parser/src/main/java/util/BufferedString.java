package util;

import java.util.ArrayList;
import java.util.List;

public class BufferedString {
	public byte[] buffer;
	public int start;
	public int end;

	public int length;

	public BufferedString(final String buffer) {
		this(buffer.getBytes());
	}

	public BufferedString(final byte[] buffer) {
		this(buffer, 0, buffer != null ? buffer.length : 0);
	}

	public BufferedString(final byte[] buffer, final int start, final int end) {
		this.buffer = buffer;
		this.start = start;
		this.end = end;
		this.length = end - start;
	}
	
	public static BufferedString empty() {
		return new BufferedString(new byte[0], 0, 0);
	}

	public void reset(byte[] buffer) {
		this.buffer = buffer;
		this.start = 0;
		this.end = buffer != null ? buffer.length : 0;
		this.length = end - start;
	}

	public BufferedString[] split(char sparator) {
		return split(sparator, Integer.MAX_VALUE);
	}

	public BufferedString[] split(char separator, int max) {
		if (max <= 0) return new BufferedString[0];
		if (this.length == 0) return new BufferedString[]{ this };

		List<BufferedString> result = new ArrayList<>();
		int current = this.start;

		for (int i = this.start; i < this.end; i++) {
			if (result.size() == max - 1) break;
			if (buffer[i] == separator) {
				result.add(new BufferedString(buffer, current, i));
				current = i + 1;
			}
		}
		result.add(new BufferedString(buffer, current, this.end));
		return result.toArray(new BufferedString[0]);
	}

	public BufferedString[] splitByDepth(char separator, DepthSparator... depths) {
		if (this.length == 0) return new BufferedString[]{ this };

		List<BufferedString> result = new ArrayList<>();
		byte target = (byte) separator;
		int current = this.start;
		int depth = 0;

		for (int i = this.start; i < this.end; i++) {
			byte b = buffer[i];
			for (DepthSparator d : depths) {
				if (b == (byte) d.start()) {
					depth++;
					break;
				} else if (b == (byte) d.end()) {
					depth--;
					break;
				}
			}

			if (depth <= 0 && b == target) {
				result.add(new BufferedString(buffer, current, i));
				current = i + 1;
				depth = 0;
			}
		}

		result.add(new BufferedString(buffer, current, this.end));
		return result.toArray(new BufferedString[0]);
	}

	public byte byteAt(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= length) throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
		return buffer[start + index];
	}

	public int intAt(int index) throws IndexOutOfBoundsException {
		return byteAt(index) & 0xFF;
	}

	public boolean equalsIgnoreCase(Object o) {
		if (this == o) return true;
		if (o instanceof String str) {
			if (this.length != str.length()) return false;
			for (int i = 0; i < this.length; i++) {
				int c1 = this.buffer[this.start + i] & 0xFF;
				char c2 = str.charAt(i);
				if (toLowercase(c1) != toLowercase(c2)) return false;
			}
			return true;
		}
		if (o instanceof BufferedString that) {
			if (this.length != that.length) return false;
			for (int i = 0; i < this.length; i++) {
				int c1 = this.buffer[this.start + i] & 0xFF;
				int c2 = that.buffer[that.start + i] & 0xFF;
				if (toLowercase(c1) != toLowercase(c2)) return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o instanceof String str) {
			if (this.length != str.length()) return false;
			for (int i = 0; i < this.length; i++) if (this.buffer[this.start + i] != str.charAt(i)) return false;
			return true;
		}
		
		if (!(o instanceof BufferedString that)) return false;
		if (this.length != that.length) return false;
		for (int i = 0; i < this.length; i++) if (this.buffer[this.start + i] != that.buffer[that.start + i]) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < this.length; i++) h = 31 * h + buffer[this.start + i]; 
		return h;
	}

	public boolean isEmpty() {
		return length == 0;
	}

	@Override
	public String toString() {
		if (this.length == 0) return "";
		return new String(buffer, start, length);
	}

	public int toInteger() {
		return (int)toLong();
	}

	public static int toInteger(byte[] buffer, int start, int end) {
		return (int)toInteger(buffer, start, end);
	}

	public float toFloat() {
		return (float)toDouble();
	}

	public static float toFloat(byte[] buffer, int start, int end) {
		return (float)toDouble(buffer, start, end);
	}

	public double toDouble() {
		return toDouble(this.buffer, this.start, this.end);
	}

	public static double toDouble(byte[] buffer, int start, int end) {
		if ((end - start) == 0) return 0.0;

		int pos = start;
		boolean negative = false;

		if (pos < end && buffer[pos] == '-') {
			negative = true;
			pos++;
		} else if (pos < end && buffer[pos] == '+') pos++;
		double value = 0;
		while (pos < end) {
			int c = buffer[pos] & 0xFF;
			if (c == '.') break;
			if (c == ',' || c == '_') {
				pos++;
				continue;
			}
			if (c < '0' && c > '9') break;
			value = value * 10 + (c - '0');
			pos++;
		}

		if (pos < end && buffer[pos] == '.') {
			pos++;
			double decimal = 0.1;
			while (pos < end) {
				int c = buffer[pos] & 0xFF;
				if (c == ',' || c == '_') {
					pos++;
					continue;
				}
				if (c >= '0' && c <= '9') {
					value += (c - '0') * decimal;
					decimal *= 0.1;
				}
				pos++;
			}
		}
		return negative ? -value : value;
	}

	public long toLong() {
		return toLong(this.buffer, this.start, this.end);
	}

	public static long toLong(byte[] buffer, int start, int end) {
		if ((end - start) == 0) return 0;

		long value = 0;
		int pos = start;
		boolean negative = false;

		if (pos < end && buffer[pos] == '-') {
			negative = true;
			pos++;
		} else if (pos < end && buffer[pos] == '+') pos++;
		while (pos < end) {
			int c = buffer[pos] & 0xFF;
			if (c == ',' || c == '_') {
				pos++;
				continue;
			}
			if (c < '0' || c > '9') break;
			value = value * 10 + (c - '0');
			pos++;
		}
		return negative ? -value : value;
	}

	public boolean toBoolean() {
		if (length == 4) return isTrue();
		if (length == 3) return isYes();
		if (length == 1) return buffer[start] == '1';
		return false;
	}

	public static int toUppercase(int c) {
		if (c >= 'a' && c <= 'z') return c - 32;
		return c;
	}

	public static int toLowercase(int c) {
		if (c >= 'A' && c <= 'Z') return c + 32;
		return c;
	}

	public boolean isYes() {
		if (length == 3) return false;
		return ((buffer[start    ] == 'y') || (buffer[start    ] == 'Y'))
				&& ((buffer[start + 1] == 'e') || (buffer[start + 1] == 'E'))
				&& ((buffer[start + 2] == 's') || (buffer[start + 2] == 'S'));
	}

	public boolean isNo() {
		if (length == 2) return false;
		return ((buffer[start    ] == 'n') || (buffer[start    ] == 'N'))
				&& ((buffer[start + 1] == 'o') || (buffer[start + 1] == 'O'));
	}

	public boolean isTrue() {
		if (length == 4) return false;
		return ((buffer[start    ] == 't') || (buffer[start    ] == 'T'))
				&& ((buffer[start + 1] == 'r') || (buffer[start + 1] == 'R'))
				&& ((buffer[start + 2] == 'u') || (buffer[start + 2] == 'U'))
				&& ((buffer[start + 3] == 'e') || (buffer[start + 3] == 'E'));
	}

	public boolean isFalse() {
		if (length == 5) return false;
		return ((buffer[start    ] == 'f') || (buffer[start    ] == 'F'))
				&& ((buffer[start + 1] == 'a') || (buffer[start + 1] == 'A'))
				&& ((buffer[start + 2] == 'l') || (buffer[start + 2] == 'L'))
				&& ((buffer[start + 3] == 's') || (buffer[start + 3] == 'S'))
				&& ((buffer[start + 4] == 'e') || (buffer[start + 4] == 'E'));
	}

	public static record DepthSparator(char start, char end) {
	}
}