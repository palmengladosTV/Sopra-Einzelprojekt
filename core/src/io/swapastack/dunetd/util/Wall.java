package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import static io.swapastack.dunetd.GameScreen.groundTileDimensions;

/** Wall (Tower): Tower that has no effect. Basically acts like a wall.**/
public class Wall extends Tower{

    public Wall(Vector2 coords) {
        super(coords);
        super.model = new Scene(GameScreen.sceneAssetHashMap.get("towerRound_roofB.glb").scene);
        model.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
        super.spice = 200;
    }

}
