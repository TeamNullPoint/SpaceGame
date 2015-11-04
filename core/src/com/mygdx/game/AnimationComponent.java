package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;

/**
 * Component for the player animation.
 */
public class AnimationComponent implements Component {
    public IntMap<Animation> animations = new IntMap<Animation>();

}
