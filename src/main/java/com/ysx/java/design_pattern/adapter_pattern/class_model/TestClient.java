package com.ysx.java.design_pattern.adapter_pattern.class_model;

import com.ysx.java.design_pattern.adapter_pattern.MobileClient;
import com.ysx.java.design_pattern.adapter_pattern.TarInface;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:16:30
 * Description:
 */
public class TestClient {

    	public static void main(String[] args) {
            //给手机充电
            MobileClient mobileClient = new MobileClient();
            TarInface changeAdapter = new ChangeAdapter();
            mobileClient.congdian(changeAdapter);
        }
}
