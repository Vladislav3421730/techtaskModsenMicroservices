package com.example.apigateway.filter;

import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Set;

public class CheckAuthorization {

    private final static Set<String> userPaths = Set.of(
            "/book/get",
            "/book/free",
            "/book/get/",
            "/book/get/isbn/",
            "/library/getStatuses",
            "/library/getStatus/"
    );

    public static boolean isAuthorized(ServerWebExchange exchange, List<String> roles) {

        String path = exchange.getRequest().getPath().value();

        if (roles.contains("ROLE_ADMIN")) {
            return true;
        }

        if (userPaths.stream().anyMatch(path::startsWith)) {
            return roles.contains("ROLE_USER");
        }
        return false;
    }
}
