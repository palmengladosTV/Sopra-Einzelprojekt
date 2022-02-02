package io.swapastack.dunetd.util;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import java.util.LinkedList;

public class Infantry extends Enemy{
    public Infantry(int livePoints, int velocity, Vector2 coords, LinkedList<Vector2> path) {
        super(livePoints, velocity, coords, path);
        super.model = new Scene(GameScreen.sceneAssetHashMap.get("cute_cyborg/scene.gltf").scene);
        model.modelInstance.transform.scale(0.02f, 0.04f, 0.03f);
        super.money = 100;
        super.ac = new AnimationController(model.modelInstance);
        super.ac.setAnimation("RUN", -1);
    }

}
