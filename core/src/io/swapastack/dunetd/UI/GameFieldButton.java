package io.swapastack.dunetd.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;

public class GameFieldButton extends VisImageButton
{
    private Color background;
    private Texture textureUP;
    private Texture textureDOWN;
    private int id;
    private boolean isOccupied;

    public GameFieldButton(Texture textureUP, Texture textureDOWN){
        super(new SpriteDrawable(new Sprite(textureUP)), new SpriteDrawable(new Sprite(textureDOWN)));
        this.textureUP = textureUP;
        this.textureDOWN = textureDOWN;
    }

    public GameFieldButton(Texture textureUP, Texture textureDOWN, int id, boolean isOccupied){
        super(new SpriteDrawable(new Sprite(textureUP)), new SpriteDrawable(new Sprite(textureDOWN)));
        this.textureUP = textureUP;
        this.textureDOWN = textureDOWN;
        this.id = id;
        this.isOccupied = isOccupied;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public boolean isOccupied(){
        return isOccupied;
    }

    public void setOccupied(boolean isOccupied){
        this.isOccupied = isOccupied;
    }

}
