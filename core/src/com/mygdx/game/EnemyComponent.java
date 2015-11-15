package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * The component for an enemy.
 */
public class EnemyComponent implements Component {
    Vector2 originalPosition;
    float timePassed = 0;
    Vector2 speed = new Vector2(33, 0);
    public EnemyComponent() {
    }
}
