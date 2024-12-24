package com.streamhub.ott.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {

	public UserDetails loadUserByUsername(String email);
}
