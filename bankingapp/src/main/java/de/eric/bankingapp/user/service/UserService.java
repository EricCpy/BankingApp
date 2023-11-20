package de.eric.bankingapp.user.service;

import de.eric.bankingapp.config.auth.JwtUtils;
import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.user.model.LoginRequest;
import de.eric.bankingapp.user.model.LoginResponse;
import de.eric.bankingapp.user.model.User;
import de.eric.bankingapp.user.model.UserRole;
import de.eric.bankingapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    public User createUser(RegistrationRequest registrationRequest) {
        String email = registrationRequest.email();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.info("Email " + email + " already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " + email + " already exists");
        }
        User newUser = new User(null,
                email,
                registrationRequest.firstName(),
                registrationRequest.lastName(),
                passwordEncoder.encode(registrationRequest.password()),
                UserRole.CUSTOMER,
                false);
        return userRepository.save(newUser);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void verifyAccount(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            log.info("Can not verify account, because account does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user account does not exist!");
        }
        if (user.get().isEmailVerified()){
            log.info("Can not verify account, because account is already verified!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your account is already verified!");
        }

        user.get().setEmailVerified(true);
        userRepository.save(user.get());
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> user = userRepository.findByEmail(loginRequest.email());
        if(user.isEmpty()) {
            log.info("Can not login, because account does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user account does not exist!");
        }

        return new LoginResponse(loginRequest.email(), jwtUtils.createToken(user.get()));
    }
}
