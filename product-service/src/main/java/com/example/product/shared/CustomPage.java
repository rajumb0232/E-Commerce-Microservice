package com.example.product.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomPage<T> {
    private List<T> data;
    private int page;
    private int size;
    private long totalPages;
    private long totalElements;
}
