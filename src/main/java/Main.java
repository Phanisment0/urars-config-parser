import java.io.File;
import java.text.ParseException;
import java.util.Arrays;

import parser.Parser;
import parser.line.LineParser;
import util.BufferedString;

public final class Main {
	private static final File TEST_FILE = new File("example");

	private static final int WARMUP = 20;
	private static final int ITERATIONS = 100;

	public static void main(String[] args) throws ParseException {
		byte[] raw_line = """
			key{test=a;ok=1} ctx
		""".getBytes();

		var line = new LineParser(new BufferedString(raw_line, 0, raw_line.length));
		line.parse();

		System.out.println(line);
	}

	private static void run() {
		System.out.println("Java      : " + System.getProperty("java.version"));
		System.out.println("OS        : " + System.getProperty("os.name"));
		System.out.println("File Size : " + formatSize(TEST_FILE.length()));
		System.out.println();

		benchmark("Parser", () -> {
			try {
				var parser = new Parser(TEST_FILE.toPath());
				parser.parse();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private static void benchmark(Runnable task) {
		long start = System.nanoTime();
		task.run();
		long end = System.nanoTime();
		double ms = (end - start) / 1_000_000.0;
		System.out.println("Time: " + ms + " ms");
	}

	private static void benchmark(String name, Runnable task) {
		// Warmup
		for (int i = 0; i < WARMUP; i++) {
			task.run();
		}

		System.gc();

		double[] samples = new double[ITERATIONS];

		double total = 0;
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		for (int i = 0; i < ITERATIONS; i++) {
			long start = System.nanoTime();
			task.run();
			long end = System.nanoTime();

			double ms = (end - start) / 1_000_000.0;

			samples[i] = ms;
			total += ms;
			min = Math.min(min, ms);
			max = Math.max(max, ms);
		}

		Arrays.sort(samples);

		double average = total / ITERATIONS;
		double median = samples[ITERATIONS / 2];

		double throughput = (TEST_FILE.length() / 1024.0 / 1024.0) / (average / 1000.0);

		System.out.println("========== " + name + " ==========");
		System.out.printf("Iterations : %d%n", ITERATIONS);
		System.out.printf("Average    : %.3f ms%n", average);
		System.out.printf("Median     : %.3f ms%n", median);
		System.out.printf("Min        : %.3f ms%n", min);
		System.out.printf("Max        : %.3f ms%n", max);
		System.out.printf("Total      : %.3f ms%n", total);
		System.out.printf("Speed      : %.2f MB/s (Estimation) %n", throughput);
		System.out.println();
	}

	private static String formatSize(long bytes) {
		if (bytes < 1024) return bytes + " B";
		if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
		return String.format("%.2f MB", bytes / 1024.0 / 1024.0);
	}
}