package io.swapastack.dunetd;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import imgui.flag.ImGuiWindowFlags;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;
// SpaiR/imgui-java
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import java.util.Locale;


public class ModelShowcase implements Screen {

    SceneAsset sceneAsset;
    Scene scene;
    SceneManager sceneManager;
    SceneSkybox skybox;

    PerspectiveCamera camera;
    DirectionalLightEx light;
    Cubemap environmentCubemap;
    Cubemap diffuseCubemap;
    Cubemap specularCubemap;

    Texture brdfLUT;

    float time;

    // SpaiR/imgui-java
    public ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    public ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    long windowHandle;

    // imgui slider values
    private float[] radius = {2.222f};
    private float[] azimuthalAngle = {0.0f};
    private float[] polarAngle = {1.111f};

    // imgui file chooser values
    int selectedModel = 1;
    String kenneyModels[];
    String basePath = "kenney_gltf/";

    DuneTD parent;

    public ModelShowcase(DuneTD parent) {
        this.parent = parent;
    }

    @Override
    public void show() {

        FileHandle assetsHandle = Gdx.files.internal("kenney_assets.txt");
        String fileContent = assetsHandle.readString();
        kenneyModels = fileContent.split("\\r?\\n");

        for (int i = 0; i < kenneyModels.length; i++) {
            DuneTD.assetManager.load(basePath + kenneyModels[i], SceneAsset.class);
        }
        DuneTD.assetManager.finishLoading();

        // SpaiR/imgui-java
        ImGui.createContext();
        windowHandle = ((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle();
        imGuiGlfw.init(windowHandle, true);
        imGuiGl3.init("#version 120");

        // create scene
        sceneManager = new SceneManager();
        displayModel(selectedModel);

        // setup camera (The BoomBox model is very small so you may need to adapt camera settings for your scene)
        camera = new PerspectiveCamera(60f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        float d = 2000f;
        camera.near = d / 10000f;
        camera.far = d * 4;
        sceneManager.setCamera(camera);

        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();

        // This texture is provided by the library, no need to have it in your assets.
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keyCode) {
                switch (keyCode) {
                    case Keys.UP:
                        selectedModel = (selectedModel > 0) ? selectedModel - 1 : selectedModel;
                        break;
                    case Keys.DOWN:
                        selectedModel = (selectedModel < kenneyModels.length - 1) ? selectedModel + 1 : selectedModel;
                        break;
                    case Keys.ESCAPE:
                        parent.changeScreen(ScreenEnum.MENU);
                        break;
                }
                displayModel(selectedModel);
                return true;
            }
        });

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        time += delta;

        // animate camera
        setFromSpherical(camera, radius[0], azimuthalAngle[0], polarAngle[0]);
        camera.up.set(Vector3.Y);
        camera.lookAt(Vector3.Zero);
        camera.update();

        // SpaiR/imgui-java
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        ImGui.begin("Performance", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.text(String.format(Locale.US,"deltaTime: %1.6f", delta));
        ImGui.end();

        ImGui.begin("Camera", ImGuiWindowFlags.AlwaysAutoResize);
        ImGui.sliderFloat("radius", radius, 0.1f, 10.0f);
        ImGui.sliderFloat("azimuthalAngle", azimuthalAngle, 0.0f, MathUtils.PI2);
        ImGui.sliderFloat("polarAngle", polarAngle, 0.1f, MathUtils.PI);
        ImGui.end();

        ImGui.begin("Info");
        ImGui.text("Quick tip:");
        ImGui.text("1. Use the up and down arrow keys to navigate the file chooser.");
        ImGui.text("2. Press escape to go back to main menu.");
        ImGui.end();

        ImGui.begin("File Chooser");
        for (int i = 0; i < kenneyModels.length; i++) {
            if (ImGui.selectable(kenneyModels[i], i == selectedModel)) {
                selectedModel = i;
                displayModel(selectedModel);
            }
        }
        ImGui.end();

        // render
        sceneManager.update(delta);
        sceneManager.render();

        // SpaiR/imgui-java
        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    @Override
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    /**
     * This method is used to switch between the kenney 3d gltf models.
     *
     * @author Dennis Jehle
     * @param index - the index of the model name in the kenneyModels array
     */
    private void displayModel(int index) {
        // remove active model
        if (sceneAsset != null)
            sceneAsset.dispose();
        if (scene != null)
            sceneManager.removeScene(scene);
        scene = null;
        // load new model
        sceneAsset = DuneTD.assetManager.get(basePath + kenneyModels[index], SceneAsset.class);
        scene  = new Scene(sceneAsset.scene);
        sceneManager.addScene(scene);
    }

    /**
     * Calculate the spherical coordinate for the camera.
     * Note: Y is pointing up, X is pointing right and Z is pointing towards screen in libGDX
     *       This is an alternative implementation for Vector3.setFromSpherical
     *
     * Wiki:
     * https://en.wikipedia.org/wiki/Spherical_coordinate_system
     * https://de.wikipedia.org/wiki/Kugelkoordinaten
     *
     * @author Dennis Jehle
     * @param camera - The camera which position should be set
     * @param radius - The distance between Zero and camera position
     * @param azimuthalAngle - The azimuthal angle [0, 2PI]
     * @param polarAngle - The polar angle (0, PI]
     */
    private void setFromSpherical(Camera camera, float radius, float azimuthalAngle, float polarAngle) {

        float cosPolar = MathUtils.cos(polarAngle);
        float sinPolar = MathUtils.sin(polarAngle);
        float cosAzim = MathUtils.cos(azimuthalAngle);
        float sinAzim = MathUtils.sin(azimuthalAngle);

        float x = radius * cosAzim * sinPolar;
        float y = radius * cosPolar;
        float z = radius * sinAzim * sinPolar;

        camera.position.set(x, y, z);
    }

    @Override
    public void dispose() {
        sceneManager.dispose();
        sceneAsset.dispose();
        environmentCubemap.dispose();
        diffuseCubemap.dispose();
        specularCubemap.dispose();
        brdfLUT.dispose();
        skybox.dispose();
        // SpaiR/imgui-java
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }
}
