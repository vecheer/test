package other.charcompare;


import lombok.SneakyThrows;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3000, timeUnit = TimeUnit.MILLISECONDS) // 进行3轮预热，每轮时长3秒
@Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS) // 进行4轮测试，每轮测试运行时长5秒
//@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class BenchMarkTest {
    int character = '$';

    @Benchmark
    public void testLessThanEquals(){
        boolean flag;
        flag = character <= ' ';
    }

    @Benchmark
    public void testEquals(){
        boolean flag;
        flag = character == ' ';
    }


    @SneakyThrows
    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(BenchMarkTest.class.getSimpleName())
                .output("./char_compare_Benchmark.log")
                .build();
        new Runner(options).run();
    }
}
