package com.example.api_gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
@AllArgsConstructor
public class DynamicRouteDefinitionLocator implements RouteDefinitionLocator {

    private final DiscoveryClient discoveryClient;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();

        discoveryClient.getServices().forEach(serviceId -> {
            log.info("Found Service with ID: {}", serviceId);
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            if (instances.isEmpty()) return;

            ServiceInstance instance = instances.get(0); // One instance is enough per versioned service

            String basePath = instance.getMetadata().get("basePath");

            if (basePath != null) {
                RouteDefinition routeDefinition = new RouteDefinition();
                routeDefinition.setId(serviceId); // ID per version
                routeDefinition.setUri(URI.create("lb://" + serviceId)); // Load-balanced URI

                PredicateDefinition predicate = new PredicateDefinition();
                predicate.setName("Path");
                predicate.addArg("_genkey_0", basePath + "/**");

                routeDefinition.setPredicates(List.of(predicate));

                log.info("Adding route for service {} at base path {}", serviceId, basePath);
                routeDefinitions.add(routeDefinition);
            }
        });

        return Flux.fromIterable(routeDefinitions);
    }
}
