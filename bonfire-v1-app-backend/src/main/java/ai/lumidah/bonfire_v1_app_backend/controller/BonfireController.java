package ai.lumidah.bonfire_v1_app_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ai.lumidah.bonfire_v1_app_backend.dto.BonfireMarkerResponse;
import ai.lumidah.bonfire_v1_app_backend.dto.BonfireRequest;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.BonfireDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.CountryDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.LocalityDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.PlaceDTO;
import ai.lumidah.bonfire_v1_app_backend.dto.dtomodel.RegionDTO;
import ai.lumidah.bonfire_v1_app_backend.model.BonfireUnlock;
import ai.lumidah.bonfire_v1_app_backend.model.Hierarchy;
import ai.lumidah.bonfire_v1_app_backend.model.MongoBonfire;
import ai.lumidah.bonfire_v1_app_backend.model.MongoNode;
import ai.lumidah.bonfire_v1_app_backend.model.MongoUser;
import ai.lumidah.bonfire_v1_app_backend.model.NodeUnlock;
import ai.lumidah.bonfire_v1_app_backend.model.User;
import ai.lumidah.bonfire_v1_app_backend.repository.MongoBonfireRepository;
import ai.lumidah.bonfire_v1_app_backend.service.MongoBonfireService;
import ai.lumidah.bonfire_v1_app_backend.service.MongoNodeService;
import ai.lumidah.bonfire_v1_app_backend.service.MongoUserService;
import ai.lumidah.bonfire_v1_app_backend.service.UserService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api")
public class BonfireController {

    @Autowired
    private UserService userService;

    @Autowired
    private MongoUserService mongoUserService;

    @Autowired
    private MongoBonfireService mongoBonfireService;

    @Autowired
    private MongoNodeService mongoNodeService;

    @PostMapping("/bonfire/update")
    public ResponseEntity<Object> updateUserNode(@RequestBody BonfireRequest bonfireRequest) {

        // System.out.println(bonfireRequest);

        // Map<String, Object> response = new HashMap<>();
        // response.put("Controller", bonfireRequest );

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        String bonfireId = bonfireRequest.getName() + "_" + bonfireRequest.getNodeId();
        StringBuilder response = new StringBuilder();
    
        Optional<User> userOptional = userService.getUser(userName);
        Long sqlId = null;

        if (userOptional.isPresent()){
            User user = userOptional.get();
            sqlId = user.getId();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found in MYSQL");
        };

        Optional<MongoUser> mongoUser = mongoUserService.findById(sqlId);
        Optional<MongoBonfire> mongoBonfire = mongoBonfireService.findById(bonfireId);
        MongoUser currentUser;
        

        //GOTTA CHECK IF THE USER HAS UNLOCKED THE AREA

        if (mongoUser.isPresent()){
            currentUser = mongoUser.get();
            List<NodeUnlock> nodeUnlocks = currentUser.getNodeUnlocks();

            // boolean hasUserUnlockNodeYet = nodeUnlocks.stream().anyMatch(nodeUnlock -> nodeUnlock.getNodeId().equals(bonfireRequest.getNodeId()));
            boolean hasUserUnlockNodeYet = nodeUnlocks.stream()
            .peek(nodeUnlock -> System.out.println(nodeUnlock.getNodeId()))
            .anyMatch(nodeUnlock -> nodeUnlock.getNodeId().equals(bonfireRequest.getNodeId()));

            System.out.println("Checking against: " + bonfireRequest.getNodeId());

            if (!hasUserUnlockNodeYet){
                // return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("you really a teapot bruh");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You haven't unlocked this area yet.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You haven't unlocked this area yet."); 
        }

        
        if (mongoBonfire.isPresent()){

            MongoBonfire existingBonfire = mongoBonfire.get();

            BonfireUnlock bonfireUnlock = new BonfireUnlock();
            bonfireUnlock.setBonfireId(bonfireId);
            bonfireUnlock.setNodeId(existingBonfire.getNodeId());
            bonfireUnlock.setTimeStamp(Instant.now());

            mongoUserService.updateBonfire(sqlId, bonfireUnlock);
            response.append("BonfireId exist, will refer from that and save inside users log");
        } else {

            MongoBonfire newBonfire = new MongoBonfire();
            newBonfire.setId(bonfireId);
            newBonfire.setNodeId(bonfireRequest.getNodeId());
            newBonfire.setName(bonfireRequest.getName());
            newBonfire.setMaki(bonfireRequest.getMaki());
            newBonfire.setCategory(bonfireRequest.getCategory());
            newBonfire.setType(bonfireRequest.getType());
            newBonfire.setCoordinates(new double[]{bonfireRequest.getPoiLng(), bonfireRequest.getPoiLat()});

            mongoBonfireService.save(newBonfire);

            BonfireUnlock bonfireUnlock = new BonfireUnlock();
            bonfireUnlock.setBonfireId(newBonfire.getId());
            bonfireUnlock.setNodeId(newBonfire.getNodeId());
            bonfireUnlock.setTimeStamp(Instant.now());

            mongoUserService.updateBonfire(sqlId, bonfireUnlock);
            response.append("BonfireId doesnt exist, its a fresh one so i will add it into a bonfire collection and append to users bonfire claim");
            System.out.println(newBonfire);

        }

        Map<String, String> map = new HashMap<>();
        map.put("message", response.toString());
        
        return ResponseEntity.ok().body(map);
    }
    
    @GetMapping("/bonfire/fetch/{username}")
    public ResponseEntity<Object> getMethodName(@PathVariable String username) {
        Long id = userService.getUserId(username);

        Optional<MongoUser> oUser = mongoUserService.findById(id);


        if (oUser.isPresent()){

            MongoUser user = oUser.get();
            BonfireMarkerResponse bonfireMarkerResponse = new BonfireMarkerResponse();
            List<CountryDTO> countries = new ArrayList<>();
            List<RegionDTO> regions = new ArrayList<>();
            List<PlaceDTO> places = new ArrayList<>();
            List<LocalityDTO> localities = new ArrayList<>();
            List<BonfireDTO> bonfires = new ArrayList<>();

            for (NodeUnlock nodeUnlock: user.getNodeUnlocks()){

                Optional<MongoNode> omongoNode = mongoNodeService.findById(nodeUnlock.getNodeId());

                MongoNode mongoNode = omongoNode.get();
                CountryDTO countryDTO = new CountryDTO();
                RegionDTO regionDTO = new RegionDTO();
                PlaceDTO placeDTO = new PlaceDTO();
                LocalityDTO localityDTO = new LocalityDTO();

                Hierarchy hierarchy = mongoNode.getHierarchy();

                countryDTO.setName(hierarchy.getCountry().getName());
                countryDTO.setType("country");
                countryDTO.setLngLat(hierarchy.getCountry().getCoordinate());

                regionDTO.setName(hierarchy.getRegion().getName());
                regionDTO.setType("region");
                regionDTO.setLngLat(hierarchy.getRegion().getCoordinate());

                placeDTO.setName(hierarchy.getPlace().getName());
                placeDTO.setType("place");
                placeDTO.setLngLat(hierarchy.getPlace().getCoordinate());
                
                localityDTO.setName(hierarchy.getLocality().getName());
                localityDTO.setType("locality");
                localityDTO.setLngLat(hierarchy.getLocality().getCoordinate());

                countries.add(countryDTO);
                regions.add(regionDTO);
                places.add(placeDTO);
                localities.add(localityDTO);

            }

            for (BonfireUnlock bonfireUnlock: user.getBonfireUnlocks()){
                
                Optional<MongoBonfire> oMongoBonfire = mongoBonfireService.findById(bonfireUnlock.getBonfireId());

                MongoBonfire mongoBonfire = oMongoBonfire.get();
                BonfireDTO bonfireDTO = new BonfireDTO();

                bonfireDTO.setName(mongoBonfire.getName());
                bonfireDTO.setType("bonfire");
                bonfireDTO.setLngLat(mongoBonfire.getCoordinates());

                bonfires.add(bonfireDTO);

            }

            bonfireMarkerResponse.setCountries(countries);
            bonfireMarkerResponse.setRegions(regions);
            bonfireMarkerResponse.setPlaces(places);
            bonfireMarkerResponse.setLocalities(localities);
            bonfireMarkerResponse.setBonfires(bonfires);



            return ResponseEntity.ok().body(bonfireMarkerResponse);
        }

        return ResponseEntity.badRequest().body("oopsie");
    }
    
}
