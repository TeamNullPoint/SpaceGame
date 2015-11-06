package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class PlatformerTutorial extends ApplicationAdapter {
    private SceneLoader sceneLoader;
    private Viewport viewport;
    private ResourceManager resourceManager;
    private AssetManager assetManager;
    private Player player;
    private UIStage uiStage;

    @Override
    public void create () {
        assetManager = new AssetManager();
        resourceManager = new ResourceManager();
        resourceManager.initAllResources();
        viewport = new FitViewport(189,100);
        sceneLoader = new SceneLoader(resourceManager);
        sceneLoader.loadScene(NullConstants.MAIN_SCENE, viewport);

        ItemWrapper root = new ItemWrapper(sceneLoader.getRoot());

        player = new Player(sceneLoader.world);
        root.getChild(NullConstants.PLAYER).addScript(player);

        sceneLoader.addComponentsByTagName(NullConstants.PLATFORM, PlatformComponent.class);
        sceneLoader.getEngine().addSystem(new PlatformSystem());
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());
        //asdasd
        //uiStage.act();
       // uiStage.draw();
        ((OrthographicCamera) viewport.getCamera()).position.x = player.getX() + player.getWidth()/2f;
    }
}