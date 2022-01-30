package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;

public class Node {
    private Vector2 coords;
    private LinkedList<Node> shortestPath = new LinkedList<Node>();
    private int distance = Integer.MAX_VALUE;
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

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return getCoords().equals(node.getCoords()) && Objects.equals(adjacentNodes, node.adjacentNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCoords(), adjacentNodes);
    }*/
}
