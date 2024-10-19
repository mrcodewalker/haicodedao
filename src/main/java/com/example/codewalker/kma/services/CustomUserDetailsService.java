//package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.User;
import com.example.codewalker.kma.repositories.UserRepository; // Import repository của bạn
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;

//@Service
//public class CustomUserDetailsService implements UserDetailsService {

//    @Autowired
//    private UserRepository userRepository; // Tiêm repository
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // Tìm người dùng trong cơ sở dữ liệu
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
//
//        // Chuyển đổi User thành UserDetails
//        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
//        userBuilder.password(user.getPassword());
//        userBuilder.roles(user.getRole().getRoleName()); // Nếu bạn có roles cho người dùng
//
//        return userBuilder.build();
//    }
//}
