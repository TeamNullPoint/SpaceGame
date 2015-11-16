package com.mygdx.game;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

/**
 * Created by Jaden on 15/11/2015.
 */
public class PlayerSystem extends IteratingSystem {
    public PlayerSystem(Family family, int priority) {
        super(family, priority);
    }

    public PlayerSystem(Family family) {
        super(family);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }
}
