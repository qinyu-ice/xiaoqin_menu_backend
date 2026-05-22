package org.qinyu.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.qinyu.entity.ChatUserBind;
import org.qinyu.mapper.AIMapper;
import org.qinyu.mapper.ChatUserBindMapper;
import org.qinyu.service.AIService;
import org.qinyu.util.JwtUtil;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ChatUserBindMapper chatUserBindMapper;

    @Autowired
    private AIMapper aiMapper;

    private final ChatClient chatClient;

    // 注入
    public AIServiceImpl(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory) {
        // 构建 Advisor
        var memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory)
                .build();
        // 将 Advisor 设为默认
        this.chatClient = chatClientBuilder.defaultAdvisors(memoryAdvisor).build();
        log.info("DeepSeek聊天模型加载成功，已启用 JDBC 聊天记忆");
    }

    @Override
    public String askWithChatClient(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "消息内容不能为空";
        }
        try {
            return this.chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("调用 DeepSeek 失败", e);
            return "AI 服务暂时不可用，请稍后重试";
        }
    }

    @Override
    public Flux<String> askStreamWithChatClient(String userMessage) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return Flux.just("消息内容不能为空");
        }
        return this.chatClient.prompt()
                .user(userMessage)
                .stream()
                .content()
                .doOnError(e -> log.error("流式调用 DeepSeek 失败, 消息: {}", userMessage, e))
                .onErrorResume(e -> Flux.just("[系统提示] AI 服务暂时不可用，请稍后重试"));
    }

    @Override
    public String askWithMemory(String userMessage, String conversationId) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "消息内容不能为空";
        }
        try {
            insertChatUserBind(conversationId);
            return this.chatClient.prompt()
                    .user(userMessage)
                    .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                    .call()
                    .content();
        } catch (Exception e) {
            log.error("调用 DeepSeek 失败", e);
            return "AI 服务暂时不可用，请稍后重试";
        }
    }

    @Override
    public Flux<String> askStreamWithMemory(String userMessage, String conversationId) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return Flux.just("消息内容不能为空");
        }
        insertChatUserBind(conversationId);
        return this.chatClient.prompt()
                .user(userMessage)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content()
                .doOnError(e -> log.error("流式调用 DeepSeek 失败, 消息: {}", userMessage, e))
                .onErrorResume(e -> Flux.just("[系统提示] AI 服务暂时不可用，请稍后重试"));
    }

    @Override
    public Boolean deleteConversation(String conversationId) {
        if (conversationId != null && !conversationId.trim().isEmpty()) {
            aiMapper.deleteById(conversationId);
            chatUserBindMapper.deleteById(conversationId);
            return true;
        }
        return false;
    }

    // 用户聊天数据插入
    private void insertChatUserBind(String conversationId) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = extractToken(request);
        String userId = jwtUtil.getUserIdFromToken(token);
        if (chatUserBindMapper.selectById(conversationId) == null) {
            ChatUserBind chatUserBind = new ChatUserBind();
            chatUserBind.setUserId(userId);
            chatUserBind.setConversationId(conversationId);
            chatUserBindMapper.insert(chatUserBind);
        }
    }

    // 获取当前 token
    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}