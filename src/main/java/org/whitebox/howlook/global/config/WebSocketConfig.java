package org.whitebox.howlook.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //메시지 구독 url (topic을 구독)
        config.enableSimpleBroker("/sub");
        //메시지 발행 url
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
                //.withSockJS();
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                StompHeaderAccessor accessor =
//                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//                    Authentication user = ... ; // access authentication header(s)
//                    accessor.setUser(user);
//                }
//                return message;
//            }
//        });
//    }
}