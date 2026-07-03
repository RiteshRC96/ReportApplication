package com.project.security;

import com.project.login.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

@SuppressWarnings("serial")
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final boolean effectiveEnabled;

    public CustomUserDetails(User user, boolean effectiveEnabled) {
        this.user = user;
        this.effectiveEnabled = effectiveEnabled;
    }

    public String getName() {
        return user.getName();   // 👈 THIS IS WHAT WE WANT
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // login still uses email
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() {
        return effectiveEnabled; // true only if admin-active AND has approved subscription
    }

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}
}
