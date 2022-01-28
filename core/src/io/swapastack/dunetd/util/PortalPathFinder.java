package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.LinkedList;

public class PortalPathFinder {

    private static LinkedList<Vector2> path = new LinkedList<Vector2>();
    private static byte dimX, dimY;
    private static byte currentMin;

    public static LinkedList<Vector2> findShortestPath(int[][] gameField){
        path.clear();
        currentMin = Byte.MAX_VALUE;
        dimX = (byte) gameField.length;
        dimY = (byte) gameField[0].length;
        Vector2 startPos = new Vector2(0,0),
                endPos = new Vector2(0,0);
        for(int i = 0; i < dimX; i++){
            for(int j = 0; j < dimY;j++){
                if(gameField[i][j] == 6)
                    startPos = new Vector2(i,j);
                else if(gameField[i][j] == 7)
                    endPos = new Vector2(i,j);
                else if(gameField[i][j] != 0)
                    gameField[i][j] = 0;
                else
                    gameField[i][j]++;
            }
        }

        scimThroughPath(startPos,endPos, new LinkedList<Vector2>(), gameField.clone());
        path.add(endPos);

        path.forEach(ll -> System.out.print("(" + ll.x + "," + ll.y + "), "));
        System.out.println();
        return path;

    }

    @SuppressWarnings("unchecked")
    private static void scimThroughPath(Vector2 startPos, Vector2 endPos, LinkedList<Vector2> visited, int[][] adjacencyMatrix){
        if (startPos.x < 0 ||startPos.y < 0 || startPos.x >= dimX || startPos.y >= dimY ||
                adjacencyMatrix[(int) startPos.x][(int) startPos.y] == 0)
            return;
        else if(startPos.equals(endPos)){
            if(currentMin > visited.size()){
                currentMin = (byte) visited.size();
                path = (LinkedList<Vector2>) visited.clone();
            }
            return;
        }
        visited.add(startPos);
        adjacencyMatrix[(int) startPos.x][(int) startPos.y] = 0;
        int[][] newAdjacencyMatrix = Arrays.stream(adjacencyMatrix).map(int[]::clone).toArray(int[][]::new);
        scimThroughPath(new Vector2(startPos.x - 1, startPos.y),endPos, (LinkedList<Vector2>) visited.clone(), newAdjacencyMatrix);
        scimThroughPath(new Vector2(startPos.x + 1, startPos.y),endPos, (LinkedList<Vector2>) visited.clone(), newAdjacencyMatrix);
        scimThroughPath(new Vector2(startPos.x, startPos.y + 1),endPos, (LinkedList<Vector2>) visited.clone(), newAdjacencyMatrix);
        scimThroughPath(new Vector2(startPos.x, startPos.y - 1),endPos, (LinkedList<Vector2>) visited.clone(), newAdjacencyMatrix);

    }
}
