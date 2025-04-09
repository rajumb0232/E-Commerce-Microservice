package com.example.api_gateway.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;

import java.util.function.Predicate;

@Slf4j
@Configuration
@AllArgsConstructor
public class RouteLogger {

    private final RouteDefinitionLocator routeDefinitionLocator;

    @PostConstruct
    public void logRoutes() {
        var routeDefinitions = routeDefinitionLocator.getRouteDefinitions();

        routeDefinitions.collectList().subscribe(defs -> {
            System.out.println("\n==== DYNAMIC ROUTES LOADED AT STARTUP ====\n");

            String format = "%-45s | %-30s%n";
            System.out.printf(format, "Route ID", "Path");
            int routeColWidth = 45;
            int pathColWidth = 30;
            int separatorLength = routeColWidth + pathColWidth + 3; // 3 for " | "
            String separator = "-".repeat(separatorLength);
            System.out.println(separator);

            Predicate<RouteDefinition> filteredDefs = def -> !def.getId().startsWith("ReactiveCompositeDiscoveryClient");

            defs.stream()
                    .filter(filteredDefs)
                    .forEach(def -> {
                        var path = getPath(def);
                        System.out.printf(format, def.getId(), path);
                    });

            System.out.println(separator + "\n");

            var size = defs.stream()
                    .filter(filteredDefs).toList().size();

            System.out.println("total registered routes: " + size + "\n");
        }, error -> {
            log.error("Error occurred while logging route definitions", error);
        });
    }


    private static String getPath(RouteDefinition def) {
        return def.getPredicates().stream()
                .filter(p -> p.getName().equals("Path"))
                .map(p -> p.getArgs().getOrDefault("_genkey_0", "N/A"))
                .findFirst()
                .orElse("N/A");
    }
}
