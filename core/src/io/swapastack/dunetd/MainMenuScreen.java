package io.swapastack.dunetd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;


/**
 * This is the MainMenuScreen class.
 * This class is used to display the main menu.
 * It displays the name of the game in latin and japanese letters.
 * Multiple buttons for interaction with the app.
 *
 * @author Dennis Jehle
 */
public class MainMenuScreen implements Screen {

    // reference to the parent object
    // the reference is used to call methods of the parent object
    // e.g. parent_.get_window_dimensions()
    // the 'parent' object has nothing to do with inheritance in the accustomed manner
    // it is called 'parent' because the DuneTD class extends com.badlogic.gdx.Game
    // and each Game can have multiple classes which implement the com.badlogic.gdx.Screen
    // interface, so in this special case the Game is the parent of a Screen
    private final DuneTD parent;
    // see: https://github.com/libgdx/libgdx/wiki/Orthographic-camera
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/OrthographicCamera.html
    private final OrthographicCamera camera;
    // see: https://github.com/libgdx/libgdx/wiki/Viewports
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/utils/viewport/ScreenViewport.html
    private final Viewport viewport;
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/Stage.html
    private final Stage stage;
    // see: https://github.com/libgdx/libgdx/wiki/Spritebatch,-Textureregions,-and-Sprites
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/g2d/SpriteBatch.html
    private final SpriteBatch spriteBatch;
    // see: https://github.com/libgdx/libgdx/wiki/Skin
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/scenes/scene2d/ui/Skin.html
    // see: https://github.com/czyzby/gdx-skins (!!! other skins available here)
    private final Skin skin;
    // see: https://libgdx.info/basic-label/
    private final FreeTypeFontGenerator bitmapFontGenerator;
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/graphics/Texture.html
    private final Texture backgroundTexture;
    // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/audio/Music.html
    private final Music backgroundMusic;

    /**
     * This is the constructor for the MainMenuScreen class.
     *
     * @param parent reference to the parent object
     * @author Dennis Jehle
     */
    public MainMenuScreen(DuneTD parent) {
        VisUI.load();
        // store reference to parent class
        this.parent = parent;
        // initialize OrthographicCamera with current screen size
        // e.g. OrthographicCamera(1280.f, 720.f)
        camera = new OrthographicCamera((float)Gdx.graphics.getWidth(), (float)Gdx.graphics.getHeight());
        // initialize ScreenViewport with the OrthographicCamera created above
        viewport = new ScreenViewport(camera);
        // initialize SpriteBatch
        spriteBatch = new SpriteBatch();
        // initialize the Stage with the ScreenViewport created above
        stage = new Stage(viewport, spriteBatch);
        // initialize the Skin
        skin = VisUI.getSkin();
        //skin = new Skin(Gdx.files.internal("glassy/skin/glassy-ui.json"));

        // create string for BitmapFont and Label creation
        String duneTD = "Dune TD";

        // initialize FreeTypeFontGenerator for BitmapFont generation
        bitmapFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/NotoSansCJKtc_ttf/NotoSansCJKtc-Bold.ttf"));
        // specify parameters for BitmapFont generation
        FreeTypeFontGenerator.FreeTypeFontParameter bitmapFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        // set font size
        bitmapFontParameter.size = 60;
        // specify available letters
        bitmapFontParameter.characters = duneTD;
        // set font color in RGBA format (red, green, blue, alpha)
        bitmapFontParameter.color = new Color(1.f, 1.f, 0, 1.f);
        // other specifications
        bitmapFontParameter.borderWidth = 1;
        bitmapFontParameter.borderColor = Color.BLACK; // alternative enum color specification
        bitmapFontParameter.shadowOffsetX = 3;
        bitmapFontParameter.shadowOffsetY = 3;
        bitmapFontParameter.shadowColor = new Color(1.f, 1.f, 0, 0.25f);

        // generate BitmapFont with FreeTypeFontGenerator and FreeTypeFontParameter specification
        BitmapFont japanese_latin_font = bitmapFontGenerator.generateFont(bitmapFontParameter);

        // create a LabelStyle object to specify Label font
        Label.LabelStyle japanese_latin_label_style = new Label.LabelStyle();
        japanese_latin_label_style.font = japanese_latin_font;

        // create a Label with the main menu title string
        Label duneTDLabel = new Label(duneTD, japanese_latin_label_style);
        duneTDLabel.setFontScale(1, 1);
        duneTDLabel.setPosition(
                (float)Gdx.graphics.getWidth() / 2.f - duneTDLabel.getWidth() / 2.f
                , (float)Gdx.graphics.getHeight() / 2.f - duneTDLabel.getHeight() / 2.f
        );

        // add main menu title string Label to Stage
        stage.addActor(duneTDLabel);

        // load background texture
        backgroundTexture = new Texture("sharad-bhat-mZDOfgiQeE4-unsplash.jpg");

        // load background music
        // note: every game should have some background music
        //       feel free to exchange the current wav with one of your own music files
        //       but you must have the right license for the music file
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("piano/piano_loop.wav"));
        backgroundMusic.setLooping(true);
        //backgroundMusic.play(); // TODO: reactivate

        // create switch to GameScreen button //ORIG: add attribute "small"!!!
        Button gameScreenButton = new TextButton("Start Game", skin);
        gameScreenButton.setPosition(
                (float)Gdx.graphics.getWidth() / 2.f - gameScreenButton.getWidth() / 2.f
                , (float)Gdx.graphics.getHeight() / 2.f - 125.f
        );
        // add InputListener to Button, and close app if Button is clicked
        gameScreenButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                TextField f = new TextField("5", skin);
                TextField g = new TextField("5", skin);
                Dialog ss = new Dialog("Initial Game Options", skin){
                   public void result(Object o){
                       try {
                           byte dimX = Byte.parseByte(f.getText());
                           byte dimY = Byte.parseByte(g.getText());
                           if(dimX < 10 && dimY < 10 && dimX > 2 && dimY > 2)
                               parent.changeScreen(ScreenEnum.GAME, dimX, dimY);
                           else
                               invalidInput();
                       }
                       catch (NumberFormatException e){
                           invalidInput();
                       }
                   }

                   private void invalidInput(){
                        Dialog ii = new Dialog("Invalid Input", skin);
                        Label il = new Label("Your input must be a valid\nnumber and between 2 and 10!", skin);
                        il.setAlignment(Align.center);
                        ii.text(il);
                        ii.button("OK");
                        ii.show(stage);
                   }
               };
               ss.getContentTable().add(new Label("Field dimension:", skin));
               ss.getContentTable().add(f).width(20);
               ss.getContentTable().add(new Label("x", skin));
               ss.getContentTable().add(g).width(20);
               ss.button("Start game");
               //ss.getCells().forEach(c -> c.padBottom(3));
               //ss.debug();
               ss.show(stage);

            }

        });


        // add exit button to Stage
        stage.addActor(gameScreenButton);

        // create switch to GameScreen button
        Button showcaseButton = new TextButton("SHOWCASE", skin);
        showcaseButton.setPosition(
                (float)Gdx.graphics.getWidth() / 2.f - showcaseButton.getWidth() / 2.f
                , (float)Gdx.graphics.getHeight() / 2.f - 200.f
        );
        // add InputListener to Button, and close app if Button is clicked
        showcaseButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                parent.changeScreen(ScreenEnum.SHOWCASE);
            }
        });

        // add exit button to Stage
        stage.addActor(showcaseButton);

        // create exit application button
        Button exitButton = new TextButton("EXIT", skin);
        exitButton.setPosition(
                10.0f
                , 10.0f
        );
        // add InputListener to Button, and close app if Button is clicked
        exitButton.addListener(new InputListener(){
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });

        // add exit button to Stage
        stage.addActor(exitButton);

    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     *
     * @author Dennis Jehle
     */
    @Override
    public void show() {
        // this command is necessary that the stage receives input events
        // e.g. mouse click on exit button
        // see: https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/Input.html
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     * @author Dennis Jehle
     */
    @Override
    public void render(float delta) {
        // clear the client area (Screen) with the clear color (black)
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // update camera
        camera.update();

        // update the current SpriteBatch
        spriteBatch.setProjectionMatrix(camera.combined);

        // draw background graphic
        spriteBatch.begin();
        spriteBatch.setColor(Color.WHITE);
        spriteBatch.draw(backgroundTexture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        spriteBatch.end();

        // update the Stage
        stage.act(delta);
        // draw the Stage
        stage.draw();
    }

    /**
     * This method gets called after a window resize.
     *
     * @param width new window width
     * @param height new window height
     * @see ApplicationListener#resize(int, int)
     * @author Dennis Jehle
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * This method gets called if the application lost focus.
     *
     * @see ApplicationListener#pause()
     * @author Dennis Jehle
     */
    @Override
    public void pause() {}

    /**
     * This method gets called if the application regained focus.
     *
     * @see ApplicationListener#resume()
     * @author Dennis Jehle
     */
    @Override
    public void resume() {}

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     * @author Dennis Jehle
     */
    @Override
    public void hide() {
        backgroundMusic.stop();
    }

    /**
     * Called when this screen should release all resources.
     * @author Dennis Jehle
     */
    @Override
    public void dispose() {
        backgroundMusic.dispose();
        backgroundTexture.dispose();
        bitmapFontGenerator.dispose();
        skin.dispose();
        stage.dispose();
        spriteBatch.dispose();
        VisUI.dispose();
    }
}
