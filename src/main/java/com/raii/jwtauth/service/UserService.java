package com.raii.jwtauth.service;

import com.raii.jwtauth.model.User;
import com.raii.jwtauth.repository.UserRepository;
import com.raii.jwtauth.service.exceptions.UserAlreadyExistsWithFieldException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserDetailsService userDetailsService() {
        return login -> repository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
    }

    public User create(User user) throws UserAlreadyExistsWithFieldException {
        if (repository.findByLogin(user.getLogin()).isPresent()) {
            throw new UserAlreadyExistsWithFieldException("login");
        }

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsWithFieldException("email");
        }

        return repository.save(user);
    }
}
