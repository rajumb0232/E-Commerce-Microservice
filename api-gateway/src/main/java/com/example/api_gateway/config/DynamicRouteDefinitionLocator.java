package com.example.api_gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
@AllArgsConstructor
public class DynamicRouteDefinitionLocator implements RouteDefinitionLocator {

    private final DiscoveryClient discoveryClient;

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        List<RouteDefinition> routeDefinitions = new ArrayList<>();

        discoveryClient.getServices().forEach(serviceId -> {
            log.debug("Found Service with ID: {}", serviceId);
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
            if (instances.isEmpty()) return;

            instances.forEach(instance -> {
                Map<String, String> metadata = instance.getMetadata();
                String basePath = metadata != null ? metadata.get("basePath") : null;

                buildRouteDefinition(serviceId, basePath, routeDefinitions);
            });
        });

        return Flux.fromIterable(routeDefinitions);
    }

    private static void buildRouteDefinition(String serviceId, String basePath, List<RouteDefinition> routeDefinitions) {
        if (basePath != null && !basePath.isBlank()) {
            String[] paths = basePath.split(";");

            for (String rawPath : paths) {
                var path = sanitize(rawPath);

                if (path == null) continue;

                var routeDefinition = buildRouteDefinition(serviceId, path);
                routeDefinitions.add(routeDefinition);
            }
            log.debug("Registered {} route(s) for service '{}': {}", paths.length, serviceId, Arrays.toString(paths));
        }
    }

    private static RouteDefinition buildRouteDefinition(String serviceId, String path) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(serviceId + "-" + path.replaceAll("[^a-zA-Z0-9\\-]", "_"));
        routeDefinition.setUri(URI.create("lb://" + serviceId));

        PredicateDefinition predicate = new PredicateDefinition();
        predicate.setName("Path");
        predicate.addArg("_genkey_0", path + "/**");

        routeDefinition.setPredicates(List.of(predicate));
        log.debug("Route Definition built for {} at base path {}", serviceId, path);
        return routeDefinition;
    }

    private static String sanitize(String rawPath) {
        String path = rawPath.trim();
        if (path.isEmpty()) return null;

        if (!path.startsWith("/")) path = "/" + path; // Ensure slash prefix
        return path;
    }

}
