package io.swapastack.dunetd.UI;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import io.swapastack.dunetd.DuneTD;

public class GameUI {
    private final DuneTD parent;
    public static Stage stage;
    public TowerPickerWidget tpw;
    public WaveOverviewWidget wow;
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
        wow = new WaveOverviewWidget(parent,stage);
    }

    private void configureWidgets() {
        tpw.setSize(150,280);
        tpw.setPosition(40, 180);

        wow.setSize(200,120);
        wow.setPosition(1150,650);
    }

    public static void showDialog(VisDialog d){
        d.show(stage);
    }

    public static void showNotEnoughMoneyDialog(){
        VisDialog ii = new VisDialog("Not enough money");
        VisLabel il = new VisLabel("You don't have enough spice to do that!");
        il.setAlignment(Align.center);
        ii.text(il);
        ii.button("OK");
        showDialog(ii);
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
