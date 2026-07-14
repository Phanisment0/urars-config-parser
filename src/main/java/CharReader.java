import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class CharReader implements AutoCloseable {
	private static final int BUFFER_SIZE = 1024;
	private static final int SIZE_MULTIPLIER = 2;

	private final Reader reader;
	
	private char[] buffer = new char[BUFFER_SIZE];
	private int pos = 0;

	public CharReader(String file) throws FileNotFoundException {
		this(new BufferedReader(new FileReader(file)));
	}

	public CharReader(Reader reader) {
		this.reader = reader;
	}

	private void resize() {
		buffer = Arrays.copyOf(buffer, buffer.length * SIZE_MULTIPLIER);
	}

	public void read() throws IOException {
		while (true) {
			if (pos == buffer.length) this.resize();
			int len = reader.read(buffer, pos, buffer.length - pos);
			if (len == -1) break;
			pos += len;
		}
	}

	public int length() {
		return pos;
	}

	public char[] buffer() {
		return buffer;
	}

	@Override
	public void close() throws IOException {
		if (reader != null) reader.close();
	}
}