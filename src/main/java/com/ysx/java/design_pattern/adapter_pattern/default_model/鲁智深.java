package com.ysx.java.design_pattern.adapter_pattern.default_model;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:17:10
 * Description:
 */
public class 鲁智深 extends 天星 implements 和尚{

    @Override
    public void 习武(){
        System.out.println("拳打镇关西；\n" +
                "        大闹五台山；\n" +
                "        大闹桃花村；\n" +
                "        火烧瓦官寺；\n" +
                "        倒拔垂杨柳；");

    }

    @Override
    public String getName(){
        return "鲁智深";
    }
}
