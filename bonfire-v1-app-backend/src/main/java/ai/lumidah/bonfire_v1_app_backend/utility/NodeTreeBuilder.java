    package ai.lumidah.bonfire_v1_app_backend.utility;

    import java.util.List;

import ai.lumidah.bonfire_v1_app_backend.model.Hierarchy;
import ai.lumidah.bonfire_v1_app_backend.model.MongoBonfire;
import ai.lumidah.bonfire_v1_app_backend.model.MongoNode;
import ai.lumidah.bonfire_v1_app_backend.model.NodeTree;

    public class NodeTreeBuilder {

        private NodeTree root = new NodeTree("root", "Earth", "planet", null);

        public NodeTree build(List<MongoNode> mongoNodes){

            for (MongoNode mongoNode: mongoNodes){
                nodeBuilder(mongoNode);
            }
            return root;

        }

        public void nodeBuilder(MongoNode mongoNode){

            Hierarchy hierarchy = mongoNode.getHierarchy();

            NodeTree countryTree = root.addChild(
                hierarchy.getCountry().getName(),
                hierarchy.getCountry().getName(),
                "country",
                null
            );

            NodeTree regionTree = countryTree.addChild(
                hierarchy.getRegion().getName(),
                hierarchy.getRegion().getName(),
                "region",
                hierarchy.getRegion().getCoordinate()
            );

            NodeTree placeTree = regionTree.addChild(
                hierarchy.getPlace().getName(),
                hierarchy.getPlace().getName(),
                "place",
                hierarchy.getPlace().getCoordinate()
            );

            placeTree.addChild(
                mongoNode.getId(),
                hierarchy.getLocality().getName(),
                "locality",
                hierarchy.getLocality().getCoordinate()
            );
        }

        public NodeTree attachBonfires(NodeTree nodeTree, List<MongoBonfire> mongoBonfires) {
            for (MongoBonfire bonfire : mongoBonfires) {
                NodeTree targetNode = findNodeById(nodeTree, bonfire.getNodeId());
                if (targetNode != null) {
                    targetNode.addBonfire(bonfire);
                }
            }
            return nodeTree;
        }
    
        private NodeTree findNodeById(NodeTree node, String nodeId) {
            if (node == null) {
                return null;
            }
    
            if (node.getId().equals(nodeId)) {
                return node;
            }
    
            for (NodeTree child : node.getChild().values()) {
                NodeTree found = findNodeById(child, nodeId);
                if (found != null) {
                    return found;
                }
            }
    
            return null; 
        }

}



