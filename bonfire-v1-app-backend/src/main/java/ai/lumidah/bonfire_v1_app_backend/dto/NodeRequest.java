package ai.lumidah.bonfire_v1_app_backend.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NodeRequest {

    private String locality;
    private double localityLng; 
    private double LocalityLat;

    private String place;
    private double placeLng;
    private double placeLat;

    private String region;
    private double regionLng;
    private double regionLat;

    private String country;
    private String countryCode;
    private String countryCode3;

    private LocalDateTime timestamp;    



}
