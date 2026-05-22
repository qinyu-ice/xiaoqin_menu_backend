package org.qinyu.ai;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/ai")
public class DeepSeekController {

    private final DeepSeekService deepSeekService;

    public DeepSeekController(DeepSeekService deepSeekService) {
        this.deepSeekService = deepSeekService;
    }

    @GetMapping(value = "/chat", produces = "text/plain; charset=utf-8")
    public String chat(@RequestParam String message) {
        return deepSeekService.askWithChatClient(message);
    }

    @GetMapping(value = "/chat/stream", produces = "text/plain; charset=utf-8")
    public Flux<String> chatStream(@RequestParam String message) {
        return deepSeekService.askStreamWithChatClient(message);
    }
}