package ai.lumidah.bonfire_v1_app_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.lumidah.bonfire_v1_app_backend.dto.FollowRequest;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.service.SocialService;
import ai.lumidah.bonfire_v1_app_backend.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/social")
public class SocialController {

    @Autowired
    private UserService userService;

    @Autowired
    private SocialService socialService;

    @PostMapping("/follow")
    public ResponseEntity<Object> follow(@RequestBody FollowRequest followRequest) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Long sqlId = userService.getUserId(userName);
        Long followingId = userService.getUserId(followRequest.getFollowing());

        socialService.captureFollow(sqlId, followingId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "user has successfully followed");
        response.put("status", "success");

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<Object> unfollow(@RequestBody FollowRequest followRequest){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Long sqlId = userService.getUserId(userName);
        Long followingId = userService.getUserId(followRequest.getFollowing());

        socialService.retractFollow(sqlId, followingId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "user has successfully unfollowed");
        response.put("status", "success");
        

        return ResponseEntity.ok().body(response);

    }

    @GetMapping("/isfollowing/{username}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable String username) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Long sqlId = userService.getUserId(userName);
        Long followingId = userService.getUserId(username);

        Boolean isFollowing = socialService.isFollowing(sqlId, followingId);

        return ResponseEntity.ok().body(isFollowing);
    }

    @GetMapping("/stats/{username}")
    public ResponseEntity<Object> stats(@PathVariable String username){
        Long sqlId = userService.getUserId(username);

        int following = socialService.getFollowing(sqlId);
        int followers = socialService.getFollowers(sqlId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("following", following);
        stats.put("followers", followers);

        //get LAST ACTIVE
        

        return ResponseEntity.ok(stats);

    }
    
    
    
}
