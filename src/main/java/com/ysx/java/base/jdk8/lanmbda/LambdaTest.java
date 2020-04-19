package com.ysx.java.base.jdk8.lanmbda;


import com.ysx.java.base.jdk8.lanmbda.lambdainterface.OneIn0OutInterface;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 *  JDK 8 Lambda 表达式的一些测试
 * Lambda表达式返回的是接口对象实例，如果函数式接口的实现恰好可以通过调用一个方法来实现，那么我们可以使用方法引用来替代Lambda表达式
 * 总结:
 *  Lambda 表达式 其实就是代替匿名对象创建的一种方式 只是只能代替那种只有一个待实现方法的接口所创建的匿名对象
 *  常规的语法:
 *  () -> {}
 *  ()内表示方法参数列表，不需要加类型 {}内编写实现方法的内容
 *  例
 *   OneIn1OutInterface oneIn11utInterface = (param1) -> {
 *             System.out.println("传入的参数:"+param1);
 *             return Integer.parseInt(param1);
 *         };
 *
 * 简化语法一
 *  当 () 内的参数只有一个时 可以省略 ()
 *  例
 *    OneIn1OutInterface oneIn11utInterface = param1 -> {
 *              System.out.println("传入的参数:"+param1);
 *               return Integer.parseInt(param1);
 *           };
 *
 * 简化语法二
 * 当 {} 内方法内容只有一行时 可以忽略 {}  方法内容是return语句 且只有一行，return 也可忽略
 * 例
 *       OneIn1OutInterface oneIn11utInterface = param1 ->  Integer.parseInt(param1);
 *
 * 简化语法三(最终)
 * 方法引用: 当 { } 内方法体的方法仅有一行，实现只是单纯调用其他方法，且这个其他方法参数和返回值同函数的方法参数和返回值一致
 *           此时 () 和 {} -> return 语句等皆可省略 只需保留内部方法类名 加 :: 加实际调用的方法名
 * 例
 *     OneIn1OutInterface oneIn11utInterface = Integer::parseInt;
 *
 * JDK 8 内置了一些 函数接口，可以使用这些接口的情况下，就无需自己新写接口
 *  1参0返 jdk8 内置 Consumer 接口
 *  1参1返 jdk8 内置 Function 接口
 *  0参1返 jdk8 内置 Supplier 接口
 *  1参固定返回Boolean类型 jdk8 内置 Predicate 接口
 *
 *  例如上面的那些示例接口，不想新建接口的话，可以用 jdk8 内置 Function 接口
 *  例:   Function<String,Integer> oneIn11utInterface = Integer::parseInt;
 * */
public class LambdaTest {
    public static void main(String[] args) {
        test0In0Out();
        test1In0Out(); // jdk8 内置 Consumer 接口
        test1In11ut(); // jdk8 内置 Function 接口
        test0In11ut(); // jdk8 内置 Supplier 接口
        testSprcial(); // jdk8 内置 Predicate 接口
    }

    /**
     *  测试 无参 无返回值接口 Lambda
     */
    private static void  test0In0Out(){
        System.out.println("------------"+"测试 无参 无返回值 接口Lambda"+"------------");
        // 匿名函数编写
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("传统 无入参无出参匿名实例创建");
            }
        };
        runnable.run();

        // Lambda 编写
        // ()内表示方法参数列表，不需要加类型 {}内编写实现方法的内容 如果方法只有一行 可以省略 {}
        Runnable runnable1 = () -> System.out.println("Lambda 无入参无出参Lambda匿名实例创建");
        runnable1.run();



        System.out.println();
    }

    /**
     *  测试 一个参数 无返回值接口 Lambda
     *  Consumer 消费类型函数式接口 (1入0回)
     */
    private static void  test1In0Out(){
        System.out.println("------------"+"测试 一参 无返回值 接口Lambda"+"------------");
        // 匿名函数编写
        OneIn0OutInterface oneIn0OutInterface = new OneIn0OutInterface() {
            @Override
            public void oneInMethod(String input) {
                System.out.println("入参:" + input);
            }
        };

        oneIn0OutInterface.oneInMethod("传统 一入参无出参匿名实例创建");

        // Lambda 编写
        //括号表示方法参数列表 {}内编写实现方法的内容 如果参数只有一个可以不写小括号 如果方法只有一行 可以省略 {}
        OneIn0OutInterface oneIn0OutInterface1 = input -> System.out.println("入参:" + input);
        oneIn0OutInterface1.oneInMethod("Lambda 一入参无出参Lambda匿名实例创建");
        // 进一步简化
        // oneIn0OutInterface1 = System.out::print;

        // Consumer JDK 内置的 1参0返，在代码中如果需要1参0返的匿名函数可以优先考虑内置的
        Consumer<String> consumer = e -> System.out.println("入参:"+e);
        consumer.accept("Consumer JDK 内置的 1参0返 匿名接口");

        // Lambda 一种特殊情况下的简化语法 当 Lambda 函数(匿名接口实例) 要实现的方法 内部 实现只是单纯调用其他方法，且这个其他方法参数和返回值同就是函数的方法参数和返回值
        // 那么可以使用方法引用 简化代码
        Consumer<String> consumer1 = LambdaTest::getInput;
        consumer1.accept("Consumer 1参0返 方法引用");

        System.out.println();
    }

    /**
     *  测试 一个参数 一个返回值接口 Lambda
     *  Function<T,R> 函数型接口 泛型T表示 入参类型 泛型R 表示出参类型
     */
    private static void  test1In11ut(){
        System.out.println("------------"+"测试 一参 一返回值 接口Lambda"+"------------");
        // 普通写法
        Function<String,Integer> function = input ->{
            return Integer.parseInt(input);
        };
        Integer apply = function.apply("5");
        System.out.println("普通写法，返回值"+apply);

        // Lambda 简化语法 只有一行的情况下，可以省略大括号和 return
        // 简化写法
        Function<String,Integer> function1 = input -> Integer.parseInt(input);
        Integer apply1 = function1.apply("6");
        System.out.println("简化写法，返回值"+apply1);

        // 更简化写法 方法引用
        // Lambda一种特殊情况下的简化语法 当 Lambda 函数(匿名接口实例) 要实现的方法 内部 实现只是单纯调用其他方法，且这个其他方法参数和返回值同就是函数的方法参数和返回值
        // 那么可以使用方法引用 简化代码
        Function<String,Integer> function2 = Integer::parseInt;
        Integer apply2 = function2.apply("7");
        System.out.println("方法引用，返回值"+apply2);

        //ps 扩充关于Function的compose方法  function1.apply(function2.apply(2)) = function1.compose(function2).apply(2)
        System.out.println();
    }

    /**
     *  测试 0个参数 一个返回值接口 Lambda
     * Supplier<R>  函数型接口  泛型R 表示出参类型
     */
    private static void  test0In11ut(){
        System.out.println("------------"+"测试 0参 一返回值 接口Lambda"+"------------");
        // 返回值1行 可以省略{} 和 return
        Supplier<String> supplier = () -> "Supplier: 0参1返";
        String result = supplier.get();
        System.out.println(result);

        System.out.println();
    }

    /**
     *  这是一个用于判断指定入参 经过一系列操作后是否满足条件的函数接口
     *  Predicate<T>  泛型 T 表示 入参类型 出参类型固定为布尔类型
     */
    private static void testSprcial(){
        System.out.println("------------"+"测试 1入固定Boolean出 特殊接口Lambda"+"------------");
        Predicate<String> predicate = input -> input != null && input.trim().length() != 0;
        boolean test = predicate.test("");

        System.out.println("Predicate: 入参是否满足指定规则:"+test);
    }


    private static void getInput(String input){
        System.out.println("入参:"+input);
    }

}
