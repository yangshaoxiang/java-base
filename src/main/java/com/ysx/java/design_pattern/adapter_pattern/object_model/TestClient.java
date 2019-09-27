package com.ysx.java.design_pattern.adapter_pattern.object_model;

import com.ysx.java.design_pattern.adapter_pattern.MobileClient;
import com.ysx.java.design_pattern.adapter_pattern.OldClass;
import com.ysx.java.design_pattern.adapter_pattern.TarInface;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:17:01
 * Description:
 */
public class TestClient {
    	public static void main(String[] args) {
    	    //创建已有功能对象
            OldClass oldClass = new OldClass();
            //创建适配对象,对oldClass适配
            TarInface changeAdapter = new ChangeAdapter(oldClass);
            //给手机充电
            MobileClient mobileClient = new MobileClient();
            mobileClient.congdian(changeAdapter);
        }

}
