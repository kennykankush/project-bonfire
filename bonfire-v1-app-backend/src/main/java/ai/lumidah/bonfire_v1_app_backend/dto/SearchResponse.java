package ai.lumidah.bonfire_v1_app_backend.dto;

import java.util.List;

import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {

    List<UserDTO> users;
    
}
