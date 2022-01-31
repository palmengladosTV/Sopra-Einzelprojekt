package io.swapastack.dunetd.util;

import com.badlogic.gdx.math.Vector2;
import net.mgsx.gltf.scene3d.scene.Scene;

public abstract class Tower {
    Vector2 coords;
    Scene model;
    int rotation;

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


}
