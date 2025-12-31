package com.maguaJun.magua.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.vectorstore.VectorStore;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VectorServiceTest {

    @Mock
    private VectorStore vectorStore;

    @InjectMocks
    private VectorService vectorService;

    @Test
    void storeCode_should_add_document_to_vectorStore() {
        // given
        String code = "public class A {}";

        // when
        vectorService.storeToVectorDB(code);

        // then
        verify(vectorStore).add(anyList());
    }
}
