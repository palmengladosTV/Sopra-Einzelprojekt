package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import static io.swapastack.dunetd.GameScreen.groundTileDimensions;

/** Sonic Tower: Very costly tower that slows enemies in certain range down.**/

public class SonicTower extends Tower{
    int power;
    public SonicTower(Vector2 coords, int power) {
        super(coords);
        super.model = new Scene(GameScreen.sceneAssetHashMap.get("towerRound_crystals.glb").scene);
        model.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
        this.power = power;
        super.spice = 3000;
    }

    @Override
    public Enemy findEnemyInRange(Enemy e) {
        return super.findEnemyInRange(e);
    }

    @Override
    public void rotateModel(float degrees) {
        this.getModel().modelInstance.transform.rotate(0f,1f,0f,10);
    }

    @Override
    public void hitEnemy(Enemy e) {
        e.setVelocity(Math.round(e.maxSpeed*(1-power/10f)));
    }
}
