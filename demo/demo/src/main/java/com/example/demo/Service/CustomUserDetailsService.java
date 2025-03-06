package com.example.demo.Service;

import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.model.User;

import java.util.List;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 Searching for user: " + username);

        User userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    System.out.println("❌ User not found: " + username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        System.out.println("✅ User found: " + userEntity.getUsername());
        System.out.println("🔑 Stored Password in DB: " + userEntity.getPassword());
        System.out.println("🔒 Roles: " + userEntity.getPermission());

        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles((userEntity.getPermission()))
                .build();
    }

    private String[] parseRoles(List<String> rolesList) {
        return rolesList.toArray(new String[0]);
    }
}
