package com.ysx.java.design_pattern.adapter_pattern.class_model;

import com.ysx.java.design_pattern.adapter_pattern.OldClass;
import com.ysx.java.design_pattern.adapter_pattern.TarInface;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:16:24
 * Description: 适配器类
 */
public class ChangeAdapter extends OldClass implements TarInface {

    public int outPut5V() {
        int oldv = outPut220V();
        int newv = oldv/44;
        System.out.println("电压适配为5V");
        return newv;
    }
}
