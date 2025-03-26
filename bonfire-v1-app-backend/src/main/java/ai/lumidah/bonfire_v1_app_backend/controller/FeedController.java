package ai.lumidah.bonfire_v1_app_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.lumidah.bonfire_v1_app_backend.dto.FeedResponse;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.FeedContentDTO;
import ai.lumidah.bonfire_v1_app_backend.model.BonfireUnlock;
import ai.lumidah.bonfire_v1_app_backend.model.Follow;
import ai.lumidah.bonfire_v1_app_backend.model.MongoBonfire;
import ai.lumidah.bonfire_v1_app_backend.model.MongoNode;
import ai.lumidah.bonfire_v1_app_backend.model.MongoUser;
import ai.lumidah.bonfire_v1_app_backend.model.NodeUnlock;
import ai.lumidah.bonfire_v1_app_backend.service.MongoBonfireService;
import ai.lumidah.bonfire_v1_app_backend.service.MongoNodeService;
import ai.lumidah.bonfire_v1_app_backend.service.MongoUserService;
import ai.lumidah.bonfire_v1_app_backend.service.SocialService;
import ai.lumidah.bonfire_v1_app_backend.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api")
public class FeedController {

    @Autowired
    private SocialService socialService;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoUserService mongoUserService;

    @Autowired
    private MongoNodeService mongoNodeService;

    @Autowired
    private MongoBonfireService mongoBonfireService;

    @GetMapping("/feed/{username}")
    public ResponseEntity<Object> getUserFeed(@PathVariable String username) {
    Long userId = userService.getUserId(username);
    List<Long> followingIds = socialService.getFollowingList(userId);
    FeedResponse feedResponse = new FeedResponse();

    if (followingIds.isEmpty()) {
        return ResponseEntity.ok().body(feedResponse);
    }
    
    List<FeedContentDTO> feedStuffs = new ArrayList<>();
    PrettyTime prettyTime = new PrettyTime();
    
    for (Long followingId : followingIds) {
        Optional<MongoUser> oMongoUser = mongoUserService.findById(followingId);
        String photo = userService.getPFPbyId(followingId);
        if (oMongoUser.isPresent()) {
            MongoUser mongoUser = oMongoUser.get();
            
            for (BonfireUnlock bonfireUnlock : mongoUser.getBonfireUnlocks()) {
                Optional<MongoBonfire> oBonfire = mongoBonfireService.findById(bonfireUnlock.getBonfireId());
                if (oBonfire.isPresent()) {
                    MongoBonfire bonfire = oBonfire.get();
                    
                    Date date = Date.from(bonfireUnlock.getTimeStamp());
                    
                    FeedContentDTO feedContent = new FeedContentDTO();
                    feedContent.setProfilePicture(photo);
                    feedContent.setUsername(mongoUser.getUsername());

                    feedContent.setType("bonfire");
                    feedContent.setContext(bonfire.getMaki());
                    feedContent.setBodyLandmark(bonfire.getName());
                    feedContent.setTimestamp(date);
                    feedContent.setRecentcy(prettyTime.format(date));
                    
                    feedStuffs.add(feedContent);
                }
            }
            
            for (NodeUnlock nodeUnlock : mongoUser.getNodeUnlocks()) {
                Optional<MongoNode> oNode = mongoNodeService.findById(nodeUnlock.getNodeId());
                if (oNode.isPresent()) {
                    MongoNode node = oNode.get();
                    
                    Date date = Date.from(nodeUnlock.getTimestamp());
                    
                    FeedContentDTO feedContent = new FeedContentDTO();
                    feedContent.setProfilePicture(photo);
                    feedContent.setUsername(mongoUser.getUsername());
                    feedContent.setType("node");
                    feedContent.setContext(node.getHierarchy().getCountry().getCountryCode());
                    feedContent.setBodyLandmark(node.getHierarchy().getCountry().name + ", " + node.getHierarchy().getRegion().getName());
                    feedContent.setTimestamp(date);
                    feedContent.setRecentcy(prettyTime.format(date));
                    
                    feedStuffs.add(feedContent);
                }
            }
        }
    }
    
    feedStuffs.sort((item1, item2) -> item2.getTimestamp().compareTo(item1.getTimestamp()));
    
    if (feedStuffs.size() > 10) {
        feedStuffs = feedStuffs.subList(0, 10);
    }
    
    feedResponse.setFeedContent(feedStuffs);
    return ResponseEntity.ok(feedStuffs);
}
    
  
}
