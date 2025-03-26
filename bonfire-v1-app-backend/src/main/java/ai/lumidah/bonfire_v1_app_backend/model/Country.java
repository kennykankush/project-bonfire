package ai.lumidah.bonfire_v1_app_backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Country {

    public String name;
    public String countryCode;
    public String countryCode3;
    private double[] coordinate;
    
    @Override
    public String toString() {
        return "Country [name=" + name + ", countryCode=" + countryCode + ", countryCode3=" + countryCode3 + "]";
    }
    
}
