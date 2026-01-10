package com.konopka.ChatService.Configurations;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;

@Component
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println(httpRequest.getServletPath() + " " + httpRequest.getHeader("X-AuthId-Header"));

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // || httpRequest.getRequestURI().startsWith("/nowy_6.html") || true
            if (httpRequest.getRequestURI().contains("/ws")) {
                chain.doFilter(request, response);
                return;
            }
        String customHeader = httpRequest.getHeader("X-AuthId-Header");
//        String customHeaderUserName = httpRequest.getHeader("X-AuthUserName-Header");
        System.out.println("customHeader: " + customHeader);
        if (customHeader == null || customHeader.isBlank()) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                customHeader,
                null,
                null
        );
        System.out.println("authentication: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }


}
