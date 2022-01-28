package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector3;
import net.mgsx.gltf.scene3d.scene.Scene;

public abstract class Enemy {
    int livePoints;
    int velocity;
    Scene model;

    public Enemy(int livePoints, int velocity){
        this.livePoints = livePoints;
        this.velocity = velocity;
    }

    public int getLivePoints() {
        return livePoints;
    }

    public void setLivePoints(int livePoints) {
        this.livePoints = livePoints;
    }

    public void changeLivePoints(int value){
        this.livePoints = this.livePoints + value;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public Scene getModel(){
        return this.model;
    }

    public void setModel(Scene model) {
        this.model = model;
    }

    public void setModelToPosition(Vector3 coords){
        this.model.modelInstance.transform.translate(coords);
    }





}
