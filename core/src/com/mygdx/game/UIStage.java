package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.uwsoft.editor.renderer.SceneLoader;
import com.uwsoft.editor.renderer.components.additional.ButtonComponent;
import com.uwsoft.editor.renderer.data.CompositeItemVO;
import com.uwsoft.editor.renderer.data.ProjectInfoVO;
import com.uwsoft.editor.renderer.resources.IResourceRetriever;
import com.uwsoft.editor.renderer.scene2d.CompositeActor;

/**
 * Create the separate stage for the UI components.
 */
public class UIStage extends Stage {

    private int numberHits = 0;
    public UIStage(IResourceRetriever ir){

        Gdx.input.setInputProcessor(this);
        ProjectInfoVO projectInfo = ir.getProjectVO();

        //create button data
        CompositeItemVO leftbuttondata = projectInfo.libraryItems.get("leftbutton");
        CompositeItemVO rightbuttondata = projectInfo.libraryItems.get("rightbutton");
        CompositeItemVO abuttondata = projectInfo.libraryItems.get("abutton");
        CompositeItemVO bbuttondata = projectInfo.libraryItems.get("bbutton");
        CompositeItemVO health = projectInfo.libraryItems.get("health");

        //create actors from button data
        CompositeActor leftbuttonActor = new CompositeActor(leftbuttondata, ir);
        CompositeActor rightbuttonActor = new CompositeActor(rightbuttondata, ir);
        CompositeActor abuttonActor = new CompositeActor(abuttondata, ir);
        CompositeActor bbuttonActor = new CompositeActor(bbuttondata, ir);
        CompositeActor healthActor = new CompositeActor(health, ir);

        //add buttons to screen
        addActor(leftbuttonActor);
        addActor(rightbuttonActor);
        addActor(abuttonActor);
        addActor(bbuttonActor);
        addActor(healthActor);

        //set actor positions
        leftbuttonActor.setX(0);
        leftbuttonActor.setY(0);
        rightbuttonActor.setX(200);
        rightbuttonActor.setY(2);
        bbuttonActor.setX(400);
        bbuttonActor.setY(3);
        abuttonActor.setX(599);
        abuttonActor.setY(4);
        healthActor.setX(20);
        healthActor.setY(400);

        //set listeners for buttons
        leftbuttonActor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    Player.moveLeft(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    Player.moveLeft(false);
            }
        });

        rightbuttonActor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Player.moveRight(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Player.moveRight(false);
            }
        });
        abuttonActor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Player.dojump(true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Player.dojump(false);
            }
        });
        bbuttonActor.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Player.doshoot(true);
                return true;
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Player.doshoot(false);
            }
        });
    }
    public void hit()
    {
        numberHits++;
    }
}
