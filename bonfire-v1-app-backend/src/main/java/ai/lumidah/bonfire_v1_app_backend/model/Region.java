package ai.lumidah.bonfire_v1_app_backend.model;

import java.util.Arrays;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Region {

    public String name;
    public double[] coordinate;
    
    @Override
    public String toString() {
        return "Region [name=" + name + ", coordinate=" + Arrays.toString(coordinate) + "]";
    }
    
}
