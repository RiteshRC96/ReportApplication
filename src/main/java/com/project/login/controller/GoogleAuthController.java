package com.project.login.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.login.entity.User;
import com.project.login.repository.UserRepository;
import com.project.security.CustomUserDetails;
import com.project.security.JwtTokenProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleAuthController {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@GetMapping("/google-success")
	public String googleLogin(
			Authentication authentication,
			HttpSession session,
			HttpServletResponse response
			) {
		System.out.println("Visited page/Button clicked: Google Login (/google-success)");
		OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
		
		String email = oauthUser.getAttribute("email");
		String name = oauthUser.getAttribute("name");
		
		User user = userRepository.findByEmail(email).orElse(null);
		if (user == null) {
			user = new User();
			user.setEmail(email);
			user.setName(name);
			user.setPassword("OAUTH2_USER");
			userRepository.save(user);
		}
		
		CustomUserDetails customUserDetails = new CustomUserDetails(user);
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				customUserDetails, null, customUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		session.setAttribute("loggedInUser", user);

		// Generate JWT token and set in cookie
		String token = tokenProvider.generateToken(email);
		Cookie jwtCookie = new Cookie("jwt-token", token);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setSecure(false); // set to true if HTTPS is used
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(30 * 24 * 60 * 60); // 30 days
		response.addCookie(jwtCookie);
		
		return "redirect:/dashboard";
	}
	
}
