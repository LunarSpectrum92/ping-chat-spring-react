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
        String path = exchange.getRequest().getPath().value();
        String token;

        if (path.contains("/ws")) {
            token = exchange.getRequest().getQueryParams().getFirst("token");
            System.out.println("token: " + token);
        } else {
            token = getToken(exchange.getRequest());
        }
        if (token != null && jwtService.validateToken(token)) {
            String username = jwtService.extractUserName(token);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username, null, List.of());
            exchange.getRequest().getHeaders().set("X-AuthId-Header", jwtService.extractUserId(token));
            System.out.println(exchange.getRequest().getHeaders().get("X-AuthId-Header"));
//            exchange.getRequest().getHeaders().set("X-AuthUserName-Header", username);
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
