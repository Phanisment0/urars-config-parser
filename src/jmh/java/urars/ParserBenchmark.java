package urars;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import parser.Parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class ParserBenchmark {
	

	private byte[] small;
	private byte[] large;
	private Parser parser;

	@Setup(Level.Trial)
	public void setup() throws IOException {
		var raw_small = "key { block example } # Comment\n";
		small = raw_small.getBytes();
		var raw_large = new StringBuilder();
		for (int i = 0; i < 100; i++) raw_large.append("skill ex_").append(i).append(" {\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
		.append("\n}");
		large = raw_large.toString().getBytes();
		parser = new Parser(small);
	}

	@Benchmark
	public void small(Blackhole bh) throws ParseException {
		parser.reset(small);
		bh.consume(parser.parse());
	}

	@Benchmark
	public void large(Blackhole bh) throws ParseException {
		parser.reset(large);
		bh.consume(parser.parse());
	}
}