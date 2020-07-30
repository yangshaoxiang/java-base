package com.ysx.java.base.jdk8.base;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 *  switch 和 if 性能测试
 *  switch 优于 if 因为 switch 对条件变量读取只读一次  if 每次都读 需要更多的时间
 *
 *
 *
 *  switch 的判断条件是 5 个时，性能比 if 高出了约 2.3 倍，而当判断条件的数量越多时，他们的性能相差就越大。
 *  而 switch 在编译为字节码时，会根据 switch 的判断条件是否紧凑生成两种代码：tableswitch（紧凑时生成）和 lookupswitch（非紧凑时生成），
 *  其中 tableswitch 是采用类似于数组的存储结构，直接根据索引查询元素；而 lookupswitch 则需要逐个查询或者使用二分法查询，
 *  因此 tableswitch 的性能会比 lookupswitch 的性能高，但无论如何 switch 的性能都比 if 的性能要高。
 *  来源 https://mp.weixin.qq.com/s/skd4cDwAi5y36bhkVwI9hg
 */
// 测试完成时间
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
// 预热 2 轮，每次 1s
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.SECONDS)
// 测试 5 轮，每次 3s
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1) // fork 1 个线程
@State(Scope.Thread) // 每个测试线程一个实例
public class SwitchOptimizeTest {

    static Integer _NUM = 9;

    public static void main(String[] args) throws RunnerException {
        // 启动基准测试
        Options opt = new OptionsBuilder()
                // 要导入的测试类
                .include(SwitchOptimizeTest.class.getSimpleName())
                // 输出测试结果的文件
                .output("D:\\tmp\\jmh-switch.log")
                .build();
        // 执行测试
        new Runner(opt).run();
    }

    @Benchmark
    public void switchTest() {
        int num1;
        switch (_NUM) {
            case 1:
                num1 = 1;
                break;
            case 3:
                num1 = 3;
                break;
            case 5:
                num1 = 5;
                break;
            case 7:
                num1 = 7;
                break;
            case 9:
                num1 = 9;
                break;
            default:
                num1 = -1;
                break;
        }
    }

    @Benchmark
    public void ifTest() {
        int num1;
        if (_NUM == 1) {
            num1 = 1;
        } else if (_NUM == 3) {
            num1 = 3;
        } else if (_NUM == 5) {
            num1 = 5;
        } else if (_NUM == 7) {
            num1 = 7;
        } else if (_NUM == 9) {
            num1 = 9;
        } else {
            num1 = -1;
        }
    }
}
