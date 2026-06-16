package com.project.login.config;

import com.project.security.CustomUserDetailsService;
import com.project.security.CustomUserDetails;
import com.project.security.JwtAuthenticationFilter;
import com.project.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/",
                        "/login",
                        "/register",
                        "/payment",               
                        "/admin/**",
                        "/send-register-otp",
                        "/resend-register-otp",
                        "/verify-register-otp",
                        "/forgot-password",
                        "/verify-otp",
                        "/account-inactive",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/qr_code.jpeg",
                        "/logo.png",
                        "/favicon.png",
                        "/webjars/**"
                ).permitAll()
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    Object principal = authentication.getPrincipal();
                    if (principal instanceof CustomUserDetails) {
                        String email = ((CustomUserDetails) principal).getUsername();
                        String token = tokenProvider.generateToken(email);

                        Cookie jwtCookie = new Cookie("jwt-token", token);
                        jwtCookie.setHttpOnly(true);
                        jwtCookie.setSecure(false); // set to true if HTTPS is used
                        jwtCookie.setPath("/");
                        jwtCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
                        response.addCookie(jwtCookie);
                    }
                    response.sendRedirect("/dashboard");
                })
                .failureHandler((request, response, exception) -> {
                    if (exception instanceof org.springframework.security.authentication.DisabledException) {
                        // User is inactive — redirect to subscription_invalid page
                        response.sendRedirect("/account-inactive?email=" + 
                            java.net.URLEncoder.encode(
                                request.getParameter("email") != null ? request.getParameter("email") : "", 
                                "UTF-8"
                            ));
                        return;
                    }
                    response.sendRedirect("/?error=true");
                })
                .permitAll()
            )

            .oauth2Login(oauth2 -> oauth2
                .loginPage("/")
                .defaultSuccessUrl("/google-success", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    Cookie jwtCookie = new Cookie("jwt-token", null);
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setSecure(false);
                    jwtCookie.setPath("/");
                    jwtCookie.setMaxAge(0);
                    response.addCookie(jwtCookie);
                    response.sendRedirect("/?logout");
                })
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "jwt-token")
                .permitAll()
            )

            .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        // This ensures DisabledException is thrown for inactive users
        provider.setHideUserNotFoundExceptions(true);
        return provider;
    }
}
