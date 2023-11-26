package de.eric.bankingapp.user.repository;

import de.eric.bankingapp.user.model.User;
import de.eric.bankingapp.user.model.response.UserResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u")
    List<UserResponse> findAllUserResponses();
}
