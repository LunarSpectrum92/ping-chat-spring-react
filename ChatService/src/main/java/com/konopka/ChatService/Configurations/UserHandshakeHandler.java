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
//    private final Logger LOG = LoggerFactory.getLogger(UserHandshakeHandler.class);
//
//    @Override
//    protected Principal determineUser(ServerHttpRequest request,
//                                      WebSocketHandler wsHandler,
//                                      Map<String, Object> attributes) {
//        List<String> authHeaders = request.getHeaders().get("X-AuthId-Header");
//        if (authHeaders == null || authHeaders.isEmpty()) {
//            return null;
//        }
//        String authId = authHeaders.get(0).trim();
//        return new UserPrincipal(authId);
//    }


    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        URI uri = request.getURI();
        MultiValueMap<String, String> params = UriComponentsBuilder.fromUri(uri).build().getQueryParams();
        String authId = params.getFirst("authId");

        if (authId == null || authId.isBlank()) {
            return null;
        }

        return new UserPrincipal(authId);
    }
}