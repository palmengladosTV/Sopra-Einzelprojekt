package io.swapastack.dunetd.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;

public class GameFieldButton extends VisImageButton
{
    private Color background;
    private Texture textureUP;
    private Texture textureDOWN;
    //private int id;
    private Vector2 id;

    public GameFieldButton(Texture textureUP, Texture textureDOWN){
        super(new SpriteDrawable(new Sprite(textureUP)), new SpriteDrawable(new Sprite(textureDOWN)));
        this.textureUP = textureUP;
        this.textureDOWN = textureDOWN;
    }

    public GameFieldButton(Texture textureUP, Texture textureDOWN, Vector2 id){
        super(new SpriteDrawable(new Sprite(textureUP)), new SpriteDrawable(new Sprite(textureDOWN)));
        this.textureUP = textureUP;
        this.textureDOWN = textureDOWN;
        this.id = id;
    }

    public Vector2 getID(){
        return this.id;
    }
}
