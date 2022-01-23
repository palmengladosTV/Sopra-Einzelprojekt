package io.swapastack.dunetd.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import io.swapastack.dunetd.DuneTD;
import net.mgsx.gltf.scene3d.scene.Scene;

public class TowerPickerWidget extends Actor {
    private final DuneTD parent;
    //public InputMultiplexer iMpx;
    public VisWindow window;
    public Stage stage;
    private VisList<String> list;
    private VisTextButton b;

    public TowerPickerWidget(DuneTD parent, Stage stage){
        this.parent = parent;
        this.stage = stage;
        //iMpx = new InputMultiplexer();
        setWidgets();
        configureWidgets();
        //Gdx.input.setInputProcessor(window.getStage());
        setListeners();

        //iMpx.addProcessor(window.getStage());
        //Gdx.input.setInputProcessor(this.stage);



    }

    private void setWidgets() {
        window = new VisWindow("Choose a tower");
        list = new VisList<String>();
        list.setItems("item1", "item2", "item3", "item4");
        b = new VisTextButton("OK");

        VisDialog ii = new VisDialog("Invalid Input");
        VisLabel il = new VisLabel("Your input must be a\nnumber and between 2 and 10!");
        il.setAlignment(Align.center);
        ii.text(il);
        ii.button("OK");
        ii.show(stage);

    }

    private void configureWidgets() {
        window.add(list).row();
        window.add(b).row();
        window.add(new LinkLabel("Link"));
        window.addListener(new InputListener(){
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor formerActor){
                System.out.println("SA");
            }
        });
        stage.addActor(window);
    }

    private void setListeners() {
        System.out.println("Scheisaxf");
        System.out.println();
        super.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor formerActor){
                System.out.println("ss");
            }
        });
        b.addListener(new InputListener(){
           @Override
           public void touchUp(InputEvent inputEvent, float x , float y, int pointer, int button){
               System.out.println("soos");
           }
        });

        Gdx.input.setInputProcessor(stage);
        stage.draw();
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
        //window.setSize(width * 2, height * 2);
        window.setSize(width,height);
    }

}
