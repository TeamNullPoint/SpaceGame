package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 *
 */
public class PlatformComponent implements Component {
    Vector2 originalPosition;
    float timePassed = 0;
    public PlatformComponent(){
    }
}
