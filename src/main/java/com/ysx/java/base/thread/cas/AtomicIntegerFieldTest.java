package com.ysx.java.base.thread.cas;


import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 *  cas 对类字段的操作
 *  对更新字段的要求
 *   1. volatile 修饰的 int 类型
 *   2. 在使用时可被反射操作字段(public/default)
 *   3. 不能 static 不能 final
 *
 *  为什么有了AtomicInteger还需要AtomicIntegerFieldUpdater？
 *
 *  当需要进行原子限定的属性所属的类会被创建大量的实例对象, 如果用AtomicInteger, 每个实例里面都要创建AtomicInteger对象, 从而多出内存消耗.显然是不合适的。
 *  因此出现了AtomicIntegerFieldUpdater（原子字段更新器），仅需要在抽象的父类中声明一个静态的更新器，就可以在各个对象中使用了。
 */
public class AtomicIntegerFieldTest {

    public static void main(String[] args) {

        AtomicIntegerFieldUpdater<Person> ageFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
        Person person = new Person();
        person.setName("张三");
        person.age = 10;
        ageFieldUpdater.addAndGet(person, 100);
        // 110
        System.out.println(person.age);
    }


    static class Person{
        private String name;
        volatile int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}


