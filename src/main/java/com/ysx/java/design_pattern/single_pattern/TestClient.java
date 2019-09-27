package com.ysx.java.design_pattern.single_pattern;

import java.lang.reflect.Constructor;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:10:48
 * Description: 验证
 */
public class TestClient {

    	public static void main(String[] args) throws Exception {
            //基本验证
            SingleInstance singleInstance = SingleInstance.getSingleInstance();
            SingleInstance singleInstance1 = SingleInstance.getSingleInstance();
            System.out.println(singleInstance==singleInstance1);
            singleInstance.singleMethod();

            SingleEnum singleEnum = SingleEnum.SingleEnum;
            singleEnum.singleMethod();

            //反射验证 --结论  无法防止反射构造多例对象
            Class clazzInstance = SingleInstance.class;

            Constructor constructor = clazzInstance.getDeclaredConstructor();
            constructor.setAccessible(true);
            SingleInstance instance = (SingleInstance)constructor.newInstance();
            instance.singleMethod();

        }
}
