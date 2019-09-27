package com.ysx.java.design_pattern.single_pattern;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:10:45
 * Description: 静态内部类实现单例设计模式
 * 实现过程:
 * 1. 私有化构造方法
 * 2. 创建静态内部类，并设置内部类的一个静态成员变量为外部类对象
 * 3. 提供向外获取外部类对象的方法，返回值为内部类持有的外部类对象
 */
public class SingleInstance {

    private static class SingleInstanceHelp{
        private static SingleInstance  singleInstance = new SingleInstance();
    }

    public static SingleInstance getSingleInstance(){
        return  SingleInstanceHelp.singleInstance;
    }
    private SingleInstance(){}

    public void singleMethod(){
        System.out.println("静态单例方法");
    }
}
