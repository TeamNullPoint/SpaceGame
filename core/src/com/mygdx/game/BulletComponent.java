package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.TransformComponent;

/**
 * Created by Jaden on 15/11/2015.
 */
public class BulletComponent implements Component {
    public enum PLAYER_DIRECTION{
        LEFT_DIRECTION(-1), RIGHT_DIRECTION(1);
        private float direction;

        PLAYER_DIRECTION(float direction){
            this.direction = direction;
        }

        public float getDirection(){
            return this.direction;
        }
    }

    public float x;
    public float y;
    //public float scaleX;
    boolean isLive;
    public Vector2 originalPosition = null;
    public float currentDirection;

    public BulletComponent() {
    }

}
