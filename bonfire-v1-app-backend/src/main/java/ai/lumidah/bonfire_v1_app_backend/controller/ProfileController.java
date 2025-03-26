package ai.lumidah.bonfire_v1_app_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.lumidah.bonfire_v1_app_backend.dto.ProfileHeaderResponse;
import ai.lumidah.bonfire_v1_app_backend.model.MongoNode;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.service.MongoNodeService;
import ai.lumidah.bonfire_v1_app_backend.service.MongoUserService;
import ai.lumidah.bonfire_v1_app_backend.service.UserService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.bson.Document;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;


@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private MongoUserService mongoUserService;

    @Autowired
    private MongoNodeService mongoNodeService;

    @GetMapping("/{username}")
    public ResponseEntity<Object> getProfile(@PathVariable String username) {

        Optional<User> oUser = userService.getUser(username);

        if (oUser.isPresent()) {
            User user = oUser.get();
            ProfileHeaderResponse response = new ProfileHeaderResponse();
            response.setUsername(user.getUsername());
            response.setProfilePicture(user.getProfilePictureUrl());
            response.setBio(user.getBio());
            response.setCountry(user.getCountry());
            response.setHeader(user.getHeaderUrl());
    
            Document lastActivity = mongoUserService.getLatestActivity(user.getId());
    
            if (lastActivity != null) {
                Date timestamp = lastActivity.getDate("latest_timestamp");
                String nodeId = lastActivity.getString("nodeId");
    
                Optional<MongoNode> oNode = mongoNodeService.findById(nodeId);
            
                if (oNode.isPresent()) {
                    MongoNode mongoNode = oNode.get();
                    String country = mongoNode.getHierarchy().getCountry().getName();
    
                    //https://www.baeldung.com/java-calculate-time-ago 
                    //https://www.baeldung.com/java-calculate-time-ago 
                    //https://www.baeldung.com/java-calculate-time-ago 
                    PrettyTime prettyTime = new PrettyTime();
                    String relativeTime = prettyTime.format(timestamp);
    
                    response.setLastActivityCountry(country);
                    response.setLastActivityRelativeTime(relativeTime);
                    System.out.println(response);
                }

            }
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
