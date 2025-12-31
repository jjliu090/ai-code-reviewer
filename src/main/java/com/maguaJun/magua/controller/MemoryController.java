package com.maguaJun.magua.controller;


import com.maguaJun.magua.service.VectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/memory")
@RequiredArgsConstructor
public class MemoryController {

    private final VectorService vectorService;

    @PostMapping("/store")
    public String store(@RequestBody String code) {
        return "asdf";
    }

}
