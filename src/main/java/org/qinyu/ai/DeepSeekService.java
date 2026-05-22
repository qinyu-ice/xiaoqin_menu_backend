package org.qinyu.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Slf4j
@Service
public class DeepSeekService {

    private final DeepSeekChatModel chatModel;
    private final ChatClient chatClient;

    // 方式一：注入 DeepSeekChatModel
    public DeepSeekService(DeepSeekChatModel chatModel, ChatClient.Builder chatClientBuilder) {
        this.chatModel = chatModel;
        // 方式二：使用ChatClient.Builder (推荐)
        this.chatClient = chatClientBuilder.build();
        log.info("DeepSeek聊天模型加载成功！");
    }

    public String askWithDeepSeekModel(String userMessage) {
        ChatResponse response = this.chatModel.call(new Prompt(userMessage));
        return Objects.requireNonNull(response.getResult()).getOutput().getText();
    }

    /**
     * 非流式输出
     *
     * @param userMessage 用户信息
     * @return ai回答
     */
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

    /**
     * 流式输出（使用 ChatClient 的 stream 方法）
     *
     * @param userMessage 用户消息
     * @return 流式返回的内容片段（Flux<String>）
     */
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
}