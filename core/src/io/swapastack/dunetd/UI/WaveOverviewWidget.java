package io.swapastack.dunetd.UI;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisWindow;
import io.swapastack.dunetd.DuneTD;
import io.swapastack.dunetd.GameScreen;

public class WaveOverviewWidget extends Actor {

    private final DuneTD parent;
    private final Stage stage;
    private VisWindow window;
    private static VisLabel waveNo, currentEnemyCount, spice, totalLives;
    private static int currentWaveMaxEnemies, currentWaveEnemies;

    /** Sets up a UI window with basic information about the current game stats.**/
    public WaveOverviewWidget(DuneTD parent, Stage stage){
        this.parent = parent;
        this.stage = stage;
        setWidgets();
    }

    /** Initial settings for widgets. **/
    private void setWidgets() {
        waveNo = new VisLabel("0");
        currentEnemyCount = new VisLabel("0/0");
        spice = new VisLabel(Integer.toString(GameScreen.money));
        totalLives = new VisLabel(Integer.toString(GameScreen.lives));

        window = new VisWindow("Wave Stats");
        window.add(new VisLabel("Total lives:")).left();
        window.add(totalLives).right().padLeft(15).row();
        window.add(new VisLabel("Wave Number:")).left();
        window.add(waveNo).right().padLeft(15).row();
        window.add(new VisLabel("Enemies killed:")).left();
        window.add(currentEnemyCount).right().padLeft(15).row();
        window.add(new VisLabel("Spice:")).left();
        window.add(spice).right().padLeft(15).row();

        stage.addActor(window);
    }

    /** Changes the money for the whole game.
     * Not the prettiest implementation but it gets the job done.
     * @param money If positive, money will increase, if negative, money will decrease.
     * @see GameScreen**/
    public static void changeMoney(int money){
        GameScreen.money += money;
        spice.setText(GameScreen.money);
    }

    /** Changes the wave number for the whole game.
     * Not the prettiest implementation but it gets the job done.
     * @see GameScreen**/
    public static void changeWaveNo(){
        GameScreen.waveNumber++;
        waveNo.setText(GameScreen.waveNumber);
    }

    /** Sets the total amount of enemies this wave.
     * Not the prettiest implementation but it gets the job done.
     * @param maxEnemies Amount of total enemies this wave.
     * @see GameScreen**/
    public static void setWaveEnemies(int maxEnemies){
        currentWaveMaxEnemies = maxEnemies;
        currentWaveEnemies = 0;
        currentEnemyCount.setText("0/" + currentWaveMaxEnemies);
    }

    /** Increases the kill counter for the enemies in the current wave.
     * Not the prettiest implementation but it gets the job done.
     * @see GameScreen**/
    public static void addWaveEnemies(){
        currentWaveEnemies++;
        currentEnemyCount.setText(currentWaveEnemies + "/" + currentWaveMaxEnemies);
    }

    /** Subtracts one of the total amount of lives.
     * Not the prettiest implementation but it gets the job done.
     * @see GameScreen**/
    public static void changeLives(){
        GameScreen.lives--;
        totalLives.setText(Integer.toString(GameScreen.lives));
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
