package urars;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import io.phanisment.urars.lib.file_parser.UrArsFileParser;
import io.phanisment.urars.lib.file_parser.config.ConfigSection;
import io.phanisment.urars.lib.file_parser.parser.Parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 40, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class ParserBenchmark {
	private byte[] small;
	private byte[] large;
	private Parser parser;

	@Setup(Level.Trial)
	public void setup() throws IOException {
		var raw_small = "key { - mechanic{key=value;key0=value} context } # Comment\n";
		small = raw_small.getBytes();
		var raw_large = new StringBuilder();
		for (int i = 0; i < 100; i++) raw_large.append("skill ex_").append(i).append(" {\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
			.append("  - mechanic{} @self\n")
		.append("\n}\n")
		.append("test_").append(i).append("=Lololololo\n");
		large = raw_large.toString().getBytes();
		parser = new Parser(small);
	}

	@Benchmark
	public void line_small(Blackhole bh) throws ParseException {
		var data = (ConfigSection)UrArsFileParser.load(small);
		bh.consume(data.getLine("key"));
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