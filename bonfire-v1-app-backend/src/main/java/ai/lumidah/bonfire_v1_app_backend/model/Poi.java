package ai.lumidah.bonfire_v1_app_backend.model;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "pois")
@Getter
@Setter
public class Poi {

    @Id
    private String id;
    private String type;

    @Field("properties.name")
    private String name;

    @Field("properties.name:en")
    private String nameEn;

    @GeoSpatialIndexed
    private GeoJsonPoint geometry;

    
}
