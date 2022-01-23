package io.swapastack.dunetd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.kotcrab.vis.ui.VisUI;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import io.swapastack.dunetd.UI.GameUI;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

import java.util.HashMap;
import java.util.Locale;

/**
 * The GameScreen class.
 *
 * @author Dennis Jehle
 */
public class GameScreen implements Screen {

    private final DuneTD parent;

    public static int[][] gameField;

    // GDX GLTF
    private SceneManager sceneManager;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;

    // libGDX
    private PerspectiveCamera camera;
    private CameraInputController cameraInputController;

    // 3D models
    String basePath = "kenney_gltf/";
    String kenneyAssetsFile = "kenney_assets.txt";
    String[] kenneyModels;
    HashMap<String, SceneAsset> sceneAssetHashMap;

    // Grid Specifications
    private int rows = 5;
    private int cols = 5;

    // Animation Controllers
    AnimationController bossCharacterAnimationController;
    AnimationController enemyCharacterAnimationController;
    AnimationController spaceshipAnimationController;

    // SpaiR/imgui-java
    public ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    public ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    long windowHandle;

    //Game UI
    GameUI gameUI;

    public GameScreen(DuneTD parent) {
        this.parent = parent;
        initGameUI();
    }

    public GameScreen(DuneTD parent, byte fieldX, byte fieldY) {
        this.parent = parent;
        this.rows = fieldX;
        this.cols = fieldY;
        initGameUI();
    }

    public void initGameUI(){
        gameUI = new GameUI(parent);
    }
    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

        // SpaiR/imgui-java
        ImGui.createContext();
        windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 120");

        // GDX GLTF - Scene Manager
        sceneManager = new SceneManager(64);

        // GDX GLTF - Light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // GDX GLTF - Image Based Lighting
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // GDX GLTF - This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        // GDX GLTF - Cubemaps
        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // GDX GLTF - Skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        // Camera
        camera = new PerspectiveCamera();
        camera.position.set(10.0f, 10.0f, 10.0f);
        camera.lookAt(Vector3.Zero);
        sceneManager.setCamera(camera);

        // Camera Input Controller
        cameraInputController = new CameraInputController(camera);

        // Set Input Processor
        // TODO: add further input processors if needed
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gameUI.inputMultiplexerUI);
        inputMultiplexer.addProcessor(cameraInputController);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Load all 3D models listed in kenney_assets.txt file in blocking mode
        FileHandle assetsHandle = Gdx.files.internal("kenney_assets.txt");
        String fileContent = assetsHandle.readString();
        kenneyModels = fileContent.split("\\r?\\n");
        for (int i = 0; i < kenneyModels.length; i++) {
            parent.assetManager.load(basePath + kenneyModels[i], SceneAsset.class);
        }
        // Load example enemy models
        parent.assetManager.load("faceted_character/scene.gltf", SceneAsset.class);
        parent.assetManager.load("cute_cyborg/scene.gltf", SceneAsset.class);
        parent.assetManager.load("spaceship_orion/scene.gltf", SceneAsset.class);
        DuneTD.assetManager.finishLoading();

        // Create scene assets for all loaded models
        sceneAssetHashMap = new HashMap<>();
        for (int i = 0; i < kenneyModels.length; i++) {
            SceneAsset sceneAsset = parent.assetManager.get(basePath + kenneyModels[i], SceneAsset.class);
            sceneAssetHashMap.put(kenneyModels[i], sceneAsset);
        }
        SceneAsset bossCharacter = parent.assetManager.get("faceted_character/scene.gltf");
        sceneAssetHashMap.put("faceted_character/scene.gltf", bossCharacter);
        SceneAsset enemyCharacter = parent.assetManager.get("cute_cyborg/scene.gltf");
        sceneAssetHashMap.put("cute_cyborg/scene.gltf", enemyCharacter);
        SceneAsset harvesterCharacter = parent.assetManager.get("spaceship_orion/scene.gltf");
        sceneAssetHashMap.put("spaceship_orion/scene.gltf", harvesterCharacter);

        //createMapExample(sceneManager);
        createMap(sceneManager);

    }

    /**
     * Called when the screen should render itself.
     * @param delta - The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // OpenGL - clear color and depth buffer
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        try { bossCharacterAnimationController.update(delta); }
        catch (NullPointerException ignored) { }
        try { enemyCharacterAnimationController.update(delta); }
        catch (NullPointerException ignored) { }
        try{ spaceshipAnimationController.update(delta); }
        catch (NullPointerException ignored) { }

        // SpaiR/imgui-java
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        // GDX GLTF - update scene manager and render scene
        sceneManager.update(delta);
        sceneManager.render();

        ImGui.begin("Performance", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.text(String.format(Locale.US,"deltaTime: %1.6f", delta));
        ImGui.end();

        ImGui.begin("Menu", ImGuiWindowFlags.AlwaysAutoResize);
        if (ImGui.button("Back to menu")) {
            parent.changeScreen(ScreenEnum.MENU);
        }
        ImGui.end();

        // SpaiR/imgui-java
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        //Update UI
        gameUI.update(delta);

        //Draw UI
        gameUI.render();
    }

    @Override
    public void resize(int width, int height) {
        // GDX GLTF - update the viewport
        sceneManager.updateViewport(width, height);
        //gameUI.resize(width,height);
    }

    @Override
    public void pause() {
        // TODO: implement pause logic if needed
    }

    @Override
    public void resume() {
        // TODO: implement resume logic if needed
    }

    @Override
    public void hide() {
        // TODO: implement hide logic if needed
    }

    private void createMap(SceneManager sceneManager){
        Vector3 groundTileDimensions = createGround();
    }

    private Vector3 createGround(){
        Vector3 groundTileDimensions = new Vector3();
        gameField = new int[rows][cols];

        // Simple way to generate the example map
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < cols; k++) {
                // Create a new Scene object from the tile_dirt gltf model
                Scene gridTile = new Scene(sceneAssetHashMap.get("tile_dirt.glb").scene);
                // Create a new BoundingBox, this is useful to check collisions or to get the model dimensions
                BoundingBox boundingBox = new BoundingBox();
                // Calculate the BoundingBox from the given ModelInstance
                gridTile.modelInstance.calculateBoundingBox(boundingBox);
                // Create Vector3 to store the ModelInstance dimensions
                Vector3 modelDimensions = new Vector3();
                // Read the ModelInstance BoundingBox dimensions
                boundingBox.getDimensions(modelDimensions);
                // TODO: refactor this if needed, e.g. if ground tiles are not all the same size
                groundTileDimensions.set(modelDimensions);
                // Set the ModelInstance to the respective row and cell of the map
                gridTile.modelInstance.transform.setToTranslation(k * modelDimensions.x, 0.0f, i * modelDimensions.z);
                // Add the Scene object to the SceneManager for rendering
                sceneManager.addScene(gridTile);
                // it could be useful to store the Scene object reference outside this method
            }
        }

        return groundTileDimensions;
    }

    /**
     * This function acts as a starting point.
     * It generate a simple rectangular map with towers placed on it.
     * It doesn't provide any functionality, but it uses some common ModelInstance specific functions.
     * @param sceneManager
     */
    private void createMapExample(SceneManager sceneManager) {

        Vector3 groundTileDimensions = createGround();

        // place example sonicTower
        Scene sonicTower = new Scene(sceneAssetHashMap.get("towerRound_crystals.glb").scene);
        sonicTower.modelInstance.transform.setToTranslation(0.0f, groundTileDimensions.y, 0.0f);
        sceneManager.addScene(sonicTower);

        // place example canonTower
        Scene canonTower = new Scene(sceneAssetHashMap.get("weapon_cannon.glb").scene);
        canonTower.modelInstance.transform.setToTranslation(1.0f, groundTileDimensions.y, 0.0f);
        sceneManager.addScene(canonTower);

        // place example bombTower
        Scene bombTower = new Scene(sceneAssetHashMap.get("weapon_blaster.glb").scene);
        bombTower.modelInstance.transform.setToTranslation(2.0f, groundTileDimensions.y, 0.0f);
        sceneManager.addScene(bombTower);

        // place boss character
        Scene bossCharacter = new Scene(sceneAssetHashMap.get("faceted_character/scene.gltf").scene);
        bossCharacter.modelInstance.transform.setToTranslation(0.0f, groundTileDimensions.y, 2.0f).scale(0.005f, 0.005f, 0.005f);
        sceneManager.addScene(bossCharacter);

        bossCharacterAnimationController = new AnimationController(bossCharacter.modelInstance);
        bossCharacterAnimationController.setAnimation("Armature|Run", -1);

        // place enemy character
        Scene enemyCharacter = new Scene(sceneAssetHashMap.get("cute_cyborg/scene.gltf").scene);
        enemyCharacter.modelInstance.transform.setToTranslation(1.0f, groundTileDimensions.y, 2.0f)
                .scale(0.02f, 0.04f, 0.03f)
                .rotate(new Vector3(0.0f, 1.0f, 0.0f), 180.0f);
        sceneManager.addScene(enemyCharacter);

        enemyCharacterAnimationController = new AnimationController(enemyCharacter.modelInstance);
        enemyCharacterAnimationController.setAnimation("RUN", -1);

        // place spaceship character
        Scene spaceshipCharacter = new Scene(sceneAssetHashMap.get("spaceship_orion/scene.gltf").scene);
        spaceshipCharacter.modelInstance.transform.setToTranslation(2.0f, 0.25f, 2.0f)
                .scale(0.2f, 0.2f, 0.2f);
        sceneManager.addScene(spaceshipCharacter);

        spaceshipAnimationController = new AnimationController(spaceshipCharacter.modelInstance);
        spaceshipAnimationController.setAnimation("Action", -1);

    }

    @Override
    public void dispose() {
        // GDX GLTF - dispose resources
        sceneManager.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
        VisUI.dispose();
        gameUI.dispose();
    }

}
