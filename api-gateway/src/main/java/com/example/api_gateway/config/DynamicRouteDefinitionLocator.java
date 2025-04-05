package com.example.api_gateway.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DynamicRouteDefinitionLocator {

    RouteDefinitionLocator routeDefinitionLocator(DiscoveryClient discoveryClient) {
        return () -> {
            discoveryClient.getServices().forEach(System.err::println);

            List<RouteDefinition> routeDefinitions = new ArrayList<>();

            discoveryClient.getServices().forEach(serviceId -> {
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

                    FilterDefinition filter = new FilterDefinition();
                    filter.setName("StripPrefix");
                    filter.addArg("_genkey_0", String.valueOf(basePath.split("/").length - 1));

                    routeDefinition.setPredicates(List.of(predicate));
                    routeDefinition.setFilters(List.of(filter));

                    routeDefinitions.add(routeDefinition);
                }
            });

            return Flux.fromIterable(routeDefinitions);
        };
    }
}
