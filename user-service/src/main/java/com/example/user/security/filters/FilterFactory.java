package com.example.user.security.filters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

//@Component
@AllArgsConstructor
public class FilterFactory {

    private final FilterHelper filterHelper;

    public AuthFilter getAuthFilter() {
        return new AuthFilter(filterHelper);
    }

}
