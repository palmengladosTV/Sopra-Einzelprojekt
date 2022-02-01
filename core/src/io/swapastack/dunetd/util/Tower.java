package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import net.mgsx.gltf.scene3d.scene.Scene;

public abstract class Tower {
    Vector2 coords;
    Scene model;
    int rotation;
    int spice;


    public Tower(Vector2 coords){
        this.coords = coords;
        this.rotation = 0;
    }

    public Scene getModel(){
        return this.model;
    }

    public void setModel(Scene model) {
        this.model = model;
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

    public int getMoney(){
        return this.spice;
    }

    public void hitEnemy(Enemy e){

    };

}
