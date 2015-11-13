package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.TransformComponent;

/**
 */
public class CollisionComponent implements Component {
    Vector2 position;
    float width;
    float height;

    public CollisionComponent(){}
}
