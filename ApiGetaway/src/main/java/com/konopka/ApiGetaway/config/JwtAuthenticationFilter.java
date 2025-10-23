package com.konopka.ApiGetaway.config;

import com.konopka.ApiGetaway.services.JwtService;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.List;


@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = getToken(exchange.getRequest());

        if (token != null && jwtService.validateToken(token)) {
            String username = jwtService.extractUserName(token);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username, null, List.of());
            return chain.filter(exchange).contextWrite(
                    ReactiveSecurityContextHolder.withAuthentication(auth));
        }

        return chain.filter(exchange);
    }

    private String getToken(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


}
