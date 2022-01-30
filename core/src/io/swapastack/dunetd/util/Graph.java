package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;

public class Graph {

    private HashSet<Node> nodes = new HashSet<Node>();

    public Graph(int[][] gameField){
        byte dimX = (byte) gameField.length;
        byte dimY = (byte) gameField[0].length;

        Node[][] n = new Node[dimX][dimY];
        for(int i = 0; i < dimX; i++){
            for (int j = 0; j < dimY; j++) {
                if (gameField[i][j] == 0 || gameField[i][j] == 6 || gameField[i][j] == 7){
                     n[i][j] = new Node(new Vector2(i, j));
                }
            }
        }
        for(int i = 0; i < dimX; i++){
            for (int j = 0; j < dimY; j++) {
                if (n[i][j] != null){
                    if(i > 0 && n[i-1][j] != null){
                        n[i][j].addNeighbors(n[i-1][j]);
                        n[i-1][j].addNeighbors(n[i][j]);
                    }
                    if(i < dimX-1 && n[i+1][j] != null){
                        n[i][j].addNeighbors(n[i+1][j]);
                        n[i+1][j].addNeighbors(n[i][j]);
                    }
                    if(j > 0 && n[i][j-1] != null){
                        n[i][j].addNeighbors(n[i][j-1]);
                        n[i][j-1].addNeighbors(n[i][j]);
                    }
                    if(j > dimY-1 && n[i][j+1] != null){
                        n[i][j].addNeighbors(n[i][j+1]);
                        n[i][j+1].addNeighbors(n[i][j]);
                    }
                    nodes.add(n[i][j]);
                }
            }
        }
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public HashSet<Node> getNodes(){
        return this.nodes;
    }

    public Node getNode(Vector2 coords){
        for(Node n : nodes){
            if (n.getCoords().equals(coords))
                    return n;
        }
        return null;
    }


}
