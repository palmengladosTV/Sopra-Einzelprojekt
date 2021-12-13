package io.swapastack.dunetd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class DuneTD extends Game {

	// The AssetManager is used to load game assets like 3D gltf models or pngs
	public static AssetManager assetManager = new AssetManager();

	/**
	 * This function can be used to switch screens.
	 *
	 * @param screen {@link ScreenEnum}
	 * @author Dennis Jehle
	 */
	public void changeScreen(ScreenEnum screen) {
		// get reference to current Screen object
		Screen currentScreen = this.getScreen();
		// set the screen to null
		this.setScreen(null);
		// if the current screen reference is not null...
		if (currentScreen != null) {
			// dispose all resources from this screen instance
			currentScreen.dispose();
		}
		// create new Screen instance and set it as current
		switch (screen) {
			case MENU:
				this.setScreen(new MainMenuScreen(this));
				break;
			case GAME:
				this.setScreen(new GameScreen(this));
				break;
			case SHOWCASE:
				this.setScreen(new ModelShowcase(this));
				break;
		}
	}
	
	@Override
	public void create () {
		// set window title
		Gdx.graphics.setTitle("Dune-TD - Sopra 2021 / 2022");

		// configure asset manager to work with gdx gltf
		assetManager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
		assetManager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());

		// load the main menu screen
		changeScreen(ScreenEnum.MENU);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		// if screen is not null ...
		if (screen != null) {
			// dispose all resources from screen
			screen.dispose();
		}
		// free all resources allocated by the AssetManager
		assetManager.dispose();
	}
}
