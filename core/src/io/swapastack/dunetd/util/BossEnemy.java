package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import java.util.LinkedList;

public class BossEnemy extends Enemy{
    public BossEnemy(int livePoints, int velocity, Vector2 coords, LinkedList<Vector2> path) {
        super(livePoints, velocity, coords, path);
        super.model = new Scene(GameScreen.sceneAssetHashMap.get("faceted_character/scene.gltf").scene);
        model.modelInstance.transform.scale(0.005f, 0.005f, 0.005f);
        super.money = 1500;
    }

}
