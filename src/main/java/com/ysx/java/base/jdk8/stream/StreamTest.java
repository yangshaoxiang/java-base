package com.ysx.java.base.jdk8.stream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 *  重点:
 *  1. 集合转化为流后，流和集合是类似引用关系，集合数据变更，对流是有影响的，反过来则不成立 例如 流过滤一些数据，原始集合不受影响
 *  2. 对于 集合的 功能性操作 只是 先搞出功能性操作的函数式，并未实际操作，实际操作是在，流实际被使用 例如遍历
 *  3. 流实际只能被打开使用一次  例如 forEach 只能使用一次，使用后，流已不可操作
 *
 */
public class StreamTest {

    public static void main(String[] args) {
        List<String> languageList = new ArrayList<>();
        languageList.add("js");
        languageList.add("java");
        languageList.add("php");
        languageList.add("c++");
        languageList.add("html");
        //Arrays.asList("java","php","c++","js","html");
        for (String language : languageList) {
            System.out.println(language);
        }

        System.out.println("====================以下为流遍历操作====================");
        // 集合转化为流
        Stream<String> listTransStream = languageList.stream();
        // 通过下面采用流的方式遍历集合 会输出 go 表明 集合转化为流后 原始集合数据变更，流内容也会变更 因为流是和原始集合联系在一起的，类似引用关系，而不是一种 拷贝克隆关系
        // languageList.add("go");

        // 流操作 forEach -- 遍历集合 参数为 Consumer  1参0返 jdk8 内置 Consumer 接口 入参为集合的每个元素
        // 注意 流 只能被遍历一次
        // listTransStream.forEach(System.out::println);



        System.out.println("====================以下为流过滤操作====================");
        // 流操作 filter 对 流内容进行过滤 参数为 Predicate  其是接口 1参固定返回Boolean类型 jdk8 内置 入参为集合每个元素
        // 流的过滤操作 对原始集合没有影响,即 原始集合的元素是不受任何影响的
        Stream<String> afterFilterStream = listTransStream.filter(name -> name.startsWith("j"));
        // 元素集合新增过滤不应该通过的数据 不会影响过滤结果 即 go 不会再 结果中出现
        // languageList.add("go");
        // 元素集合删除 过滤通过的 数据 会影响过滤结果 即 java 不会在结果中出现
        // languageList.remove("java");
        // 由此 初步得出结论  filter 操作 只是一个 未执行的函数式 只有在真正使用 流时才会真正执行
        // afterFilterStream.forEach(System.out::println);

        // 流的过滤操作 对原始集合没有影响,即 原始集合的元素是不受任何影响的 输出的仍然是原始集合
        /*for (String language : languageList) {
            System.out.println(language);
        }*/

        System.out.println("====================以下为流元素操作====================");
        // 流操作 map 对 流内容进行操作并返回 1参1返 jdk8 内置 Function 接口 入参为 元素内容 出参为 操作后的元素
        Stream<String> afterMapStream = afterFilterStream.map(String::toUpperCase);
        // afterMapStream.forEach(System.out::println);

        System.out.println("====================以下为流排序操作====================");
        Stream<String> afterSortStream = afterMapStream.sorted();
        // afterSortStream.forEach(System.out::println);

        System.out.println("====================以下为流转集合操作====================");
        List<String> newLanguageList = afterSortStream.collect(toList());
        for (String newLangura : newLanguageList) {
            System.out.println(newLangura);
        }

        System.out.println("====================流可链式调用，以上一行代码操作====================");
        newLanguageList = languageList
                .stream()
                .filter(language->language.startsWith("j"))
                .map(String::toUpperCase)
                .sorted()
                .collect(toList());
        for (String s : newLanguageList) {
            System.out.println(s);
        }

        System.out.println("====================数组转换成流====================");
        String[] languageArray = {"java","php","c++","js","html"};
        Stream<String> languageArrayStream = Stream.of(languageArray);
        // 操作同上面的流操作

        System.out.println("====================文件内容转换成流====================");
        /*Path path = Paths.get("test.txt");
        Stream<String> lines = Files.lines(path);*/
    }
}
