package ai.lumidah.bonfire_v1_app_backend.model;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeTree {

    private String id;
    private String name; 
    private String type; 
    private double[] coordinates;
    private Map<String, NodeTree> child = new HashMap<>();
    private List<MongoBonfire> bonfires;

    @Override
    public String toString() {
        return "NodeTree [id=" + id + ", name=" + name + ", type=" + type + ", coordinates="
                + Arrays.toString(coordinates) + ", child=" + child + "]";
    }

    public NodeTree(String id, String name, String type, double[] coordinates){
        this.id = id;
        this.name = name;
        this.type = type;
        this.coordinates = coordinates;
    }

    public NodeTree addChild(String id, String name, String type, double[] coordinates){

        if (!child.containsKey(id)){
            child.put(id, new NodeTree(id, name, type, coordinates));
        }

        return child.get(id);

    }

    public void addBonfire(MongoBonfire bonfire) {
        if (bonfires == null) {
            bonfires = new ArrayList<>();
        }
        bonfires.add(bonfire);
    }

}
