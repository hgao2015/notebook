package com.ainfo.mynotes.service;

import com.ainfo.mynotes.domain.User;
import com.ainfo.mynotes.dto.UserCredential;
import com.ainfo.mynotes.exception.MyNoteException;
import com.ainfo.mynotes.repository.UserRepository;
import com.ainfo.mynotes.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String signin(UserCredential userCredential) {
        try {
            String userName = userCredential.getUsername();
            String password = userCredential.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
            return jwtTokenProvider.createToken(userName, userRepository.findByUsername(userName).getRoles());
        } catch (AuthenticationException e) {
            throw new MyNoteException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signup(User user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new MyNoteException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public User search(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new MyNoteException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    public User whoami(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }
}
