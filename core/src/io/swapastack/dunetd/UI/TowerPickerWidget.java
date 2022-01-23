package io.swapastack.dunetd.UI;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.*;
import io.swapastack.dunetd.DuneTD;

public class TowerPickerWidget extends Actor {
    private final DuneTD parent;
    private VisWindow window;
    private Stage stage;
    private InputMultiplexer inputMultiplexer;
    private VisList<String> list;
    private VisTextButton b;

    public TowerPickerWidget(DuneTD parent, Stage stage, InputMultiplexer inputMultiplexer){
        this.parent = parent;
        this.stage = stage;
        this.inputMultiplexer = inputMultiplexer;
        setWidgets();
        setListeners();
        configureWidgets();
    }

    private void setWidgets() {
        window = new VisWindow("Choose a tower");
        list = new VisList<String>();
        list.setItems("item1", "item2", "item3", "item4");
        b = new VisTextButton("OK");

    }

    private void configureWidgets() {
        window.add(list).row();
        window.add(b).row();
        window.add(new LinkLabel("Link"));
        stage.addActor(window);
    }

    private void setListeners() {
        b.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent inputEvent, float x, float y){
                System.out.println("Saas");
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
