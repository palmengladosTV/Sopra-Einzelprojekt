package io.swapastack.dunetd;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Null;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisLabel;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import io.swapastack.dunetd.UI.GameUI;
import io.swapastack.dunetd.UI.TowerPickerWidget;
import io.swapastack.dunetd.util.*;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
import org.decimal4j.util.DoubleRounder;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * The GameScreen class.
 *
 * @author Dennis Jehle
 */
public class GameScreen implements Screen {

    private final DuneTD parent;

    public static int waveNumber;

    private static final byte FRAMEINTERVALL = 20;

    private static Vector2 startPos, endPos;

    public static int[][] gameField;
    public static boolean startPortalPlaced = false;
    public static boolean endPortalPlaced = false;
    private static boolean allowEnemySpawn = false;
    private static byte frameInterval;
    private static long frames;

    private static LinkedList<Vector2> path;

    private static LinkedList<Enemy> currentWaveEnemyPile;
    private static ArrayList<Enemy> currentWaveEnemiesSpawned;

    // GDX GLTF
    private static SceneManager sceneManager;
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
    public static HashMap<String, SceneAsset> sceneAssetHashMap;

    // Grid Specifications
    private static int rows = 5;
    private static int cols = 5;
    public static Vector3 groundTileDimensions;

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
        frameInterval = 0;
        initGameUI();
    }

    public GameScreen(DuneTD parent, byte fieldX, byte fieldY) {
        this.parent = parent;
        rows = fieldX;
        cols = fieldY;
        frameInterval = 0;
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
        for (String kenneyModel : kenneyModels) {
            DuneTD.assetManager.load(basePath + kenneyModel, SceneAsset.class);
        }
        // Load example enemy models
        DuneTD.assetManager.load("faceted_character/scene.gltf", SceneAsset.class);
        DuneTD.assetManager.load("cute_cyborg/scene.gltf", SceneAsset.class);
        DuneTD.assetManager.load("spaceship_orion/scene.gltf", SceneAsset.class);

        // Load all custom 3D models
        DuneTD.assetManager.load("timpalm/start_portal.glb",SceneAsset.class);
        DuneTD.assetManager.load("timpalm/end_portal.glb",SceneAsset.class);
        DuneTD.assetManager.load("timpalm/klopfer.glb",SceneAsset.class);

        // Finish up loading 3D models
        DuneTD.assetManager.finishLoading();

        // Create scene assets for all loaded models
        sceneAssetHashMap = new HashMap<>();
        for (String kenneyModel : kenneyModels) {
            SceneAsset sceneAsset = DuneTD.assetManager.get(basePath + kenneyModel, SceneAsset.class);
            sceneAssetHashMap.put(kenneyModel, sceneAsset);
        }
        SceneAsset bossCharacter = DuneTD.assetManager.get("faceted_character/scene.gltf");
        sceneAssetHashMap.put("faceted_character/scene.gltf", bossCharacter);
        SceneAsset enemyCharacter = DuneTD.assetManager.get("cute_cyborg/scene.gltf");
        sceneAssetHashMap.put("cute_cyborg/scene.gltf", enemyCharacter);
        SceneAsset harvesterCharacter = DuneTD.assetManager.get("spaceship_orion/scene.gltf");
        sceneAssetHashMap.put("spaceship_orion/scene.gltf", harvesterCharacter);

        // Create scene assets for all loaded custom models
        SceneAsset startPortal = DuneTD.assetManager.get("timpalm/start_portal.glb");
        sceneAssetHashMap.put("timpalm/start_portal.glb", startPortal);
        
        SceneAsset endPortal = DuneTD.assetManager.get("timpalm/end_portal.glb");
        sceneAssetHashMap.put("timpalm/end_portal.glb", endPortal);

        SceneAsset klopfer = DuneTD.assetManager.get("timpalm/klopfer.glb");
        sceneAssetHashMap.put("timpalm/klopfer.glb", klopfer);

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

        /*ImGui.begin("Menu", ImGuiWindowFlags.AlwaysAutoResize);
        if (ImGui.button("Back to menu")) {
            parent.changeScreen(ScreenEnum.MENU);
        }
        ImGui.end();*/

        // SpaiR/imgui-java
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        //Update UI
        gameUI.update(delta);

        //Draw UI
        gameUI.render();

        if(allowEnemySpawn)
            moveEnemies();
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
        gameField = new int[rows][cols];
        groundTileDimensions = createGround();
    }

    private static Vector3 createGround(){
        Vector3 groundTileDimensions = new Vector3();

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
                gridTile.modelInstance.transform.setToTranslation(i * modelDimensions.x, 0.0f, k * modelDimensions.z);
                // Add the Scene object to the SceneManager for rendering
                sceneManager.addScene(gridTile);
                // it could be useful to store the Scene object reference outside this method
            }
        }

        return groundTileDimensions;
    }

    public static void addNewTower(Vector2 coords, int towerIndex){
        gameField[(int) coords.x][(int) coords.y] = towerIndex;
        float internalX = coords.x;
        coords.x = gameField.length - 1 - coords.x; //Point of origin of the array is differs with point of origin of Scene Manager
        switch(towerIndex){
            case 1:
                Scene sonicTower = new Scene(sceneAssetHashMap.get("towerRound_crystals.glb").scene);
                sonicTower.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
                sceneManager.addScene(sonicTower);
                break;
            case 2:
                Scene canonTower = new Scene(sceneAssetHashMap.get("weapon_cannon.glb").scene);
                canonTower.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
                sceneManager.addScene(canonTower);
                break;
            case 3:
                Scene bombTower = new Scene(sceneAssetHashMap.get("weapon_blaster.glb").scene);
                bombTower.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
                sceneManager.addScene(bombTower);
                break;
            case 4:
                Scene wall = new Scene(sceneAssetHashMap.get("towerRound_roofB.glb").scene);
                wall.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
                sceneManager.addScene(wall);
                break;
            case 5:
                Scene klopfer = new Scene(sceneAssetHashMap.get("timpalm/klopfer.glb").scene);
                klopfer.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y, coords.y);
                klopfer.modelInstance.transform.scale(0.2f, 0.2f, 0.2f);
                klopfer.modelInstance.transform.rotate(new Vector3(0f,1f,0f),30f);
                sceneManager.addScene(klopfer);
                break;
            case 6:
                startPortalPlaced = true;
                startPos = new Vector2(internalX,coords.y);
                Scene startPortal = new Scene(sceneAssetHashMap.get("timpalm/start_portal.glb").scene);
                startPortal.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y+0.25f, coords.y);
                startPortal.modelInstance.transform.scale(0.25f, 0.25f, 0.25f);
                startPortal.modelInstance.transform.rotate(0f,1f,0f, 270f);
                sceneManager.addScene(startPortal);
                break;
            case 7:
                endPortalPlaced = true;
                endPos = new Vector2(internalX,coords.y);
                Scene endPortal = new Scene(sceneAssetHashMap.get("timpalm/end_portal.glb").scene);
                endPortal.modelInstance.transform.setToTranslation(coords.x, groundTileDimensions.y+0.25f, coords.y);
                endPortal.modelInstance.transform.scale(0.25f, 0.25f, 0.25f);
                endPortal.modelInstance.transform.rotate(0f,1f,0f, 270f);
                sceneManager.addScene(endPortal);
                break;
        }

    }

    public static void removeTower(Vector2 coords){
        if (gameField[(int) coords.x][(int) coords.y] == 6)
            startPortalPlaced = false;
        else if (gameField[(int) coords.x][(int) coords.y] == 7){
            endPortalPlaced = false;
        }

        gameField[(int) coords.x][(int) coords.y] = 0;
        coords.x = gameField.length - 1 - coords.x; //Point of origin of the array is differs with point of origin of Scene Manager

        sceneManager.getRenderableProviders().forEach(s -> { //Remove object from 3D scene
            Scene current = (Scene) s;                         //Es wird eine 4x4 Matrix zur Beschreibung
            float x = current.modelInstance.transform.val[12]; //von Translation eines 3D-Objektes im Raum
            float y = current.modelInstance.transform.val[13]; //verwendet. Da steht alles drin wie Skalierung,
            float z = current.modelInstance.transform.val[14]; //Rotation etc. Für die Translation sind jedoch nur
            if (x == coords.x && y != 0f && z == coords.y){    //die 3 Einträge in der letzten Spalte interessant.
                sceneManager.removeScene(current);             //Also die Einträge an den Stellen 12, 13 und 14.
            }
        });
    }

    public static void createPath(){
        //path = PortalPathFinder.findShortestPath(Arrays.stream(gameField).map(int[]::clone).toArray(int[][]::new));
        Graph g = new Graph(gameField);
        path = new LinkedList<>();

        for (Node n : Dijkstra.getPath(g,g.getNode(startPos),g.getNode(endPos)))
            path.add(n.getCoords());

        path.forEach(ll -> System.out.print("(" + ll.x + "," + ll.y + "), "));
        System.out.println();

        if(path.size() <= 1){
            VisDialog ii = new VisDialog("Path error");
            VisLabel il = new VisLabel("Couldn't find a path from the start portal\n" +
                    "to the end portal. Please make sure that it is possible to get\n" +
                    "from the start portal to the end portal");
            il.setAlignment(Align.center);
            ii.text(il);
            ii.button("OK");
            GameUI.showDialog(ii);
            TowerPickerWidget.b.setText("Initialize wave");
            TowerPickerWidget.waveReady = false;
            return;
        }
        path.forEach(v ->{
            v.x = gameField.length - 1 - v.x;
        });

        for (int i = sceneManager.getRenderableProviders().size - 1; i >= 0; i--){
            Scene current = (Scene) sceneManager.getRenderableProviders().get(i);
            float y = current.modelInstance.transform.val[13];
            if(y == 0f)
                sceneManager.removeScene(current);
        }

        createGround();

        HashSet<Scene> snowTiles = new HashSet<Scene>();

        path.forEach(v ->{
            sceneManager.getRenderableProviders().forEach(s -> {
                Scene current = (Scene) s;
                float x = current.modelInstance.transform.val[12];
                float y = current.modelInstance.transform.val[13];
                float z = current.modelInstance.transform.val[14];

                if (x == v.x && y == 0f && z == v.y && !snowTiles.contains(current)){
                    sceneManager.removeScene(current);
                    Scene pathGround = new Scene(sceneAssetHashMap.get("snow_tile.glb").scene);
                    pathGround.modelInstance.transform.setToTranslation(v.x, 0f, v.y);
                    pathGround.modelInstance.transform.scale(1,0.5f, 1);
                    snowTiles.add(pathGround);
                    sceneManager.addScene(pathGround);
                }
            });
        });
    }

    @SuppressWarnings("unchecked")
    public static void createEnemies(){
        currentWaveEnemyPile = new LinkedList<>();
        for(int i = 0; i < 10; i++){
            Enemy current;
            current = new Infantry(100, 5, path.get(0), (LinkedList<Vector2>) path.clone());
            //current.setModelToPosition(new Vector3(path.get(0).x,groundTileDimensions.y,path.get(0).y));
            current.setModelToPosition();
            currentWaveEnemyPile.add(current);
        }
        frameInterval = 0;
        frames = -1;
        currentWaveEnemiesSpawned = new ArrayList<Enemy>();
        allowEnemySpawn = true;
    }

    private static void moveEnemies(){
        frameInterval++;
        if(frameInterval % FRAMEINTERVALL != 0){
            return;
        }
        frameInterval = 0;
        frames++;

        Enemy removedEnemy = null;

        for (Enemy currentEnemy : currentWaveEnemiesSpawned){
        //for (int i = 0; i < currentWaveEnemiesSpawned.size(); i++){
            //Enemy currentEnemy = currentWaveEnemiesSpawned.get(i);
            sceneManager.removeScene(currentEnemy.getModel());
            float currentX = (float) DoubleRounder.round(currentEnemy.getCoords().x, 4);
            float currentY = (float) DoubleRounder.round(currentEnemy.getCoords().y, 4);

            if(new Vector2(currentX,currentY).equals(currentEnemy.destination.getFirst()))
                currentEnemy.destination.removeFirst();

            if(currentEnemy.destination.isEmpty()){
                //TODO: Enemy ist am End-Portal angelangt
                removedEnemy = currentEnemy;
                continue;
            }

            byte secondaryVelocity = 100;
            float totalVelocity = (float) currentEnemy.getVelocity()/secondaryVelocity;
            //System.out.println("Enemy Number: " + i + " X:" + currentX + " Z: " + currentY);

            if(currentX < currentEnemy.destination.getFirst().x){
                currentEnemy.setCoords(currentX + totalVelocity, currentY);
                currentEnemy.moveModel(totalVelocity, 0f);
                currentEnemy.rotateModel(90);
            }
            if(currentX > currentEnemy.destination.getFirst().x){
                currentEnemy.setCoords(currentX - totalVelocity, currentY);
                currentEnemy.moveModel(-totalVelocity, 0f);
                currentEnemy.rotateModel(270);
            }
            if(currentY < currentEnemy.destination.getFirst().y){
                currentEnemy.setCoords(currentX, currentY + totalVelocity);
                currentEnemy.moveModel(0f, totalVelocity);
                currentEnemy.rotateModel(180);
            }
            if(currentY > currentEnemy.destination.getFirst().y){
                currentEnemy.setCoords(currentX, currentY - totalVelocity);
                currentEnemy.moveModel(0f, -totalVelocity);
                currentEnemy.rotateModel(0);
            }
            sceneManager.addScene(currentEnemy.getModel());
        }

        try {
            currentWaveEnemiesSpawned.remove(removedEnemy);
        } catch (NullPointerException ignored) { }

        short spawnIntervall = 10;
        if(frames % spawnIntervall == 0 && 0 < currentWaveEnemyPile.size()){
            Enemy currentEnemy = currentWaveEnemyPile.poll();
            currentWaveEnemiesSpawned.add(currentEnemy);
            sceneManager.addScene(currentEnemy.getModel());
        }

    }

    private void towerShoot(){
        if(frameInterval % FRAMEINTERVALL != FRAMEINTERVALL/2){
            return;
        }





    }


    /**
     * This function acts as a starting point.
     * It generate a simple rectangular map with towers placed on it.
     * It doesn't provide any functionality, but it uses some common ModelInstance specific functions.
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
