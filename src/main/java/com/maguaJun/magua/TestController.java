package com.maguaJun.magua;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final VectorStore vectorStore;

    @PostMapping("/test/store")
    public String store(@RequestBody String code) {
        System.out.println("传如参数" + code);

        Document document = new Document(code);

        vectorStore.add(List.of(document));
        return "存储成功";
    }
}
