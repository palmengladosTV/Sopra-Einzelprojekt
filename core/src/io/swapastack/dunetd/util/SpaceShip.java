package io.swapastack.dunetd.util;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import java.util.LinkedList;

/** SpaceShip Enemy: Slow enemy unit with more max. HP**/
public class SpaceShip extends Enemy{
    public SpaceShip(int livePoints, int velocity, Vector2 coords, LinkedList<Vector2> path) {
        super(livePoints, velocity, coords, path);
        super.model = new Scene(GameScreen.sceneAssetHashMap.get("spaceship_orion/scene.gltf").scene);
        model.modelInstance.transform.scale(0.2f, 0.2f, 0.2f);
        super.money = 300;
        super.ac = new AnimationController(model.modelInstance);
        super.ac.setAnimation("Action", -1);
    }

}
