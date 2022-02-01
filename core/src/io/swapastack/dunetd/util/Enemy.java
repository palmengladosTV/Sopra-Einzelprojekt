package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import java.util.LinkedList;

public abstract class Enemy{
    int livePoints;
    float velocity;
    public final float maxSpeed;
    public LinkedList<Vector2> destination;
    Vector2 coords;
    Scene model;
    int rotation;
    int money;

    public Enemy(int livePoints, float velocity, Vector2 coords, LinkedList<Vector2> path){
        this.livePoints = livePoints;
        this.velocity = velocity;
        this.maxSpeed = velocity;
        this.coords = coords;
        this.destination = path;
        this.rotation = 0;
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

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public Scene getModel(){
        return this.model;
    }

    public void setModel(Scene model) {
        this.model = model;
    }

    public void setModelToPosition(){
        this.model.modelInstance.transform.trn(this.coords.x,GameScreen.groundTileDimensions.y,this.coords.y);
    }

    public void moveModel(float dx, float dz){
        this.model.modelInstance.transform.trn(dx, 0f, dz);
    }

    public void rotateModel(float degrees){
        if(this.rotation == degrees)
            return;
        this.model.modelInstance.transform.rotate(0f,1f,0f,degrees-this.rotation);
        this.rotation += (degrees-this.rotation) % 360;
    }

    public Vector2 getCoords() {
        return coords;
    }

    public void setCoords(Vector2 coords) {
        this.coords = coords;
    }

    public void setCoords(float x, float y) {
        this.coords = new Vector2(x,y);
    }

    public int getMoney(){
        return this.money;
    }

}
