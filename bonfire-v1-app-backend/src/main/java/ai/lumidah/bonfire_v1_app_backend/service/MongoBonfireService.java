package ai.lumidah.bonfire_v1_app_backend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ai.lumidah.bonfire_v1_app_backend.model.MongoBonfire;
import ai.lumidah.bonfire_v1_app_backend.repository.MongoBonfireRepository;

@Repository
public class MongoBonfireService {

    @Autowired
    private MongoBonfireRepository mongoBonfireRepository;

    public Optional<MongoBonfire> findById(String id){
        return mongoBonfireRepository.findById(id);
    }

    public MongoBonfire save(MongoBonfire mongoBonfire){
        return mongoBonfireRepository.save(mongoBonfire);
    }






}
