package parser;
import util.BufferedString;

public record Entry(BufferedString header, BufferedString entry) {
	@Override
	public String toString() {
		return """
		\n- Entry -
		  Header: %s
		  Value:
		%s
		""".formatted(header, entry.toString().indent(4));
	}
}