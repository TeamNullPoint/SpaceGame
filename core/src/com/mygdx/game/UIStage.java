package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
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


        CompositeItemVO leftbuttondata = projectInfo.libraryItems.get("leftbutton");
        CompositeItemVO rightbuttondata = projectInfo.libraryItems.get("rightbutton");
        CompositeItemVO abuttondata = projectInfo.libraryItems.get("abutton");
        CompositeItemVO bbuttondata = projectInfo.libraryItems.get("bbutton");
        CompositeItemVO health = projectInfo.libraryItems.get("health");



        CompositeActor leftbuttonActor = new CompositeActor(leftbuttondata, ir);
        CompositeActor rightbuttonActor = new CompositeActor(rightbuttondata, ir);
        CompositeActor abuttonActor = new CompositeActor(abuttondata, ir);
        CompositeActor bbuttonActor = new CompositeActor(bbuttondata, ir);
        CompositeActor healthActor = new CompositeActor(health, ir);


        addActor(leftbuttonActor);
        addActor(rightbuttonActor);
        addActor(abuttonActor);
        addActor(bbuttonActor);
        addActor(healthActor);

        leftbuttonActor.setSize(50, 50);
        leftbuttonActor.setX(0);
        leftbuttonActor.setY(0);

        rightbuttonActor.setSize(50, 50);
        rightbuttonActor.setX(200);
        rightbuttonActor.setY(2);

        bbuttonActor.setSize(50, 50);
        bbuttonActor.setX(400);
        bbuttonActor.setY(3);

        abuttonActor.setSize(50, 50);
        abuttonActor.setX(599);
        abuttonActor.setY(4);

        healthActor.setX(20);
        healthActor.setY(400);
    }
    public void hit()
    {
        numberHits++;
    }
}
