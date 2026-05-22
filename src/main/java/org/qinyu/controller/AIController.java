package org.qinyu.controller;

import org.qinyu.mapper.AIMapper;
import org.qinyu.service.impl.AIServiceImpl;
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
    private AIServiceImpl aiServiceImpl;

    @GetMapping(value = "/chat", produces = "text/plain; charset=utf-8")
    public String chat(@RequestParam String message) {
        return aiServiceImpl.askWithChatClient(message);
    }

    @GetMapping(value = "/chat/stream", produces = "text/plain; charset=utf-8")
    public Flux<String> chatStream(@RequestParam String message) {
        return aiServiceImpl.askStreamWithChatClient(message);
    }

    @GetMapping(value = "/chat/memorial", produces = "text/plain; charset=utf-8")
    public String chat(@RequestParam String message, @RequestParam String userId) {
        return aiServiceImpl.askWithMemory(message, userId);
    }

    @GetMapping(value = "/chat/memorial/stream", produces = "text/plain; charset=utf-8")
    public Flux<String> streamChat(@RequestParam String message, @RequestParam String userId) {
        return aiServiceImpl.askStreamWithMemory(message, userId);
    }

}