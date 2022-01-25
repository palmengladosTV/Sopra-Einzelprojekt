package io.swapastack.dunetd.UI;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;
import io.swapastack.dunetd.DuneTD;

public class TowerPickerWidget extends Actor {
    private final DuneTD parent;
    private Stage stage;
    private InputMultiplexer inputMultiplexer;
    private VisWindow window;
    private VisList<String> list;
    private VisTextButton b, btnDestructionMode;
    protected static boolean gfoWindowActive = false;
    protected static boolean buildMode = true;

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
        list.setItems("Sonic Tower", "Canon Tower", "Bomb Tower", "Klopfer", "Start-Portal", "End-Portal");
        list.setSelectedIndex(-1);
        b = new VisTextButton("Spawn Enemies");
        btnDestructionMode = new VisTextButton("Destroy");
    }

    private void configureWidgets() {
        window.add(list).row();
        window.add(new Separator()).pad(5).fillX().expandX().row();
        window.add(btnDestructionMode).row();
        window.add(new Separator()).pad(5).fillX().expandX().row();
        window.add(b).colspan(2);
        stage.addActor(window);
    }

    private void setListeners() {
        b.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent inputEvent, float x, float y){
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
