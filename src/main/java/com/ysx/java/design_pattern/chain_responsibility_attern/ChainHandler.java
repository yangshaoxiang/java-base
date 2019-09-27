package com.ysx.java.design_pattern.chain_responsibility_attern;

/**
 *  处理链中 所有处理器抽象类
 */
public abstract class ChainHandler {
    /**
     *  当前处理器下一个处理器
     */
     private ChainHandler nextChainHandler;

    /**
     *  处理器处理请求
     */
    public abstract void handleRequest();


    public ChainHandler getNextChainHandler() {
        return nextChainHandler;
    }

    public void setNextChainHandler(ChainHandler nextChainHandler) {
        this.nextChainHandler = nextChainHandler;
    }
}
