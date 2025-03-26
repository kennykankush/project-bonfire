package ai.lumidah.bonfire_v1_app_backend.model;

import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "bonfires")
@NoArgsConstructor
@Setter
@Getter
public class MongoBonfire {

    @Id
    private String id;
    private String name;
    @Field("node_id")
    private String nodeId;
    private String maki;
    private String category;
    private String type;
    private double[] coordinates;
    
    @Override
    public String toString() {
        return "MongoBonfire [id=" + id + ", name=" + name + ", nodeId=" + nodeId + ", maki=" + maki + ", category="
                + category + ", type=" + type + ", coordinates=" + Arrays.toString(coordinates) + "]";
    }


    
}
