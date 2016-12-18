package com.bitreight.tasklist.security;

import com.bitreight.tasklist.dao.UserDao;
import com.bitreight.tasklist.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        User user = userDao.findByUsername(username);
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
