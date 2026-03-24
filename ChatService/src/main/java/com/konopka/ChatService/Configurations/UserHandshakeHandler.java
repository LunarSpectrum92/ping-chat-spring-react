package com.konopka.ChatService.Configurations;

import com.sun.security.auth.UserPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public class UserHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        String authId = UriComponentsBuilder.fromUri(request.getURI())
                .build().getQueryParams().getFirst("authId");

        if (authId == null) {
            System.out.println("Handshake failed: authId is null");
            return null;
        }

        System.out.println("Handshake success for user: " + authId);
        return () -> authId;
    }
}