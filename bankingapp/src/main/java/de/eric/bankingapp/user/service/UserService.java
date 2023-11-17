package de.eric.bankingapp.user.service;

import de.eric.bankingapp.user.model.Account;
import de.eric.bankingapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;



    public Account registerUser() {
        // find user by mail if existing return user already exists
        /*Optional<User> user = this.findByEmail(request.email());
        if (user.isPresent()){
            throw new UserAlreadyExistsException(
                    "User with email "+request.email() + " already exists");
        }
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());
        return userRepository.save(newUser);*/
        return null;
    }

}
