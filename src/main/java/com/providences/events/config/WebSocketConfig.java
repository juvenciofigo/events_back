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
        registry.addEndpoint("/events-livechat").setAllowedOriginPatterns("*").withSockJS();
    }

    // @Override
    // public void registerStompEndpoints(StompEndpointRegistry registry) {
    //     registry.addEndpoint("/buildrun-livechat-websocket");
    // }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // "/app" = destino para mensagens enviadas
        // "/topic" = destino para mensagens recebidas (broadcast)
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}