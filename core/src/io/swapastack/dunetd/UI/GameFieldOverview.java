package io.swapastack.dunetd.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisImageButton.VisImageButtonStyle;
import com.kotcrab.vis.ui.widget.VisWindow;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.GameScreen;

import java.awt.*;

public class GameFieldOverview extends Actor {
    private final DuneTD parent;
    private Stage stage;
    private InputMultiplexer inputMultiplexer;
    private VisWindow window;

    public GameFieldOverview(DuneTD parent, Stage stage, InputMultiplexer inputMultiplexer){
        this.parent = parent;
        this.stage = stage;
        this.inputMultiplexer = inputMultiplexer;
        setWidgets();
    }

    private void setWidgets() {
        window = new VisWindow("Game field overview");
        VisImageButtonStyle style = new VisImageButtonStyle();
        for(int i = 0; i < GameScreen.gameField.length; i++){
            for(int j = 0; j < GameScreen.gameField[0].length; j++){
                GameFieldButton b = new GameFieldButton(Color.ORANGE);
                b.setSize(10f,10f);
                b.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent inputEvent, float x, float y){
                        System.out.println("Siis");
                        Texture texture = new Texture(Gdx.files.internal("sprites/unchecked.png"));
                        VisImageButtonStyle iBs = new VisImageButtonStyle(new TextureRegionDrawable(new TextureRegion(texture)),new TextureRegionDrawable(new TextureRegion(texture)),new TextureRegionDrawable(new TextureRegion(texture)),new TextureRegionDrawable(new TextureRegion(texture)),new TextureRegionDrawable(new TextureRegion(texture)),new TextureRegionDrawable(new TextureRegion(texture)));
                        b.setStyle(new VisImageButtonStyle(iBs));
                    }
                });
                window.add(b);
            }
            window.row();
        }
        inputMultiplexer.addProcessor(stage);
        stage.addActor(window);
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x,y);
        window.setPosition(x,y);
    }

    @Override
    public void setSize(float width, float height){
        super.setSize(width,height);
        window.setSize(width,height);
    }


}
