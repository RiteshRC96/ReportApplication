package com.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = getJwtFromRequest(request);

        if (token != null && tokenProvider.validateToken(token)) {
            String email = tokenProvider.getEmailFromToken(token);

            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null) {
                    if (userDetails.isEnabled()) {
                        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                        if (authorities == null) {
                            authorities = Collections.emptyList();
                        }
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // User exists but is disabled (e.g., inactive or unpaid subscription)
                        String uri = request.getRequestURI();
                        // Do not redirect on static assets, logout, admin paths, or the inactive/payment pages themselves
                        if (!uri.startsWith("/admin") && !uri.equals("/account-inactive") 
                            && !uri.equals("/logout") && !uri.equals("/payment")
                            && !uri.startsWith("/css") && !uri.startsWith("/js") 
                            && !uri.startsWith("/images") && !uri.startsWith("/webjars")) {
                            
                            response.sendRedirect("/account-inactive?email=" + java.net.URLEncoder.encode(email, "UTF-8"));
                            return; // Stop filter chain
                        }
                    }
                }
            } catch (Exception e) {
                // If user is not found or other issues, clear authentication context
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
