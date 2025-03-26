package ai.lumidah.bonfire_v1_app_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EmailController {

    @Autowired
    private JavaMailSender emailSender;

    @PostMapping("/feedback/email")
    public ResponseEntity<Object> doEmail(@RequestBody String body){

         SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("hadidotmulia@gmail.com");
            message.setSubject("feedback for bonfire");
            message.setText(body);
            
            emailSender.send(message);
            
            System.out.println("Email sent successfully");

            Map<String, String> response = new HashMap<>();
            response.put("Success","done");
            return ResponseEntity.ok().body(response);

    }
    
}
