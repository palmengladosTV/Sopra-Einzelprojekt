package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.LinkedList;

public class Node {
    private final Vector2 coords;
    private HashSet<Node> adjacentNodes = new HashSet<>();

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
