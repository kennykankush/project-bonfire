package ai.lumidah.bonfire_v1_app_backend.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ai.lumidah.bonfire_v1_app_backend.model.MongoUser;

@Repository
public interface MongoUserRepository extends MongoRepository<MongoUser, Long> {

    Optional<MongoUser> findByUsername(String username);

}
