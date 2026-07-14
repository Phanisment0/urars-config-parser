public record Entry(BufferedString key, BufferedString entry) {
	@Override
	public String toString() {
		return key.toString() + "=" + entry.toString();
	}
}
