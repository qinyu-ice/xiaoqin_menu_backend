package org.qinyu.controller;

import org.qinyu.service.AIService;
import org.qinyu.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @GetMapping(value = "/chat", produces = "text/plain; charset=utf-8")
    public String chat(@RequestParam String message) {
        return aiService.askWithChatClient(message);
    }

    @GetMapping(value = "/chat/stream", produces = "text/plain; charset=utf-8")
    public Flux<String> chatStream(@RequestParam String message) {
        return aiService.askStreamWithChatClient(message);
    }

    @GetMapping(value = "/chat/memorial", produces = "text/plain; charset=utf-8")
    public String chat(@RequestParam String message, @RequestParam String conversationId) {
        return aiService.askWithMemory(message, conversationId);
    }

    @GetMapping(value = "/chat/memorial/stream", produces = "text/plain; charset=utf-8")
    public Flux<String> streamChat(@RequestParam String message, @RequestParam String conversationId) {
        return aiService.askStreamWithMemory(message, conversationId);
    }

    @GetMapping(value = "/chat/delete")
    public Result<Boolean> chatDelete(@RequestParam String conversationId) {
        return Result.ok("删除成功", aiService.deleteConversation(conversationId));
    }

}