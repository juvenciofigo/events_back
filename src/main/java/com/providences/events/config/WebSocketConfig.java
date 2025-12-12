package com.providences.events.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint WebSocket (onde o frontend se conecta)
        registry.addEndpoint("/events-livechat")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // fallback
    }

    // @Override
    // public void registerStompEndpoints(StompEndpointRegistry registry) {
    // registry.addEndpoint("/buildrun-livechat-websocket");
    // }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // onde o cliente ENVIA mensagens
        registry.enableSimpleBroker("/topic"); // onde o cliente OUVIRÁ mensagens
    }
}