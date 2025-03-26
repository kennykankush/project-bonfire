package ai.lumidah.bonfire_v1_app_backend.utility;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestCountryLngLatFetch {

    public double[] getCountryCoordinatesByCca3(String cca) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://restcountries.com/v3.1/alpha/" + cca;
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            
            JsonNode countryNode = rootNode.get(0);
            JsonNode latlngNode = countryNode.path("latlng");
                
            double lat = latlngNode.get(0).asDouble();
            double lng = latlngNode.get(1).asDouble();
                    
            return new double[] {lng,lat };

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
