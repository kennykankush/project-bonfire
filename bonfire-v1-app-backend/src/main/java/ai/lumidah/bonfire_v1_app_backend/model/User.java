package ai.lumidah.bonfire_v1_app_backend.model;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", unique = true) //DECLARE FROM REGISTRATION
    private String username;

    @Column(name = "password") //DECLARE FROM REGISTRATION
    private String password;

    @Column(name = "email", unique = true) //DECLARE FROM REGISTRATION
    private String email;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)  //PROFILE CONFIGURATION SIDE
    @Column(name = "privacy_setting")
    private PrivacySetting privacySetting = PrivacySetting.PUBLIC;

    @Column(name = "profile_picture_url") //DECLARE FROM REGISTRATION
    private String profilePictureUrl = "https://bonfireproject.sgp1.cdn.digitaloceanspaces.com/default.png";

    @Column(name = "header")
    private String headerUrl = "https://bonfireproject.sgp1.cdn.digitaloceanspaces.com/bonfire_homepage.png";

    @Column(name ="biography", columnDefinition = "TEXT") //PROFILE CONFIGURATION SIDE
    private String bio;

    @Column(name = "country") //DECLARE FROM REGISTRATION
    private String country;

    public enum PrivacySetting {
        PUBLIC, PRIVATE
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of();
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }




 
}
