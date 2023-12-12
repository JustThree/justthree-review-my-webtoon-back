package com.java.JustThree.config.chat;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

@Configuration
public class ServerEndpointConfigurator extends jakarta.websocket.server.ServerEndpointConfig.Configurator implements ApplicationContextAware {
    private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ServerEndpointConfigurator.context = applicationContext;
    }

//    @Override
//    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
//        // HTTP 요청에서 "Authorization" 헤더에서 토큰 추출
//        System.out.println(request.getHeaders());
//        String[] req =  request.getHeaders().get("sec-websocket-protocol").get(0).split(", ");
//
//        // 세션에 토큰 저장
//        sec.getUserProperties().put("token", req[0]);
//        sec.getUserProperties().put("masterId", req[1]);
//    }
}