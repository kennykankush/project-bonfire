package ai.lumidah.bonfire_v1_app_backend.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateSettings {

    private String bio;
    private MultipartFile profilePicture;
    private MultipartFile headerPicture;
    
    @Override
    public String toString() {
        return "UpdateSettings [bio=" + bio + ", profilePicture=" + profilePicture + ", headerPicture=" + headerPicture
                + "]";
    }

}
