package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
public class Node {
    private final Vector2 coords;
    private final HashSet<Node> adjacentNodes = new HashSet<>();

    /** Node class needed for the {@link Dijkstra Dijkstra Shortest Path Alhorithm}.
     * @param coords Coordinates of the node.
     * @see Graph
     * @see Dijkstra**/
    public Node(Vector2 coords){
        this.coords = coords;
    }

    public void addNeighbors(Node node){
       adjacentNodes.add(node);
    }

    public Vector2 getCoords(){
        return this.coords;
    }

    public HashSet<Node> getNeighbors(){
        return this.adjacentNodes;
    }
}
