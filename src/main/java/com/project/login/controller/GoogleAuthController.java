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

import jakarta.servlet.http.HttpSession;

@Controller
public class GoogleAuthController {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/google-success")
	public String googleLogin(
			Authentication authentication,
			HttpSession session
			) {
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
		
		return "redirect:/dashboard";
	}
	
}
