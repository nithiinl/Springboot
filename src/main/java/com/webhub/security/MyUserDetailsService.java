package com.webhub.security;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
	 @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 if ("user".equals(username)) {
	            return new User("user", "$2a$04$ePrhXoUTR5nbeSOFiSi9UenOljDcScoC3VQ0NmU7E4icATP.3k/2i", new ArrayList<>());
	            // The encoded password corresponds to "password"
	        } else {
	            throw new UsernameNotFoundException("User not found with username: " + username);
	        }
	    }
}
