package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector3;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

public class Infantry extends Enemy{
    //Scene model;
    public Infantry(int livePoints, int velocity) {
        super(livePoints, velocity);
        super.model = new Scene(GameScreen.sceneAssetHashMap.get("cute_cyborg/scene.gltf").scene);
        model.modelInstance.transform.scale(0.02f, 0.04f, 0.03f)
                .rotate(new Vector3(0.0f, 1.0f, 0.0f), 180.0f);
    }
}
