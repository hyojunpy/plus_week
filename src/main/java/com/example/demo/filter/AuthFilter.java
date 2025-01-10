package com.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.Generated;

import java.io.IOException;

@Generated
public class AuthFilter implements CommonAuthFilter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        findHttpSession(servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
