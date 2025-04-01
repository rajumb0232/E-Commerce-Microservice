package com.example.user.shared.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class Env {
    private String baseUrl;
    private Security security;

    @Getter
    @Setter
    public static class Security {
        private Long secretRotateIntervalMillis;
        private TokenValidity tokenValidity;
        private Cookie cookie;

        @Getter
        @Setter
        public static class TokenValidity {
            private Long accessValidity;
            private Long refreshValidity;
        }

        @Getter
        @Setter
        public static class Cookie {
            private String domain;
            private Boolean secure;
            private String sameSite;
        }
    }
}
