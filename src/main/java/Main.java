import java.io.IOException;
import java.text.ParseException;

public final class Main {
	public static void main(String[] args) {
		long start = System.nanoTime();

		try (var reader = new CharReader("example")) {
			reader.read();
			var parser = new EntryParser(reader.buffer(), reader.length());
			parser.parse();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		long end = System.nanoTime();
		System.out.println("Time: " + ((end - start) / 1_000_000.0) + " ms");
	}
}