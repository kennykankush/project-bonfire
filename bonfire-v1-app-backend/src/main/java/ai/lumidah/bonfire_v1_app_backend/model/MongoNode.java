package ai.lumidah.bonfire_v1_app_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "nodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MongoNode {

    @Id
    private String id;
    private String name;
    private Hierarchy hierarchy;
    
    @Override
    public String toString() {
        return "MongoNode [id=" + id + ", name=" + name + ", hierarchy=" + hierarchy + "]";
    }

}
