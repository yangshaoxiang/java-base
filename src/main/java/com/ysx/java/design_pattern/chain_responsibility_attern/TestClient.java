package com.ysx.java.design_pattern.chain_responsibility_attern;

/**
 * author:ysx
 * Date:2018/4/20
 * Time:17:01
 * Description:
 */
public class TestClient {
    	public static void main(String[] args) {
            FirstChainHandler firstChainHandler = new FirstChainHandler();
            SecondChainHandler secondChainHandler = new SecondChainHandler();
            firstChainHandler.setNextChainHandler(secondChainHandler);
            firstChainHandler.handleRequest();
        }
}
