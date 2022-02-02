package io.swapastack.dunetd.util;

import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

import java.util.LinkedList;

/** Abstract class for all the enemies.**/
public abstract class Enemy{
    int livePoints;
    float velocity;
    public final float maxSpeed;
    public LinkedList<Vector2> destination;
    Vector2 coords;
    Scene model;
    int rotation;
    int money;
    AnimationController ac;

    /** Constructor:
     * @param livePoints The total live points for the enemy.
     * @param velocity The speed for the enemy.
     * @param coords The current position of the enemy (X- and Z-coordinate).
     * @param path The path calculated by {@link Dijkstra Dijkstras Shortest Path Algorithm}.**/
    public Enemy(int livePoints, float velocity, Vector2 coords, LinkedList<Vector2> path){
        this.livePoints = livePoints;
        this.velocity = velocity;
        this.maxSpeed = velocity;
        this.coords = coords;
        this.destination = path;
        this.rotation = 0;
    }

    /** Returns the live points of the enemy.**/
    public int getLivePoints() {
        return livePoints;
    }

    /** Changes the live points of the enemy.
     * @param value The change value of the lives.**/
    public void changeLivePoints(int value){
        this.livePoints = this.livePoints + value;
    }

    /** Gets the velocity of the enemy.**/
    public float getVelocity() {
        return velocity;
    }

    /** Sets the velocity of the enemy.**/
    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    /** Returns the model of the enemy.
     * @return The {@link Scene} of the enemy required for the {@link net.mgsx.gltf.scene3d.scene.SceneManager} in {@link GameScreen}
     * @see GameScreen**/
    public Scene getModel(){
        return this.model;
    }

    /** Sets the model of the enemy.
     * @param model The scene of the enemy provided by the {@link net.mgsx.gltf.scene3d.scene.SceneManager} in {@link GameScreen}.
     * @see GameScreen**/
    public void setModel(Scene model) {
        this.model = model;
    }

    /** Sets the position of the {@link Scene} to the internally calculated coordinates (provided by the constructor).**/
    public void setModelToPosition(){
        this.model.modelInstance.transform.trn(this.coords.x,GameScreen.groundTileDimensions.y,this.coords.y);
    }

    /** Moves the enemy by the provided X and Z values.
     * @param dx Delta x.
     * @param dz Delta z.**/
    public void moveModel(float dx, float dz){
        this.model.modelInstance.transform.trn(dx, 0f, dz);
    }

    /** Rotates the enemy model along the Y axis for a specific amount of degrees.
     * @param degrees The degrees to rotate along the Y axis.**/
    public void rotateModel(float degrees){
        if(this.rotation == degrees)
            return;
        this.model.modelInstance.transform.rotate(0f,1f,0f,degrees-this.rotation);
        this.rotation += (degrees-this.rotation) % 360;
    }

    /** Gets the current coordinates of the enemy.
     * @return a {@link Vector2} with the coordinates.**/
    public Vector2 getCoords() {
        return coords;
    }

    /** Sets the coordinates of the enemy.
     * @param coords A {@link Vector2} for the X- and Z-coordinate.**/
    public void setCoords(Vector2 coords) {
        this.coords = coords;
    }

    /** Sets the current coordinates of the enemy.
     * @param x The X-coordinate for the enemy.
     * @param y The Z-coordinate for the enemy.**/
    public void setCoords(float x, float y) {
        this.coords = new Vector2(x,y);
    }

    /** Gets amount of money this enemy rewards when killed.**/
    public int getMoney(){
        return this.money;
    }

    /** Returns an {@link AnimationController} needed by the {@link net.mgsx.gltf.scene3d.scene.SceneManager} in {@link GameScreen}
     * to play animations for the enemies.
     * @see GameScreen**/
    public AnimationController getAnimationController(){
        return this.ac;
    }

}
