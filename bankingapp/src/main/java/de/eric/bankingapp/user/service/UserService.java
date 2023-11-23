package de.eric.bankingapp.user.service;

import de.eric.bankingapp.config.auth.JwtUtils;
import de.eric.bankingapp.registration.model.RegistrationRequest;
import de.eric.bankingapp.user.model.*;
import de.eric.bankingapp.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    public User registerUser(RegistrationRequest registrationRequest) {
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

    public void createUser(CreationRequest creationRequest, List<String> authorities) {
        String email = creationRequest.email();
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            log.info("Email " + email + " already exists");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " + email + " already exists");
        }

        boolean isAdmin = authorities.stream().anyMatch(r -> r.equals("ADMIN"));
        User newUser = new User(null,
                email,
                creationRequest.firstName(),
                creationRequest.lastName(),
                passwordEncoder.encode(creationRequest.password()),
                isAdmin ? getRoleFromString(creationRequest.role()) : UserRole.CUSTOMER,
                isAdmin && creationRequest.emailVerified());
        userRepository.save(newUser);
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

    public TokenResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> user = userRepository.findByEmail(loginRequest.email());
        if(user.isEmpty()) {
            log.info("Can not login, because account does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user account does not exist!");
        }

        if(!user.get().isEmailVerified()) {
            log.info("Can not login, because account is not verified!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This user account is not verified!");
        }

        return new TokenResponse(loginRequest.email(), jwtUtils.createToken(user.get()));
    }

    public TokenResponse refreshSession(HttpServletRequest request) {
        Claims claims = jwtUtils.resolveClaims(request);
        String email = claims.getSubject();
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            log.info("Can not login, because account does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user account does not exist!");
        }
        return new TokenResponse(email, jwtUtils.createToken(user.get()));
    }

    private UserRole getRoleFromString(String role) {
        Map<String, UserRole> roles = Map.of("admin", UserRole.ADMIN,
                                            "support", UserRole.SUPPORT,
                                            "employee",UserRole.EMPLOYEE,
                                            "customer", UserRole.CUSTOMER);
        if(role == null) role = "";
        return roles.getOrDefault(role.toLowerCase() ,UserRole.CUSTOMER);
    }
}
