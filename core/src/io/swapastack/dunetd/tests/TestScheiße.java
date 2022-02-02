package io.swapastack.dunetd.tests;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.util.Dijkstra;
import io.swapastack.dunetd.util.Graph;
import io.swapastack.dunetd.util.Node;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class TestScheiße {
    @Test
    void testDijkstra(){
        int [][] gameField = {{0,0,0,0,0},{0,0,1,1,0},{1,0,0,1,0},{1,1,0,1,0}};
        LinkedList<Node> fetz = Dijkstra.getPath(new Graph(gameField),new Node(new Vector2(1,3)), new Node(new Vector2(3,3)));
        assert(fetz.size() == 3);
    }
    //Der Rest geht alles!!!
}
