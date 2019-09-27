package com.ysx.java.design_pattern.adapter_pattern;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:16:08
 * Description: 系统现有的类，提供发电220V的方法
 */
public class OldClass {

    public int outPut220V(){
        System.out.println("现有类 - 自发电220V");
        return 220;
    }
}
