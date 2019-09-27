package com.ysx.java.design_pattern.adapter_pattern;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:16:27
 * Description: 手机充电功能类
 */
public class MobileClient {

    /**
     * 充电方法
     */
    public void congdian(TarInface inface){

        int put5V = inface.outPut5V();
        if(put5V==5){
            System.out.println("获取5V电压，充电开始");
        }else{
            System.out.println("电压不是5V。。。无法充电");
        }

    }
}
