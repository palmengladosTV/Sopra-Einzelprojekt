package io.swapastack.dunetd.UI;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import io.swapastack.dunetd.DuneTD;

public class GameUI {
    private final DuneTD parent;
    public Stage stage;
    public TowerPickerWidget tpw;
    public InputMultiplexer inputMultiplexerUI;

    public GameUI(DuneTD parent){
        this.parent = parent;
        stage = new Stage(new FitViewport(DuneTD.WIDTH,DuneTD.HEIGHT));
        inputMultiplexerUI = new InputMultiplexer();
        VisUI.load();
        setWidgets();
        configureWidgets();
    }

    private void setWidgets() {
        tpw = new TowerPickerWidget(parent, stage, inputMultiplexerUI);
    }

    private void configureWidgets() {
        tpw.setSize(100,200);
        tpw.setPosition(25, 60);
    }
    public void update(float delta){
        stage.act(delta);

    }
    public void render(){
        stage.draw();
    }
    public void resize(int width, int height) {
        stage.getViewport().update(width,height);
    }
    public void dispose(){
        stage.dispose();
    }


}