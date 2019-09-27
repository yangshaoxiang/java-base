package com.ysx.java.design_pattern.chain_responsibility_attern;

public class FirstChainHandler extends ChainHandler{

    /**
     * 处理器处理请求
     */
    public void handleRequest() {
        System.out.println("第一处理器处理。。。。。");
        if(getNextChainHandler()!=null){
            System.out.println("第一处理器委托下一个处理器处理。。。。");
            getNextChainHandler().handleRequest();
        }
    }
}
