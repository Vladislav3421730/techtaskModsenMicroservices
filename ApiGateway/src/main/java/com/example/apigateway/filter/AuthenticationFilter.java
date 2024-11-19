package com.example.apigateway.filter;


import com.example.apigateway.exception.InvalidJwtTokenException;
import com.example.apigateway.exception.MissingBearerTokenException;
import com.example.apigateway.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Set;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {

        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new MissingBearerTokenException("Please log in to access this resource");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                jwtUtil.validateToken(authHeader);
                List<String> roles = jwtUtil.getRolesFromToken(authHeader);

                if (!CheckAuthorization.isAuthorized(exchange, roles)) {
                    throw new InvalidJwtTokenException("You do not have enough rights for this operation");
                }

            }
            return chain.filter(exchange);
        });
    }



    public static class Config {

    }
}