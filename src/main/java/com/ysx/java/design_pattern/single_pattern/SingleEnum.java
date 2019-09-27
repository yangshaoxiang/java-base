package com.ysx.java.design_pattern.single_pattern;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:10:50
 * Description: 枚举实现单例模式
 */
public enum SingleEnum {
    SingleEnum;
    public void singleMethod(){
        System.out.println("枚举单例方法");
    }
}
