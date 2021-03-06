package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import static io.swapastack.dunetd.GameScreen.groundTileDimensions;

/** Bomb Tower: Medium costly tower that deals area damage to enemies.**/
public class BombTower extends Tower{
    int power;
    byte cooldown;
    final byte COOLDOWNTIMER = 10;
    public BombTower(Vector2 coords, int damage) {
        super(coords);
        super.model = new Scene(GameScreen.sceneAssetHashMap.get("weapon_blaster.glb").scene);
        model.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
        this.power = damage;
        super.spice = 1000;
    }

    @Override
    public Enemy findEnemyInRange(Enemy e) {
        return super.findEnemyInRange(e);
    }

    @Override
    public void hitEnemy(Enemy e) {
        cooldown++;
        if(cooldown%COOLDOWNTIMER != 0)
            return;
        cooldown = 0;
        e.changeLivePoints(-this.power);
    }
}
