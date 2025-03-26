package ai.lumidah.bonfire_v1_app_backend.dto.dtomodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalityDTO {

    private String name;
    private String type;
    private double[] lngLat; 
    
}
