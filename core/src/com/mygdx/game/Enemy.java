package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationComponent;
import com.uwsoft.editor.renderer.components.sprite.SpriteAnimationStateComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.scripts.IScript;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Jaden on 15/11/2015.
 */
public class Enemy  implements IScript {
    private boolean grounded = false;
    private boolean stopJumpAnimation = true;
    private Player player;
    private Entity enemy;
    private static TransformComponent transformComponent;
    private static DimensionsComponent dimensionsComponent;
    private SpriteAnimationComponent spriteAnimationComponent;
    private SpriteAnimationStateComponent spriteAnimationStateComponent;



    private World world;

    public Enemy(World world, Player player) {

        this.world = world;
        this.player = player;
    }


    private float gravity = -120f;
    private static Vector2 speed;

    private final float jumpSpeed = 66f;

    @Override
    public void init(Entity entity) {
        enemy = entity;

        transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        spriteAnimationComponent = ComponentRetriever.get(entity, SpriteAnimationComponent.class);
        spriteAnimationStateComponent = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);

        speed = new Vector2(15, 0);

    }

    private void walkingState(){
        spriteAnimationStateComponent.set(spriteAnimationComponent.frameRangeMap.get("walking"), 13, Animation.PlayMode.LOOP);
    }
    private void standingState(){
        spriteAnimationStateComponent.set(spriteAnimationComponent.frameRangeMap.get("standing"), 0, Animation.PlayMode.LOOP);
    }
    private void jumpingState(){
        spriteAnimationStateComponent.set(spriteAnimationComponent.frameRangeMap.get("jumping"), 0, Animation.PlayMode.LOOP);
    }
    private void jumpShootingState(){
        spriteAnimationStateComponent.set(spriteAnimationComponent.frameRangeMap.get("jumpshooting"), 13, Animation.PlayMode.LOOP);
    }


    private Vector2 originalPosition = new Vector2();
    private float timePassed;

    @Override
    public void act(float delta) {

        //transformComponent.x += speed.x * delta;
        if(originalPosition == null) {
            originalPosition.x = transformComponent.x;
            originalPosition.y = transformComponent.y;
        }

        float dist = transformComponent.x - player.getX();
        if(Math.abs(dist) <= 30) {
            if(Math.abs(dist) < 5) {
                //timePassed = 0;

            } else if(dist > 0) {
              transformComponent.x -= speed.x * delta;
            } else {
                transformComponent.x += speed.x * delta;
            }
            originalPosition.x = transformComponent.x;

            timePassed = 0;
        } else {
            //manage scaleX later.
            timePassed += delta;
            transformComponent.x = (originalPosition.x +
                    MathUtils.sin(timePassed * MathUtils.degreesToRadians * 20f) * 20f);
        }


        rayCast();
        checkForBodyCollision();

    }





    private void rayCast() {
        float rayGap = (dimensionsComponent.height) / 2;

        float raySize = -(speed.y+ Gdx.graphics.getDeltaTime())*Gdx.graphics.getDeltaTime();


        if(speed.y > 0) return;

        Vector2 rayFrom = new Vector2((transformComponent.x + (dimensionsComponent.width/2)) * PhysicsBodyLoader.getScale(),
                (transformComponent.y + rayGap) * PhysicsBodyLoader.getScale());

        Vector2 rayTo = new Vector2((transformComponent.x + dimensionsComponent.width/2) * PhysicsBodyLoader.getScale(),
                (transformComponent.y - raySize)* PhysicsBodyLoader.getScale());

        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                speed.y = 0;
                transformComponent.y = point.y / PhysicsBodyLoader.getScale() + 0.01f;
                grounded = true;
                return 0;
            }
        }, rayFrom, rayTo);
    }

    private void checkForBodyCollision(){
        float rayGap = (dimensionsComponent.width) / 2;
        float raySize = 2;

        if(speed.x > 0) return;

        Vector2 rayFrom = new Vector2((transformComponent.y + (dimensionsComponent.height/2)) * PhysicsBodyLoader.getScale(),
                (transformComponent.y + rayGap) * PhysicsBodyLoader.getScale());

        Vector2 rayTo = new Vector2((transformComponent.y + dimensionsComponent.height/2) * PhysicsBodyLoader.getScale(),
                (transformComponent.y - raySize)* PhysicsBodyLoader.getScale());



        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                speed.x = 0;
                transformComponent.x = point.x / PhysicsBodyLoader.getScale() + 0.01f;
                return 0;
            }
        }, rayFrom, rayTo);

    }

    public boolean landed(){
        return grounded;
    }

    public float getX() {
        return transformComponent.x;
    }
    public float getY() {
        return transformComponent.y;
    }
    public float getWidth() {
        return dimensionsComponent.width;
    }
    @Override
    public void dispose() {

    }
}