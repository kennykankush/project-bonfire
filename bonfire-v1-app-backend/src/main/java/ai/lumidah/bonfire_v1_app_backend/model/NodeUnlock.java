package ai.lumidah.bonfire_v1_app_backend.model;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NodeUnlock {

    private String nodeId;
    private Instant timestamp;
    
    @Override
    public String toString() {
        return "NodeUnlock [nodeId=" + nodeId + ", timestamp=" + timestamp + "]";
    }
    
}
