package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import io.swapastack.dunetd.GameScreen;
import net.mgsx.gltf.scene3d.scene.Scene;

/** Abstract class for all the towers.**/
public abstract class Tower {
    Vector2 coords;
    Scene model;
    int rotation;
    int spice;

    /** Constructor:
     * @param coords The coordinates of the tower.**/
    public Tower(Vector2 coords){
        this.coords = coords;
        this.rotation = 0;
    }

    /** Returns the model of the tower.
     * @return The {@link Scene} of the enemy required for the {@link net.mgsx.gltf.scene3d.scene.SceneManager} in {@link GameScreen}
     * @see GameScreen**/
    public Scene getModel(){
        return this.model;
    }

    /** Sets the model of the tower.
     * @param model The scene of the enemy provided by the {@link net.mgsx.gltf.scene3d.scene.SceneManager} in {@link GameScreen}.
     * @see GameScreen**/
    public void setModel(Scene model) {
        this.model = model;
    }

    /** Rotates the tower model along the Y axis for a specific amount of degrees.
     * @param degrees The degrees to rotate along the Y axis.**/
    public void rotateModel(float degrees){
        if(this.rotation == degrees)
            return;
        this.model.modelInstance.transform.rotate(0f,1f,0f,degrees-this.rotation);
        this.rotation += (degrees-this.rotation) % 360;
    }

    /** Gets the current coordinates of the tower.
     * @return a {@link Vector2} with the coordinates.**/
    public Vector2 getCoords() {
        return coords;
    }

    /** Gets amount of money this tower needs when placed.**/
    public int getMoney(){
        return this.spice;
    }

    /** Finds (all) enemies in range of the tower.**/
    public Enemy findEnemyInRange(Enemy e){

        if(this.getCoords().x == Math.round(e.getCoords().x - 1) && this.getCoords().y == Math.round(e.getCoords().y - 1)){ //NW
            this.rotateModel(225);
        }
        else if(this.getCoords().x == Math.round(e.getCoords().x - 1) && this.getCoords().y == Math.round(e.getCoords().y)){ //W
            this.rotateModel(270);
        }
        else if(this.getCoords().x == Math.round(e.getCoords().x - 1) && this.getCoords().y == Math.round(e.getCoords().y + 1)){ //SW
            this.rotateModel(315);
        }
        else if(this.getCoords().x == Math.round(e.getCoords().x) && this.getCoords().y == Math.round(e.getCoords().y + 1)){ //S
            this.rotateModel(0);
        }
        else if(this.getCoords().x == Math.round(e.getCoords().x + 1) && this.getCoords().y == Math.round(e.getCoords().y + 1)){ //SO
            this.rotateModel(45);
        }
        else if(this.getCoords().x == Math.round(e.getCoords().x + 1) && this.getCoords().y == Math.round(e.getCoords().y)){ //O
            this.rotateModel(90);
        }
        else if(this.getCoords().x == Math.round(e.getCoords().x + 1) && this.getCoords().y == Math.round(e.getCoords().y - 1)){ //NO
            this.rotateModel(135);
        }
        else if(this.getCoords().x == Math.round(e.getCoords().x) && this.getCoords().y == Math.round(e.getCoords().y - 1)){ //N
            this.rotateModel(180);
        }
        else
            return null;
        return e;
    }

    /** Deals damage or slows down all enemies previously found in range of the tower**/
    public void hitEnemy(Enemy e){

    };

}
