package ai.lumidah.bonfire_v1_app_backend.dto;

import java.util.List;

import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.FeedContentDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedResponse {

    List<FeedContentDTO> feedContent;
    
}
