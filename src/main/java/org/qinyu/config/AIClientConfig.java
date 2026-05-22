package org.qinyu.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        var chatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .build();

        return chatClientBuilder
                .defaultAdvisors(chatMemoryAdvisor)
                .build();
    }
}