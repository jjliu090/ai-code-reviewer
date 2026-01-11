package com.maguaJun.magua.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VectorService {

    private final VectorStore vectorStore;

    public void storeToVectorDB(String code) {
        log.debug("传入参数: {}", code);

        Document document = new Document(code);

        vectorStore.add(List.of(document));
    }
}
