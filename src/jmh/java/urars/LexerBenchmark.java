package urars;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import tokenize.Lexer;
import tokenize.Token;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class LexerBenchmark {
	private byte[] small;
	private byte[] large;
	private Lexer lexer;

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

		lexer = new Lexer(small);
	}

	@Benchmark
	public void small(Blackhole bh) {
		lexer.reset(small);
		Token token;
		while ((token = lexer.next()) != Token.EOF) bh.consume(token);
	}

	@Benchmark
	public void large(Blackhole bh) {
		lexer.reset(large);
		Token token;
		while ((token = lexer.next()) != Token.EOF) bh.consume(token);
	}
}