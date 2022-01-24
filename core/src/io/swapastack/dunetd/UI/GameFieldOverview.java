package io.swapastack.dunetd.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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

import javax.swing.*;
import java.awt.*;

public class GameFieldOverview extends Actor {
    private final DuneTD parent;
    private Stage stage;
    private InputMultiplexer inputMultiplexer;
    private VisWindow window;
    private final int selectedTower;

    public GameFieldOverview(DuneTD parent, Stage stage, InputMultiplexer inputMultiplexer, int selectedTower){
        this.parent = parent;
        this.stage = stage;
        this.inputMultiplexer = inputMultiplexer;
        this.selectedTower = selectedTower;
        setWidgets();
    }

    private void setWidgets() {
        byte dimX = (byte) GameScreen.gameField.length;
        byte dimY = (byte) GameScreen.gameField[0].length;
        window = new VisWindow("Game field overview");
        for(int i = 0; i < dimX; i++){
            for(int j = 0; j < dimY; j++){
                Texture checkedTexture = new Texture("sprites/checked.png");
                Texture uncheckedTexture = new Texture("sprites/unchecked.png");
                GameFieldButton b;
                if(GameScreen.gameField[i][j] > 0 && GameScreen.gameField[i][j] < 4){
                    b = new GameFieldButton(uncheckedTexture,uncheckedTexture,i*dimX+j,true);
                    b.setDisabled(true);
                }
                else{
                    b = new GameFieldButton(checkedTexture,checkedTexture,i*dimX+j,false);
                }
                b.setSize(20f,20f);
                b.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent inputEvent, float x, float y){
                        if(b.isChecked()){
                            int cordX = Math.floorDiv(b.getID(),dimY);
                            int cordY = b.getID()%dimY;
                            GameScreen.gameField[cordX][cordY] = selectedTower + 1;
                            window.remove();
                            GameScreen.addNewTower(new Vector2(cordX,cordY), selectedTower+1);
                            TowerPickerWidget.gfoWindowActive = false;
                        }
                    }
                });
                window.add(b);
            }
            window.row();
        }
        window.setSize(GameScreen.gameField[0].length*40f, GameScreen.gameField.length*40f);
        window.setPosition(DuneTD.WIDTH / 2f - window.getWidth() / 2f, DuneTD.HEIGHT / 2f - window.getHeight() / 2f);
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
