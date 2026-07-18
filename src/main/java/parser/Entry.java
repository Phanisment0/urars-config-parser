package parser;
import util.BufferedString;

public record Entry(BufferedString header, BufferedString entry) {
	@Override
	public String toString() {
		return "Entry (" + header.toString() + " = " + entry.toString() + ")";
	}
}