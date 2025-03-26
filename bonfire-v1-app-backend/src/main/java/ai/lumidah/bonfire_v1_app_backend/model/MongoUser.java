package ai.lumidah.bonfire_v1_app_backend.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MongoUser {

    @Id
    private Long id;
    private String username;
    @Field("node_unlock")
    private List<NodeUnlock> nodeUnlocks = new ArrayList<>();
    @Field("bonfire_unlock")
    private List<BonfireUnlock> bonfireUnlocks = new ArrayList<>();
    
    @Override
    public String toString() {
        return "MongoUser [id=" + id + ", username=" + username + ", nodeUnlock=" + nodeUnlocks + ", bonfireUnlock="
                + bonfireUnlocks + "]";
    }
     
}
