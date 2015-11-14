package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Jaden on 13/11/2015.
 */
public class EnemyComponent implements Component {
    Vector2 originalPosition;
    float timePassed = 0;
    public EnemyComponent(){
    }
}
