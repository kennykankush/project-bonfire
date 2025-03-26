package ai.lumidah.bonfire_v1_app_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.lumidah.bonfire_v1_app_backend.dto.LoginRequest;
import ai.lumidah.bonfire_v1_app_backend.dto.LoginResponse;
import ai.lumidah.bonfire_v1_app_backend.dto.RegisterRequest;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.service.AuthenticationService;
import ai.lumidah.bonfire_v1_app_backend.service.JwtService;
import ai.lumidah.bonfire_v1_app_backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private JwtService jwtService;
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService){
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        System.out.println(request);
        System.out.println("registering has been penetrated");

        //check if username is taken
        boolean isUsernameTake = userService.existByUsername(request.getUsername());
        boolean isEmailTaken = userService.existByEmail(request.getEmail());
        Map<String, String> response = new HashMap<>();
        if (isUsernameTake){
            System.out.println("USERNAME TAKEN");
            response.put("error", "username_taken");
            response.put("message", "Username is taken");
            
        }

        if (isEmailTaken){
            System.out.println("EMAIL TAKEN");
            response.put("error2", "email_taken");
            response.put("message2", "Email is taken");

        }

        if(!response.isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }


        User registeredUser = authenticationService.register(request);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody LoginRequest request){

        User authenticatedUser = authenticationService.authenticate(request);

        if (authenticatedUser == null){
            Map<String, String> response = new HashMap<>();
            response.put("error","bad_credentials");
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
        
    }
    



 

    
}
