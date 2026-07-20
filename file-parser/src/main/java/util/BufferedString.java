package util;

public class BufferedString {
	public static final BufferedString EMPTY = new BufferedString(null, 0, 0);
	public byte[] buffer;
	public int start;
	public int end;

	public int length;

	public BufferedString(final byte[] buffer, final int start, final int end) {
		this.buffer = buffer;
		this.start = start;
		this.end = end;
		this.length = end - start; 
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

	@Override
	public String toString() {
		if (this.length == 0) return "";
		return new String(buffer, start, length);
	}
}
