package com.mygdx.game;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
 * Initialization logic
 * Iteration logic and disposal logic.
 */
public class Player implements IScript {
    private boolean stopJump = false;
    private Entity player;
    private TransformComponent transformComponent;
    private DimensionsComponent dimensionsComponent;
    private SpriteAnimationComponent spriteAnimationComponent;
    private SpriteAnimationStateComponent spriteAnimationStateComponent;

    private World world;

    public Player(World world) {
        this.world = world;
    }


    private float gravity = -120f;
    private Vector2 speed;

    private final float jumpSpeed = 66f;

    @Override
    public void init(Entity entity) {
        player = entity;

        transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        spriteAnimationComponent = ComponentRetriever.get(entity, SpriteAnimationComponent.class);
        spriteAnimationStateComponent = ComponentRetriever.get(entity, SpriteAnimationStateComponent.class);

        ImmutableArray<Component> allComponents = entity.getComponents();
        for(Component component : allComponents){
            System.out.println(component.getClass().getSimpleName());
        }

        System.out.println(spriteAnimationComponent.currentAnimation);
        walkingState();
        for(TextureAtlas.AtlasRegion region : spriteAnimationStateComponent.allRegions){
            System.out.println(region.name);
        }

        System.out.println(spriteAnimationComponent.frameRangeMap);
        speed = new Vector2(33, 0);

    }

    private void walkingState(){
        //spriteAnimationStateComponent.set(spriteAnimationComponent.frameRangeMap.get("walking"), 13, Animation.PlayMode.LOOP);

    }
    private void standingState(){
        //spriteAnimationStateComponent.set(spriteAnimationComponent.frameRangeMap.get("standing"), 0, Animation.PlayMode.LOOP);
    }

    @Override
    public void act(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            transformComponent.x -= speed.x * delta;
            transformComponent.scaleX = -1f;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            transformComponent.x  += speed.x * delta;
            transformComponent.scaleX = 1f;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)|| Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            walkingState();

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            speed.y = jumpSpeed;
        }
        if(!(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))){
            standingState();
        }
        speed.y += gravity*delta;
        transformComponent.y += speed.y * delta;
        rayCast();
    }


    private void rayCast() {
        float rayGap = (dimensionsComponent.height) / 2;

        float raySize = -(speed.y+Gdx.graphics.getDeltaTime())*Gdx.graphics.getDeltaTime();

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
                return 0;
            }
        }, rayFrom, rayTo);
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