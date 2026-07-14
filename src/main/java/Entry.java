public record Entry(BufferedString header, BufferedString entry) {
	@Override
	public String toString() {
		return header.toString() + "=" + entry.toString();
	}
}
