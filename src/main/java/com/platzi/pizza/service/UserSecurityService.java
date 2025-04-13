package com.platzi.pizza.service;

import com.platzi.pizza.persistence.entity.UserRoleEntity;
import com.platzi.pizza.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.platzi.pizza.persitence.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        String[] roles = userEntity.getRoles()
                .stream()
                .map(UserRoleEntity::getRole)
                .toArray(String[]::new);

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .authorities(grantedAutorities(roles))
                .disabled(userEntity.getDisabled())
                .accountLocked(userEntity.getLocked())
                .disabled(userEntity.getDisabled())
                .build();
    }

    private String[] getAuthorities(String role) {
        if ("ADMIN".equals(role) || "CUSTOMER".equals(role)) {
            return new String[]{"random_order"};
        }
        return new String[] {};
    }

    private List<GrantedAuthority> grantedAutorities(String[] roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles.length);
        for (String role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+ role));
            for (String authority : getAuthorities(role)) {
                grantedAuthorities.add(new SimpleGrantedAuthority(authority));
            }
        }
        return grantedAuthorities;
    }
}
