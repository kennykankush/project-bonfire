package ai.lumidah.bonfire_v1_app_backend.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ai.lumidah.bonfire_v1_app_backend.dto.LoginRequest;
import ai.lumidah.bonfire_v1_app_backend.dto.RegisterRequest;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.repository.UserRepository;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager
    ){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User register(RegisterRequest request){
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCountry(request.getCountry());

        return userRepository.save(user);
        
    }

    public User authenticate(LoginRequest request){
        try {

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken
            (request.getUsername(), 
            request.getPassword()
            )
        );

        return userRepository.findByUsername(request.getUsername()).orElse(null);
        } catch (AuthenticationException e){
            return null;
        }

    }

    
    
}
