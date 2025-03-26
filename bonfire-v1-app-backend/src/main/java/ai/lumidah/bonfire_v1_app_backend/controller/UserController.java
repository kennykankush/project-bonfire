package ai.lumidah.bonfire_v1_app_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import ai.lumidah.bonfire_v1_app_backend.dto.UpdateSettings;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.UserDTO;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.service.S3Service;
import ai.lumidah.bonfire_v1_app_backend.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;


    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
        
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> allUsers(){
        List<User> users = userService.allUsers();

        return ResponseEntity.ok(users);
        
    }

    @GetMapping("/getPFP/{username}")
    public ResponseEntity<Object> getMethodName(@PathVariable String username) {
        System.out.println("PFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEINGPFP FETCHEING");
        String link = userService.getPFP(username);
        Map<String, String> response = new HashMap<>();
        response.put("url",link);
        
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam String query){
        List<User> users = userService.searchUser(query);

        if (users.size() == 0){
            Map<String, String> response = new HashMap<>();
            response.put("error", "not_found");
            response.put("message","No user found");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }  

        List<UserDTO> queriedUsers = new ArrayList<>();

        for(User user: users){
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(user.getUsername());
            userDTO.setCountry(user.getCountry());
            userDTO.setProfilePicture(user.getProfilePictureUrl());
            queriedUsers.add(userDTO);
            
        }

        return ResponseEntity.ok().body(queriedUsers);

        
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updateUserProfile(@ModelAttribute UpdateSettings updateSettings) {
    try {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        
        if (updateSettings.getBio() != null) {
            currentUser.setBio(updateSettings.getBio());
        }
        
        if (updateSettings.getProfilePicture() != null && !updateSettings.getProfilePicture().isEmpty()) {
            try {
                String profileUrl = s3Service.uploadFile(updateSettings.getProfilePicture());
                currentUser.setProfilePictureUrl(profileUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload profile picture: " + e.getMessage()));
            }
        }
        
        if (updateSettings.getHeaderPicture() != null && !updateSettings.getHeaderPicture().isEmpty()) {
            try {
                String headerUrl = s3Service.uploadFile(updateSettings.getHeaderPicture());
                currentUser.setHeaderUrl(headerUrl);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload header picture: " + e.getMessage()));
            }
        }
        
        userService.save(currentUser);

        Map<String, String> response = new HashMap<>();
        response.put("success", "uploaded");
        
        return ResponseEntity.ok().body(response);
        
    } catch (MaxUploadSizeExceededException e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "file_exceeded");
        response.put("message","File size exceeds the maximum allowed size of 1MB");

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
    } catch (Exception e) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "server_error");
        response.put("message","Failed to update profile: " + e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
}
    
    

