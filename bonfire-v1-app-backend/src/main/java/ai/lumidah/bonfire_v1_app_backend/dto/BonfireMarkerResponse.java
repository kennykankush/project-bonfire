package ai.lumidah.bonfire_v1_app_backend.dto;

import java.util.List;

import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.BonfireDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.CountryDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.LocalityDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.PlaceDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.RegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BonfireMarkerResponse {

    private List<CountryDTO> countries;

    private List<RegionDTO> regions;
    private List<PlaceDTO> places;
    private List<LocalityDTO> localities;
    private List<BonfireDTO> bonfires;

}
