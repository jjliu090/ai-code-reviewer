package com.maguaJun.magua.controller;


import com.maguaJun.magua.common.ApiRequest;
import com.maguaJun.magua.common.ApiResponse;
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
    public ApiResponse<String> store(@RequestBody ApiRequest<String> request) {
        String code = request.getData();
        vectorService.storeToVectorDB(code);

        return ApiResponse.success(code);
    }

}
