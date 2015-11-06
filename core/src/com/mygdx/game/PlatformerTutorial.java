package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.data.SimpleImageVO;
import com.uwsoft.editor.renderer.resources.ResourceManager;
import com.uwsoft.editor.renderer.utils.ItemWrapper;

public class PlatformerTutorial extends ApplicationAdapter {
    private SceneLoader sceneLoader;
    private Viewport viewport;
    private ResourceManager resourceManager;
    private AssetManager assetManager;
    private Player player;
    private UIStage uiStage;
    private ItemWrapper root;
    private Boolean playing = false;


    @Override
    public void create () {
        assetManager = new AssetManager();
        resourceManager = new ResourceManager();
        resourceManager.initAllResources();

        viewport = new FitViewport(189, 100);
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene(NullConstants.TITLE_SCREEN, viewport);


        final SimpleImageVO vo = new SimpleImageVO();
        vo.imageName = "HowtoPlay.png";
        final Entity entity = new Entity();

        root = new ItemWrapper(sceneLoader.getRoot());

        sceneLoader.addComponentsByTagName("button", ButtonComponent.class);
        root.getChild("beginbutton").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {
                this.level();
            }

            @Override
            public void touchDown() {

            }

            @Override
            public void clicked() {

            }

            //holds the MainScene
            private void level() {
                sceneLoader = new SceneLoader(resourceManager);
                sceneLoader.loadScene(NullConstants.MAIN_SCENE, viewport);
                root = new ItemWrapper(sceneLoader.getRoot());
                player = new Player(sceneLoader.world);
                root.getChild(NullConstants.PLAYER).addScript(player);

                uiStage = new UIStage(sceneLoader.getRm());

                sceneLoader.addComponentsByTagName(NullConstants.PLATFORM, PlatformComponent.class);
                sceneLoader.getEngine().addSystem(new PlatformSystem());
                playing = true;
            }
        });
        root.getChild("HowButton").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {
            }

            @Override
            public void touchDown() {

            }

            @Override
            public void clicked() {

            }
            private void howtoplay() {
                sceneLoader = new SceneLoader();
                sceneLoader.loadScene(NullConstants.HOW_TO_PLAY, viewport);
                root = new ItemWrapper(sceneLoader.getRoot());

                sceneLoader.addComponentsByTagName(NullConstants.PLATFORM, PlatformComponent.class);
                sceneLoader.getEngine().addSystem(new PlatformSystem());
            }
        });
    }



    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());

        //if the gameplay scene is up
        if(playing) {
            uiStage.act();
            uiStage.draw();
            ((OrthographicCamera) viewport.getCamera()).position.x = player.getX() + player.getWidth() / 2f;
            ((OrthographicCamera) viewport.getCamera()).position.y = player.getY() + player.getWidth() / 2f;
        }
    }
}