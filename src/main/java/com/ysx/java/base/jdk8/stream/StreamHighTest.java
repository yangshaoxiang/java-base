package com.ysx.java.base.jdk8.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  高级一些的流用法
 */
public class StreamHighTest {

    public static void main(String[] args) {
        ArrayList<Student> students = new ArrayList<>();
        students.add(new Student("张一", 11));
        students.add(new Student("张二", 12));
        students.add(new Student("张三", 13));
        students.add(new Student("张四", 14));

        //
        List<Student> studentList = students.stream()
                // .filter(e -> e.getAge() > 11 && e.getName().startsWith("张"))
                // 对于函数式，可以抽出来复用 一般不需复用，无需这么做
                .filter(Student.filter)
                .map(e -> {
                    e.setAge(e.getAge() + 1);
                    // 修改集合内元素
                    if ("张三".equals(e.getName())) {
                        e.setName("张三三");
                    }
                    return e;
                })
                .collect(Collectors.toList());

        System.out.println(studentList);


        // peek 和 map 区别
        // peek 没有 返回值，返回值默认为 原始元素
        // 两者接下来流无其他操作，均不会执行
        List<Student> studentList1 = students.stream()
                // .filter(e -> e.getAge() > 11 && e.getName().startsWith("张"))
                // 对于函数式，可以抽出来复用 一般不需复用，无需这么做
                .filter(Student.filter)
                .peek(e -> {
                    e.setAge(e.getAge() + 1);
                    // 这里 if 不会走，因为集合中已经 没有 张三 只有 张三三
                    if ("张三".equals(e.getName())) {
                        e.setName("张三三三");
                    }
                })
                .collect(Collectors.toList());
        System.out.println(studentList1);

        // 当没有流操作时 peek 和 map filter 等不会执行 他们只是相当于一段函数式
        System.out.println("单独 peek ");
        students.stream()
                .peek(System.out::println);
        System.out.println("单独 map ");
        students.stream()
                .map(e -> {
                    System.out.println();
                    return e;
                });
        students.stream()
                .filter(e -> e.getAge() > 10);

        //--- map 与 flatMap
        System.out.println("--- map 与 flatMap ---");
        List<String> strList = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");

        //map得到，的是一个新流，流中是流流里面是 Character
        Stream<Stream<Character>> stream2 = strList.stream()
                .map(StreamHighTest::filterCharacter);

        stream2.forEach((sm) -> {
            sm.forEach(System.out::println);
        });

        System.out.println("---------------------------------------------");
        //flatMap:将所有流连接成一个流
        Stream<Character> stream3 = strList.stream()
                .flatMap(StreamHighTest::filterCharacter);
        stream3.forEach(System.out::println);

    }





    private static Stream<Character> filterCharacter(String str){
        List<Character> list = new ArrayList<>();
        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();
    }


    static class Student {
        private String name;
        private Integer age;

        static Predicate<Student> filter =  e -> e.getAge() > 11 && e.getName().startsWith("张");



        Student(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        Integer getAge() {
            return age;
        }

        void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }


}
