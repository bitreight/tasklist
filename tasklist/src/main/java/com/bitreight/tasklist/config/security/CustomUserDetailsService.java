package com.bitreight.tasklist.config.security;

import com.bitreight.tasklist.dto.UserDto;
import com.bitreight.tasklist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userService.getByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("Username \"" + username + "\" doesn't exist.");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        CustomUserDetails customUser = new CustomUserDetails(user.getUsername(), user.getPassword(), authorities);
        customUser.setId(user.getId());

        return customUser;
    }
}
