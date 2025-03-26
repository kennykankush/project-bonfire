package ai.lumidah.bonfire_v1_app_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ai.lumidah.bonfire_v1_app_backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email); 
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findTop10ByUsernameStartingWithIgnoreCase(String username);

}
