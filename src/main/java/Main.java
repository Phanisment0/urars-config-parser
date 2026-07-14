import java.io.IOException;
import java.text.ParseException;

public final class Main {
	public static void main(String[] args) {
		// String i = """
		// 	example {
		// 		ok
		// 	}
		// """;

		// try (var reader = new StringReader(i)) {
		// 	var parser = new Parser(reader);
		// 	Map<String, String> result = parser.parse();

		// 	System.out.println(result);
		// }

		long start = System.nanoTime();

		try (var reader = new CharReader("example")) {
			reader.read();
			var parser = new Parser(reader.buffer(), reader.length());
			System.out.println(parser.parse());
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		long end = System.nanoTime();
		System.out.println("Time: " + ((end - start) / 1_000_000.0) + " ms");
	}
}