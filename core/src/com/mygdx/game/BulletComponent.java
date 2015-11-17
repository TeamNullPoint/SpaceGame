package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.TransformComponent;

/**
 * Created by Jaden on 15/11/2015.
 */
public class BulletComponent implements Component {
    public static final float LEFT_DIRECTION = -1;
    public static final float RIGHT_DIRECTION = 1;
    public float x;
    public float y;
    //public float scaleX;
    boolean isLive;
    public Vector2 originalPosition = null;
    public float currentDirection;

    public BulletComponent() {
    }

}
