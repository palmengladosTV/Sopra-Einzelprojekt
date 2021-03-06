package io.swapastack.dunetd.UI;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.*;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.GameScreen;

/** Shows the Tower selection UI.**/
public class TowerPickerWidget extends Actor {
    private final DuneTD parent;
    private final Stage stage;
    private final InputMultiplexer inputMultiplexer;
    private static VisWindow window;
    private VisList<String> list;
    private static VisTextButton btnDestructionMode;
    public static VisTextButton b;
    protected static boolean gfoWindowActive = false;
    protected static boolean buildMode = true;
    public static boolean waveReady = false;

    /** @param inputMultiplexer The widget needs to accept inputs.
     *  @param stage The stage on where to show the UI.**/
    public TowerPickerWidget(DuneTD parent, Stage stage, InputMultiplexer inputMultiplexer){
        this.parent = parent;
        this.stage = stage;
        this.inputMultiplexer = inputMultiplexer;
        setWidgets();
        setListeners();
        configureWidgets();
    }

    /** Initial settings for widgets.**/
     private void setWidgets() {
            window = new VisWindow("Place Towers");
            list = new VisList<String>();
            list.setItems("Sonic Tower (3000)", "Canon Tower (250)", "Bomb Tower (1000)", "Wall (200)", "Klopfer (2500)", "Start-Portal (0)", "End-Portal (0)");
            list.setSelectedIndex(-1);
            b = new VisTextButton("Initialize wave");
            btnDestructionMode = new VisTextButton("Destroy");
    }

    /** Configuration for appearance for widgets.**/
    private void configureWidgets() {
        window.add(list).row();
        window.add(new Separator()).pad(5).fillX().expandX().row();
        window.add(btnDestructionMode).row();
        window.add(new Separator()).pad(5).fillX().expandX().row();
        window.add(b);
        stage.addActor(window);
    }

    /** Set listeners for buttons and other elements in the widget.**/
    private void setListeners() {
        b.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent inputEvent, float x, float y){
                if(waveReady){
                    //waveReady = false;
                    //b.setText("Initialize wave");
                    window.getTitleLabel().setText("Only Klopfers available.");
                    //window.setTouchable(Touchable.disabled);
                    btnDestructionMode.setDisabled(true);
                    GameScreen.createEnemies();
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
                if(btnDestructionMode.isDisabled())
                    return;
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
                    if(GameScreen.allowEnemySpawn == (list.getSelectedIndex() != 4)){  //5 - 1 = 4
                        list.setSelectedIndex(-1);
                        return;
                    }
                    gfoWindowActive = true;
                    GameFieldOverview gfo = new GameFieldOverview(parent,stage,inputMultiplexer,list.getSelectedIndex());
                }
                list.setSelectedIndex(-1);
            }
        });

        inputMultiplexer.addProcessor(stage);
    }

    /** Reactivates the ability to build towers again after one wave has finished**/
    public static void allowBuild(){
        window.getTitleLabel().setText("Place towers:");
        //window.setTouchable(Touchable.enabled);
        btnDestructionMode.setDisabled(false);
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
