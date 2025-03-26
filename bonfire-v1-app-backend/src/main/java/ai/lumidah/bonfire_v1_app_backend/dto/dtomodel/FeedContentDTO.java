package ai.lumidah.bonfire_v1_app_backend.dto.dtomodel;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedContentDTO {

    private String username;
    private String profilePicture;
    private String type;
    private String context;
    private String bodyLandmark;
    private Date timestamp;
    private String recentcy;
    
}
