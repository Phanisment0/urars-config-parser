/**
 * A memory-efficient wrapper for a segment of a character buffer.
 * Instead of creating a new String object for every token, this class
 * points directly to the original data, significantly reducing GC pressure.
 */
public class BufferedString {
	public final char[] buffer;
	public final int start;
	public final int length;

	public final int end; 

	public BufferedString(final char[] buffer, final int start, final int end) {
		this.buffer = buffer;
		this.start = start;
		this.end = end;
		this.length = Math.max(0, end - start); 
	}

	public int length() {
		return this.length;
	}

	/**
	 * Compares the content of this buffer segment with another object.
	 */
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
		for (int i = 0; i < this.length; i++) {
			if (this.buffer[this.start + i] != that.buffer[that.start + i]) return false;
		}
		
		return true;
	}

	/**
	 * Generates a hash code berdasarkan isi karakter (Algoritma String standard).
	 */
	@Override
	public int hashCode() {
		int h = 0;
		for (int i = 0; i < this.length; i++) h = 31 * h + buffer[this.start + i]; 
		return h;
	}

	/**
	 * Converts the buffered segment into a standard String secara instan dan cepat.
	 */
	@Override
	public String toString() {
		if (this.length == 0) return "";
		return new String(buffer, start, length);
	}
}