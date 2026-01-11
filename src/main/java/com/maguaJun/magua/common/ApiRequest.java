package com.maguaJun.magua.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRequest<T> {
    private String requestId;
    private Long timestamp;
    private T data;
}
