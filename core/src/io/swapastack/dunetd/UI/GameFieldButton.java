package io.swapastack.dunetd.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;

/** These are the small image buttons you see in the {@link GameFieldOverview}.
   Additionally, they have a specific id.**/
public class GameFieldButton extends VisImageButton
{
    private final Vector2 id;

    /**@param textureUP The texture when the button is unpressed
     * @param textureDOWN The texture when the button is pressed
     * @param id The coordinates of the button in the virtual game environment**/
    public GameFieldButton(Texture textureUP, Texture textureDOWN, Vector2 id){
        super(new SpriteDrawable(new Sprite(textureUP)), new SpriteDrawable(new Sprite(textureDOWN)));
        this.id = id;
    }

    public Vector2 getID(){
        return this.id;
    }
}
