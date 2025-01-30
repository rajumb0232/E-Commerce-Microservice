package com.example.api_gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;


@Configuration
@AllArgsConstructor
public class ApiGatewayConfig {

//    private final DiscoveryClient discoveryClient;
//
//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
//        RouteLocatorBuilder.Builder routes = builder.routes();
//        discoveryClient.getServices().forEach(service -> {
//            System.out.println(service);
//            routes.route(service, r -> r.path("/" + service + "/**").uri("lb://" + service));
//        });
//
//        return routes.build();
//    }
}
