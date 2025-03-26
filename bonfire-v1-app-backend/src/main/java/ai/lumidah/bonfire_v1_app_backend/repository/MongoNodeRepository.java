package ai.lumidah.bonfire_v1_app_backend.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ai.lumidah.bonfire_v1_app_backend.model.MongoNode;

@Repository
public interface MongoNodeRepository extends MongoRepository<MongoNode, String> {


}
