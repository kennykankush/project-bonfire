package ai.lumidah.bonfire_v1_app_backend.service;

import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import ai.lumidah.bonfire_v1_app_backend.model.BonfireUnlock;
import ai.lumidah.bonfire_v1_app_backend.model.MongoUser;
import ai.lumidah.bonfire_v1_app_backend.model.NodeUnlock;
import ai.lumidah.bonfire_v1_app_backend.repository.MongoUserRepository;

@Service
public class MongoUserService {


    @Autowired
    private MongoUserRepository mongoUserRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<MongoUser> findById(Long id){
        return mongoUserRepository.findById(id);
    }

    public void updateNode(Long id, NodeUnlock nodeUnlock){
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet("node_unlock", nodeUnlock);

        mongoTemplate.updateFirst(query,update,MongoUser.class);
        
    }

    public void updateBonfire(Long id, BonfireUnlock bonfireUnlock){
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet("bonfire_unlock", bonfireUnlock);

        mongoTemplate.updateFirst(query,update,MongoUser.class);
        
    }

    public MongoUser save(MongoUser user){
        return mongoUserRepository.save(user);
    }

    public Document getLatestActivity(Long userId){
        System.out.println(userId);

        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("_id").is(userId)),
            Aggregation.unwind("node_unlock"),
            Aggregation.sort(Sort.Direction.DESC, "node_unlock.timestamp"),
            Aggregation.limit(1),
            Aggregation.project()
                .and("node_unlock.timestamp").as("latest_timestamp")
                .and("node_unlock.nodeId").as("nodeId")
                .andExclude("_id")
        );
        
        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "users", Document.class);
        Document document = results.getUniqueMappedResult();
        System.out.println(document);
        
        return results.getUniqueMappedResult();
    }



}


    

