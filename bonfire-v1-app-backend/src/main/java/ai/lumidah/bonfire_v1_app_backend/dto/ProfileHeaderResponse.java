package ai.lumidah.bonfire_v1_app_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileHeaderResponse {

    private String username;
    private String country;
    private String bio;
    private String header;
    private String profilePicture;
    private String lastActivityCountry;
    private String lastActivityRelativeTime;
    
    @Override
    public String toString() {
        return "ProfileHeaderResponse [username=" + username + ", country=" + country + ", bio=" + bio
                + ", profilePicture=" + profilePicture + ", lastActivityCountry=" + lastActivityCountry
                + ", lastActivityRelativeTime=" + lastActivityRelativeTime + "]";
    }
        
}
