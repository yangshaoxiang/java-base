package com.ysx.java.design_pattern.adapter_pattern.object_model;

import com.ysx.java.design_pattern.adapter_pattern.OldClass;
import com.ysx.java.design_pattern.adapter_pattern.TarInface;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:16:39
 * Description:
 */
public class ChangeAdapter implements TarInface {
    private OldClass oldClass;

    public ChangeAdapter(OldClass oldClass){
        this.oldClass = oldClass;
    }
    public int outPut5V() {
        int outPut220V = oldClass.outPut220V();
        int newv = outPut220V/44;
        System.out.println("适配器适配中。。");
        return newv;
    }
}
