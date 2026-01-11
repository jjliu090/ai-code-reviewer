package com.maguaJun.magua.controller;

import com.maguaJun.magua.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
@Slf4j
public class WebhookController {
    @PostMapping("/github")
    public ApiResponse<Map<String, String>> handleWebhook(
            @RequestHeader("X-GitHub-Event") String event,
            @RequestHeader("X-Hub-Signature-256") String signature,
            @RequestBody String payload
    ) {
        log.info("收到webhook：{}", event);
        if ("ping".equals(event)) {
            return ApiResponse.success(
                    Map.of("event", "ping", "message", "pong")
            );
        }
        if ("pull_request".equals(event)) {
            // 收到 PR 事件
            log.info("这是一个 Pull Request 事件");
            return ApiResponse.success(Map.of(
                    "event", "pull_request",
                    "message", "PR 事件已接收"
            ));
        }
        return ApiResponse.success(
                Map.of("event", event != null ? event : "unknown", "message", "已忽略")
        );
    }
}
