package ai.lumidah.bonfire_v1_app_backend.model;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class BonfireUnlock {

    private String bonfireId;
    private String nodeId;
    private Instant timeStamp;
    
    @Override
    public String toString() {
        return "BonfireUnlock [bonfireId=" + bonfireId + ", nodeId=" + nodeId + ", timeStamp=" + timeStamp + "]";
    }
    
}
