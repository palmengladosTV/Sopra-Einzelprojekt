package io.swapastack.dunetd.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.GameScreen;
import io.swapastack.dunetd.util.PortalPathFinder;

import java.util.LinkedList;
import java.util.Vector;

public class TowerPickerWidget extends Actor {
    private final DuneTD parent;
    private Stage stage;
    private InputMultiplexer inputMultiplexer;
    private VisWindow window;
    private VisList<String> list;
    private VisTextButton btnDestructionMode;
    public static VisTextButton b;
    protected static boolean gfoWindowActive = false;
    protected static boolean buildMode = true;
    public static boolean waveReady = false;

    public TowerPickerWidget(DuneTD parent, Stage stage, InputMultiplexer inputMultiplexer){
        this.parent = parent;
        this.stage = stage;
        this.inputMultiplexer = inputMultiplexer;
        setWidgets();
        setListeners();
        configureWidgets();
    }

    private void setWidgets() {
        window = new VisWindow("Place Towers");
        list = new VisList<String>();
        list.setItems("Sonic Tower", "Canon Tower", "Bomb Tower", "Wall", "Klopfer", "Start-Portal", "End-Portal");
        list.setSelectedIndex(-1);
        b = new VisTextButton("Initialize wave");
        btnDestructionMode = new VisTextButton("Destroy");
    }

    private void configureWidgets() {
        window.add(list).row();
        window.add(new Separator()).pad(5).fillX().expandX().row();
        window.add(btnDestructionMode).row();
        window.add(new Separator()).pad(5).fillX().expandX().row();
        window.add(b);
        stage.addActor(window);
    }

    private void setListeners() {
        b.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent inputEvent, float x, float y){
                if(waveReady){
                    waveReady = false;
                    b.setText("Initialize wave");
                    window.getTitleLabel().setText("Window disabled!");
                    window.setTouchable(Touchable.disabled);
                    GameScreen.allowEnemySpawn = true;
                }
                else{
                    if(GameScreen.startPortalPlaced && GameScreen.endPortalPlaced){
                        b.setText("Start wave");
                        waveReady = true;
                        GameScreen.createPath();
                    }
                    else{
                        VisDialog ii = new VisDialog("Portal placement");
                        VisLabel il = new VisLabel("You have to place a start portal\nand an end portal first!");
                        il.setAlignment(Align.center);
                        ii.text(il);
                        ii.button("OK");
                        ii.show(stage);
                    }
                }

            }
        });
        btnDestructionMode.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent inputEvent, float x, float y){
                TowerPickerWidget.buildMode = false;
                if(!gfoWindowActive){
                    gfoWindowActive = true;
                    GameFieldOverview gfo = new GameFieldOverview(parent,stage,inputMultiplexer,list.getSelectedIndex());
                }
            }
        });

        list.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent inputEvent, float x, float y){
                if(!gfoWindowActive){
                    gfoWindowActive = true;
                    GameFieldOverview gfo = new GameFieldOverview(parent,stage,inputMultiplexer,list.getSelectedIndex());
                }
                list.setSelectedIndex(-1);
            }
        });

        inputMultiplexer.addProcessor(stage);
    }

    @Override
    public void setPosition(float x, float y){
        super.setPosition(x,y);
        //window.setPosition(DuneTD.WIDTH /2-window.getWidth()/2, DuneTD.HEIGHT /2-window.getHeight()/2);
        window.setPosition(x,y);
    }

    @Override
    public void setSize(float width, float height){
        super.setSize(width,height);
        window.setSize(width,height);
    }


}
