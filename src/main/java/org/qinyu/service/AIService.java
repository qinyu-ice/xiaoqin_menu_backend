package org.qinyu.service;

import reactor.core.publisher.Flux;

public interface AIService {

    /**
     * 非流式调用
     *
     * @param userMessage 用户信息
     * @return ai回答
     */
    String askWithChatClient(String userMessage);

    /**
     * 流式调用（使用 ChatClient 的 stream 方法）
     *
     * @param userMessage 用户消息
     * @return 流式返回的内容片段（Flux<String>）
     */
    Flux<String> askStreamWithChatClient(String userMessage);

    /**
     * 非流式调用（带记忆）
     *
     * @param userMessage    用户信息
     * @param conversationId 会话ID
     * @return 回答
     */
    String askWithMemory(String userMessage, String conversationId);

    /**
     * 流式调用（带记忆）
     *
     * @param userMessage    用户信息
     * @param conversationId 会话ID
     * @return 回答
     */
    Flux<String> askStreamWithMemory(String userMessage, String conversationId);

    /**
     * 根据会话ID删除会话
     *
     * @param conversationId 会话ID
     */
    Boolean deleteConversation(String conversationId);
}
