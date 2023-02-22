package io.speed;


import io.speed.addition.BigFileGenerator;
import io.speed.reader.BufferedBIOCharReader;
import io.speed.reader.DefaultNIOReader;
import io.speed.reader.MemMapReader;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3000, timeUnit = TimeUnit.MILLISECONDS) // 进行3轮预热，每轮时长3秒
@Measurement(iterations = 5, time = 5000, timeUnit = TimeUnit.MILLISECONDS) // 进行4轮测试，每轮测试运行时长5秒
//@Threads(4)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class TestMain {

    private final String filePath = BigFileGenerator.getTestFilePath();

    @Benchmark
    public void testMemMapRead(){
        MemMapReader reader = new MemMapReader();
        reader.readFile(filePath);
    }

    @Benchmark
    public void testDefaultNIORead(){
        DefaultNIOReader reader = new DefaultNIOReader();
        reader.readFile(filePath);
    }

    @Benchmark
    public void testBufferedBIOCharRead(){
        BufferedBIOCharReader reader = new BufferedBIOCharReader();
        reader.readFile(filePath);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(TestMain.class.getSimpleName())
                .output("./IO_speed_Benchmark.log")
                .build();
        new Runner(options).run();
    }




}
