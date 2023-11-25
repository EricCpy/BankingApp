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
import org.springframework.transaction.annotation.Transactional;
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
                false,
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
                false,
                isAdmin && creationRequest.emailVerified());
        userRepository.save(newUser);
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.info("User account does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user account does not exist!");
        }
        return user.get();
    }

    public void verifyUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.info("Can not verify account, because account does not exist!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user account does not exist!");
        }
        if (user.get().isEmailVerified()) {
            log.info("Can not verify account, because account is already verified!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Your account is already verified!");
        }

        user.get().setEmailVerified(true);
        userRepository.save(user.get());
    }

    public TokenResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = findUserByEmail(loginRequest.email());

        if (!user.isEmailVerified() || user.isBlocked()) {
            log.info("Can not login, because account is not verified!");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This user account is not verified!");
        }

        return new TokenResponse(loginRequest.email(), jwtUtils.createToken(user));
    }

    public User getUserFromRequest(HttpServletRequest request) {
        Claims claims = jwtUtils.resolveClaims(request);
        String email = claims.getSubject();
        return findUserByEmail(email);
    }

    public TokenResponse refreshSession(HttpServletRequest request) {
        User user = getUserFromRequest(request);
        return new TokenResponse(user.getEmail(), jwtUtils.createToken(user));
    }

    @Transactional
    public void deleteUser(String email) {
        User user = findUserByEmail(email);
        userRepository.delete(user);
    }

    public void changeUserBlockStatus(BlockRequest blockRequest) {
        User user = findUserByEmail(blockRequest.email());
        user.setBlocked(blockRequest.block());
        userRepository.save(user);
    }

    public void editUser(String email, EditRequest editRequest, List<String> authorities) {
        User user = findUserByEmail(email);
        boolean isAdmin = authorities.stream().anyMatch(r -> r.equals("ADMIN"));
        user.setFirstName(editRequest.firstName() != null ? editRequest.firstName() : user.getFirstName());
        user.setLastName(editRequest.lastName() != null ? editRequest.lastName() : user.getLastName());
        user.setRole(isAdmin ? getRoleFromString(editRequest.role()) : user.getRole());
        user.setEmail(editRequest.email() != null ? editRequest.email() : user.getEmail());
        user.setEmailVerified(isAdmin && editRequest.emailVerified() || user.isEmailVerified());
        userRepository.save(user);
    }

    public void resetUserPassword(ResetPasswordRequest resetPasswordRequest, User user) {
        if (passwordEncoder.matches(user.getPassword(),passwordEncoder.encode(resetPasswordRequest.oldPassword()))) {
            log.info("Old password is incorrect!");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.newPassword()));
        userRepository.save(user);
    }

    public UserResponse getUser(String email) {
        return new UserResponse(findUserByEmail(email));
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAllUserResponses();
    }

    private UserRole getRoleFromString(String role) {
        Map<String, UserRole> roles = Map.of("admin", UserRole.ADMIN,
                "support", UserRole.SUPPORT,
                "employee", UserRole.EMPLOYEE,
                "customer", UserRole.CUSTOMER);
        if (role == null) role = "";
        return roles.getOrDefault(role.toLowerCase(), UserRole.CUSTOMER);
    }
}
