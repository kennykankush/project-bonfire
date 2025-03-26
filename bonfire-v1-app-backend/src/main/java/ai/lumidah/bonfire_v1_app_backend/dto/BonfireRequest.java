package ai.lumidah.bonfire_v1_app_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonfireRequest {

    private double poiLng;
    private double poiLat;
    private String maki;
    private String category;
    private String type;
    private String name;
    private String nodeId;
    
}
