package org.once_a_day.messenger.messenger_app.config;

import lombok.RequiredArgsConstructor;
import org.once_a_day.messenger.messenger_app.interceptor.WebsocketInboundInterceptor;
import org.once_a_day.messenger.messenger_app.interceptor.WebsocketOutboundInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class MessageBrokerConfig implements WebSocketMessageBrokerConfigurer {
    private final WebsocketInboundInterceptor inboundChannelInterceptor;
    private final WebsocketOutboundInterceptor outboundInterceptor;
    @Override
    public void registerStompEndpoints(final StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(final MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/messenger"); // JS starts here
        registry.enableSimpleBroker("/user"); // Topic prefixes
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(final ChannelRegistration registration) {
        registration.interceptors(inboundChannelInterceptor);
    }

    @Override
    public void configureClientOutboundChannel(final ChannelRegistration registration) {
        registration.interceptors(outboundInterceptor);
    }
}
