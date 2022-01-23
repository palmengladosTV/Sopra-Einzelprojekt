package io.swapastack.dunetd.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;

public class GameFieldButton extends VisImageButton
{
    private Color background;
    private Texture textureUP;
    private Texture textureDOWN;
    private int id;

    public GameFieldButton(Color background){
        super(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/checked.png"))),new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/checked.png"))));
        //super(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("sprites/checked.png"))), new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("sprites/checked.png"))));
        this.background = background;
        this.textureUP = new Texture(Gdx.files.internal("sprites/checked.png"));
        this.textureDOWN = new Texture(Gdx.files.internal("sprites/checked.png"));
        Sprite bg = new Sprite();
        bg.setColor(background);
        this.setBackground(new SpriteDrawable(new Sprite()));
    }

    public GameFieldButton(Color background, int id){
        super(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/checked.png"))),new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/checked.png"))));
        //super(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("sprites/checked.png"))), new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("sprites/checked.png"))));
        this.background = background;
        this.textureUP = new Texture(Gdx.files.internal("sprites/checked.png"));
        this.textureDOWN = new Texture(Gdx.files.internal("sprites/checked.png"));
        this.id = id;
        Sprite bg = new Sprite();
        bg.setColor(background);
        this.setBackground(new SpriteDrawable(new Sprite()));
    }

    public GameFieldButton(Texture textureUP, Texture textureDOWN, Color background){
        super(new SpriteDrawable(new Sprite(textureUP)), new SpriteDrawable(new Sprite(textureDOWN)));
        this.background = background;
        this.textureUP = textureUP;
        this.textureDOWN = textureDOWN;
        Sprite bg = new Sprite();
        bg.setColor(background);
        this.setBackground(new SpriteDrawable(new Sprite()));
    }

    public GameFieldButton(Texture textureUP, Texture textureDOWN, Color background, int id){
        super(new SpriteDrawable(new Sprite(textureUP)), new SpriteDrawable(new Sprite(textureDOWN)));
        this.background = background;
        this.textureUP = textureUP;
        this.textureDOWN = textureDOWN;
        this.id = id;
        Sprite bg = new Sprite();
        bg.setColor(background);
        this.setBackground(new SpriteDrawable(new Sprite()));
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

}
