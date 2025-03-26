package ai.lumidah.bonfire_v1_app_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ai.lumidah.bonfire_v1_app_backend.dto.NodeRequest;
import ai.lumidah.bonfire_v1_app_backend.model.BonfireUnlock;
import ai.lumidah.bonfire_v1_app_backend.model.Country;
import ai.lumidah.bonfire_v1_app_backend.model.Hierarchy;
import ai.lumidah.bonfire_v1_app_backend.model.Locality;
import ai.lumidah.bonfire_v1_app_backend.model.MongoBonfire;
import ai.lumidah.bonfire_v1_app_backend.model.MongoNode;
import ai.lumidah.bonfire_v1_app_backend.model.MongoUser;
import ai.lumidah.bonfire_v1_app_backend.model.NodeTree;
import ai.lumidah.bonfire_v1_app_backend.model.NodeUnlock;
import ai.lumidah.bonfire_v1_app_backend.model.Place;
import ai.lumidah.bonfire_v1_app_backend.model.Region;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.repository.MongoNodeRepository;
import ai.lumidah.bonfire_v1_app_backend.service.MongoBonfireService;
import ai.lumidah.bonfire_v1_app_backend.service.MongoNodeService;
import ai.lumidah.bonfire_v1_app_backend.service.MongoUserService;
import ai.lumidah.bonfire_v1_app_backend.service.UserService;
import ai.lumidah.bonfire_v1_app_backend.utility.NodeTreeBuilder;
import ai.lumidah.bonfire_v1_app_backend.utility.RestCountryLngLatFetch;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/nodes")
public class NodeController {


    @Autowired
    private MongoUserService mongoUserService;

    @Autowired
    private MongoNodeService mongoNodeService;

    @Autowired
    private MongoBonfireService mongoBonfireService;

    @Autowired 
    private UserService userService;

    @Autowired
    private RestCountryLngLatFetch restCountryLngLatFetch;


        @PostMapping("/update")
        public ResponseEntity<Object> updateUserNode(@RequestBody NodeRequest request) {

            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Name fetched");
            String nodeId = request.getLocality().toLowerCase() + '_' + request.getRegion().toLowerCase(); //akibara_tokyo

            //GET SQL USERID

            Optional<User> userOptional = userService.getUser(userName);
            Long sqlId = null;
            StringBuilder response = new StringBuilder();

            if (userOptional.isPresent()){
                User user = userOptional.get();
                sqlId = user.getId();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in MYSQL");
            };

            //MONGO

            Optional<MongoUser> mongoUser = mongoUserService.findById(sqlId);
            Optional<MongoNode> mongoNode = mongoNodeService.findById(nodeId);
            MongoUser currentUser;

            if (mongoUser.isPresent()){ 
                currentUser = mongoUser.get();
                response.append("User is present.");
            } else {
                currentUser = new MongoUser();
                currentUser.setId(sqlId);
                currentUser.setUsername(userName);
                mongoUserService.save(currentUser);
                response.append("User is not present. Adding user to database");
            }

            System.out.println(currentUser);

            if (mongoNode.isPresent()){    
                MongoNode existingNode = mongoNode.get();

                NodeUnlock nodeUnlock = new NodeUnlock();
                nodeUnlock.setNodeId(existingNode.getId());
                nodeUnlock.setTimestamp(Instant.now());

                mongoUserService.updateNode(currentUser.getId(), nodeUnlock);
                response.append("Node exists in Node Collection. Will refer from that and update User's node");
                System.out.println(existingNode);

            } else {

                MongoNode newNode = new MongoNode();

                newNode.setId(nodeId);
                newNode.setName(request.getLocality());
            
                Country country = new Country();
                Region region = new Region();
                Place place = new Place();
                Locality locality = new Locality();
                
                country.setName(request.getCountry());
                country.setCountryCode(request.getCountryCode());
                country.setCountryCode3(request.getCountryCode3());

                double[] countryLngLat = restCountryLngLatFetch.getCountryCoordinatesByCca3(request.getCountryCode());

                
                country.setCoordinate(countryLngLat);

                region.setName(request.getRegion());
                region.setCoordinate(new double[]{request.getRegionLng(), request.getRegionLat()});

                place.setName(request.getPlace());
                place.setCoordinate(new double[]{request.getPlaceLng(), request.getPlaceLat()});

                locality.setName(request.getLocality());
                locality.setCoordinate(new double[]{request.getLocalityLng(), request.getLocalityLat()});

                Hierarchy hierarchy = new Hierarchy(country, region, place, locality);

                newNode.setHierarchy(hierarchy);

                mongoNodeService.save(newNode);

                NodeUnlock nodeUnlock = new NodeUnlock();
                nodeUnlock.setNodeId(newNode.getId());
                nodeUnlock.setTimestamp(Instant.now());
                mongoUserService.updateNode(currentUser.getId(), nodeUnlock);
                response.append("Node doesnt exists in Node Collection. Will add to Collection and update to user's node");

                System.out.println(newNode);
            }

            Map<String, String> map = new HashMap<>();
            map.put("message", response.toString());

        return ResponseEntity.ok().body(map);

    }   

        @GetMapping("/collections/{userName}")
        public ResponseEntity<Object> getUserNode(@PathVariable String userName){
            System.out.println("Node Controller Reached");

            NodeTreeBuilder nodeTreeBuilder = new NodeTreeBuilder();

            Long sqlId = userService.getUserId(userName);

            Optional<MongoUser> ouserLog = mongoUserService.findById(sqlId);

            if (ouserLog.isEmpty()){
                return ResponseEntity.status(404).body("User has not yet claimed");
            }

            MongoUser user = ouserLog.get();

            List<MongoNode> mongoNodes = new ArrayList<>();

            for (NodeUnlock nodeUnlock: user.getNodeUnlocks()){

                Optional<MongoNode> omongoNode = mongoNodeService.findById(nodeUnlock.getNodeId());

                MongoNode mongoNode = omongoNode.get();

                mongoNodes.add(mongoNode);
            }

            NodeTree nodeTree = nodeTreeBuilder.build(mongoNodes);

            List<MongoBonfire> mongoBonfires = new ArrayList<>();

            for (BonfireUnlock bonfireUnlock: user.getBonfireUnlocks()){

                Optional<MongoBonfire> omongoBonfire = mongoBonfireService.findById(bonfireUnlock.getBonfireId());

                MongoBonfire mongoBonfire = omongoBonfire.get();

                mongoBonfires.add(mongoBonfire);

            }

            NodeTree attachBonfires = nodeTreeBuilder.attachBonfires(nodeTree, mongoBonfires);

            return ResponseEntity.ok().body(attachBonfires); 
            

            //https://mkyong.com/java/convert-java-objects-to-json-with-jackson/

            // ObjectMapper objectMapper = new ObjectMapper();

            // try {            
            //     String json = objectMapper.writeValueAsString(nodeTree);
            //     System.out.println("reached");
            //     return ResponseEntity.ok().body(json);

            // } catch (JsonProcessingException e) {
            //     return ResponseEntity.status(404).body(e.getStackTrace());
            
            // }

        }
}
