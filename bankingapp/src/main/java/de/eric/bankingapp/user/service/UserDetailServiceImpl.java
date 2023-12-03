package de.eric.bankingapp.user.service;

import de.eric.bankingapp.user.model.User;
import de.eric.bankingapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            log.info("User does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong!");
        }

        if(user.get().isBlocked() || !user.get().isEmailVerified()) {
            log.info("User with blocked/unverified account tried to send request!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong!");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.get().getEmail())
                .password(user.get().getPassword())
                .roles(user.get().getRole().toString())
                .build();
    }
}
