package io.swapastack.dunetd.UI;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.GameScreen;

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
        this.selectedTower = selectedTower + 1;
        setWidgets();
    }

    private void setWidgets() {
        byte dimX = (byte) GameScreen.gameField.length;
        byte dimY = (byte) GameScreen.gameField[0].length;
        window = new VisWindow("Game field overview");
        for(int i = 0; i < dimX; i++){ //Add image buttons
            for(int j = 0; j < dimY; j++){
                Texture checkedTexture = new Texture("sprites/checked.png");
                Texture uncheckedTexture = new Texture("sprites/unchecked.png");
                GameFieldButton b;

                if (GameScreen.gameField[i][j] > 0 && GameScreen.gameField[i][j] <= 4){ //Normal occupied: red circle
                    b = new GameFieldButton(uncheckedTexture,uncheckedTexture,new Vector2(i,j));
                    if(TowerPickerWidget.buildMode)
                        b.setDisabled(true);
                }
                else if (GameScreen.gameField[i][j] == 5){ //Occupied by Klopfer: hammer sprite
                    Texture tex = new Texture ("sprites/HammerCircle.png");
                    b = new GameFieldButton(tex,tex,new Vector2(i,j));
                    if(TowerPickerWidget.buildMode)
                        b.setDisabled(true);
                }
                else if (GameScreen.gameField[i][j] == 6){ //Occupied by start portal: Blue circle with 'S'
                    Texture tex = new Texture ("sprites/StartPortal.png");
                    b = new GameFieldButton(tex,tex,new Vector2(i,j));
                    if(TowerPickerWidget.buildMode)
                        b.setDisabled(true);
                }
                else if (GameScreen.gameField[i][j] == 7){ //Occupied by end portal: Blue circle with 'E'
                    Texture tex = new Texture ("sprites/EndPortal.png");
                    b = new GameFieldButton(tex,tex,new Vector2(i,j));
                    if(TowerPickerWidget.buildMode)
                        b.setDisabled(true);
                }
                else{ //Default: Not Occupied by anything: Green circle
                    b = new GameFieldButton(checkedTexture,checkedTexture,new Vector2(i,j));
                    if(!TowerPickerWidget.buildMode)
                        b.setDisabled(true);
                }
                b.setSize(20f,20f);
                b.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent inputEvent, float x, float y){
                        if(b.isChecked()){
                            byte cordX = (byte) b.getID().x;
                            byte cordY = (byte) b.getID().y;
                            window.remove();
                            TowerPickerWidget.waveReady = false;
                            TowerPickerWidget.b.setText("Initialize wave");
                            if(TowerPickerWidget.buildMode){
                                if(GameScreen.startPortalPlaced && selectedTower == 6 || GameScreen.endPortalPlaced && selectedTower == 7){
                                    VisDialog ii = new VisDialog("Portal placement");
                                    VisLabel il = new VisLabel("You are not allowed to place more than one\n" +
                                            "start portal and end portal.\n" +
                                            "If you want to place new portals on different positions\n" +
                                            "please destroy the old portals first.");
                                    il.setAlignment(Align.center);
                                    ii.text(il);
                                    ii.button("OK");
                                    ii.show(stage);
                                }
                                else
                                    GameScreen.addNewTower(new Vector2(cordX,cordY), selectedTower);
                            }
                            else
                                GameScreen.removeTower(new Vector2(cordX,cordY));
                            TowerPickerWidget.buildMode = true;
                            TowerPickerWidget.gfoWindowActive = false;
                        }
                    }
                });
                window.add(b);
            }
            window.row();
        }
        VisTextButton b = new VisTextButton("Cancel"); //Add cancel Button
        b.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                window.remove();
                TowerPickerWidget.gfoWindowActive = false;
                TowerPickerWidget.buildMode = true;
            }
        });
        window.add(b).right().pad(10).colspan(dimY).row();
        window.setSize(dimY*40f, dimX*40f+b.getHeight()+10f);
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
