package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ai.steer.limiters.NullLimiter;
import com.badlogic.gdx.ai.steer.utils.Collision;
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
    private static SceneLoader sceneLoader;
    private static Viewport viewport;
    private static ResourceManager resourceManager;
    private AssetManager assetManager;
    private static Player player;
    private static UIStage uiStage;
    private static ItemWrapper root;
    private static Boolean playing = false;


    @Override
    public void create() {
        assetManager = new AssetManager();
        resourceManager = new ResourceManager();
        resourceManager.initAllResources();

        viewport = new FitViewport(180, 120);
        sceneLoader = new SceneLoader();

        sceneLoader.loadScene(NullConstants.TITLE_SCREEN, viewport);

        root = new ItemWrapper(sceneLoader.getRoot());

        sceneLoader.addComponentsByTagName("button", ButtonComponent.class);




        root.getChild("beginbutton").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {

            @Override
            public void touchUp() {
                level();
            }
            @Override
            public void touchDown() {
            }
            @Override
            public void clicked() {
            }
        });

        root.getChild("howtoplay").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {
                SimpleImageVO HowtoPlay = new SimpleImageVO();
                HowtoPlay.imageName = "HowtoPlay";
                HowtoPlay.x = 55;
                HowtoPlay.y = 20;
                sceneLoader.entityFactory.createEntity(sceneLoader.getRoot(), HowtoPlay);
            }

            @Override
            public void touchDown() {

            }

            @Override
            public void clicked() {

            }
        });
    }

    public void render() {
        if (playing)
            Gdx.gl.glClearColor(0, 0, 0, 0.2f);
        else
            Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sceneLoader.getEngine().update(Gdx.graphics.getDeltaTime());

        //if the gameplay scene is up
        if (playing) {
            uiStage.act();
            uiStage.draw();
            ((OrthographicCamera) viewport.getCamera()).position.x = player.getX() + player.getWidth() / 2f;
            if (player.getY() > 0)
                ((OrthographicCamera) viewport.getCamera()).position.y = player.getY() + player.getWidth() / 2f;
            if(player.getY() < -20)
            {
                uiStage.clear();
                gameOver();
            }
        }
    }

    public static void level(){
        sceneLoader = new SceneLoader(resourceManager);
        sceneLoader.loadScene(NullConstants.MAIN_SCENE, viewport);
        root = new ItemWrapper(sceneLoader.getRoot());
        player = new Player(sceneLoader.world);
        root.getChild(NullConstants.PLAYER).addScript(player);

        uiStage = new UIStage(sceneLoader.getRm());

        sceneLoader.addComponentsByTagName(NullConstants.PLATFORM, PlatformComponent.class);
//        sceneLoader.addComponentsByTagName(NullConstants.ENEMY, CollisionComponent.class);
        sceneLoader.addComponentsByTagName(NullConstants.ENEMY, EnemyComponent.class);


        sceneLoader.getEngine().addSystem(new PlatformSystem());
  //      sceneLoader.getEngine().addSystem(new CollisionSystem(player));
        sceneLoader.getEngine().addSystem(new EnemySystem(player));

        playing = true;
    }

    public static void gameOver() {
        sceneLoader = new SceneLoader();
        sceneLoader.loadScene(NullConstants.GAME_OVER, viewport);
        root = new ItemWrapper(sceneLoader.getRoot());
        sceneLoader.addComponentsByTagName("button", ButtonComponent.class);
        root.getChild("retry").getEntity().getComponent(ButtonComponent.class).addListener(new ButtonComponent.ButtonListener() {
            @Override
            public void touchUp() {

            }

            @Override
            public void touchDown() {

            }

            @Override
            public void clicked() {
                System.out.print("yes");
            }
        });
    }
}