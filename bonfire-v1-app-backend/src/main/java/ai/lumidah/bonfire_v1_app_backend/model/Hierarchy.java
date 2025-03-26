package ai.lumidah.bonfire_v1_app_backend.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Hierarchy {

    public Country country;
    public Region region;
    public Place place;
    public Locality locality;
    
    @Override
    public String toString() {
        return "Hierarchy [country=" + country + ", region=" + region + ", place=" + place + ", locality=" + locality
                + "]";
    }
    
}
