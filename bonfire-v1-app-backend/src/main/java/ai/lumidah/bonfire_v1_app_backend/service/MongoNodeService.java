package ai.lumidah.bonfire_v1_app_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import ai.lumidah.bonfire_v1_app_backend.model.MongoNode;
import ai.lumidah.bonfire_v1_app_backend.repository.MongoNodeRepository;

@Service
public class MongoNodeService {

    @Autowired
    private MongoNodeRepository mongoNodeRepository;

    public Optional<MongoNode> findById(String id){
        return mongoNodeRepository.findById(id);
    }

    public MongoNode save(MongoNode mongoNode){
        return mongoNodeRepository.save(mongoNode);
    }

 
}
