package com.mygdx.game;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.Overlap2D;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by Jaden on 15/11/2015.
 */
public class BulletSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> collisionEntities;
    private Player player;
    Engine engine;
    public BulletSystem(Engine engine, Player player) {
        entities = engine.getEntitiesFor(Family.all(BulletComponent.class).get());
        collisionEntities = engine.getEntitiesFor(Family.all(CollisionComponent.class).get());
        this.player = player;
        this.engine = engine;
    }

    private ComponentMapper<BulletComponent> bm = ComponentMapper.getFor(BulletComponent.class);

    public void addToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BulletComponent.class).get());

    }

    @Override
    public void update(float deltaTime) {
        boolean isFresh = true;
        for(Entity bullet : entities){
            BulletComponent bulletComponent = bm.get(bullet);
            TransformComponent transformComponent = ComponentRetriever.get(bullet, TransformComponent.class);
            if(bulletComponent.originalPosition != null){
                float dist = Math.abs(transformComponent.x - bulletComponent.originalPosition.x);
                if(dist > 10){
                    isFresh = false;
                    break;
                }
            }
        }
        for(Entity entity : entities){
            BulletComponent bulletComponent = bm.get(entity);
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
            //If player wants to shoot and there is no bullet then make one and move it
            //slightly.
            if(bulletComponent.originalPosition == null && player.getShoot()) {
                bulletComponent.originalPosition = new Vector2(transformComponent.x, transformComponent.y);
                bulletComponent.currentDirection = transformComponent.scaleX;

                bulletComponent.isLive = true;
                bulletComponent.currentDirection = player.facingDirection().getDirection();

                if(bulletComponent.currentDirection == BulletComponent.PLAYER_DIRECTION.LEFT_DIRECTION.getDirection()){
                    transformComponent.x = player.getX() - player.getWidth() / 2;
                } else {
                    transformComponent.x = player.getX() + player.getWidth() / 2;

                }
                transformComponent.y = player.getY() + player.getHeight() / 2;

            } else if(bulletComponent.originalPosition != null) {

                moveBullet(deltaTime, bulletComponent, transformComponent, player.facingDirection());

                //Let's check if it has hit anyone
                enemyCheckLoop:
                for(Entity enemy : collisionEntities){
                    CollisionComponent enemyCollisionComponent = (CollisionComponent) enemy.getComponents().get(enemy.getComponents().size() - 1);
                    DimensionsComponent enemyDimensionsComponent = (DimensionsComponent) enemy.getComponents().get(0);

                    //If this bullet is in the air then check if this entity is colliding with it.
                    if(bulletComponent.originalPosition != null){
                        Vector2 currentBulletPosition = new Vector2(transformComponent.x, transformComponent.y);
                        if(currentBulletPosition.dst(enemyCollisionComponent.position) <= ((enemyDimensionsComponent.width / 2) + (dimensionsComponent.width / 2))){
                            engine.removeEntity(enemy);
                            System.out.println("auchhhh");
                            System.out.println(currentBulletPosition.x +","+ enemyCollisionComponent.position.x);
                            transformComponent.x = bulletComponent.originalPosition.x;
                            transformComponent.y = bulletComponent.originalPosition.y;
                            bulletComponent.originalPosition = null;
                        }
                    } else {
                        break enemyCheckLoop;
                    }
                }
            }
        }
    }


    public void updateInAirBullets(float deltaTime){

    }

    public void updateCartridgeBullets(float deltaTime){

    }


    private void moveBullet(float deltaTime, BulletComponent bulletComponent, TransformComponent bulletTransformComponent, BulletComponent.PLAYER_DIRECTION playerDirection){
        float dist = Math.abs(bulletTransformComponent.x - player.getX());
        float incrementAmount = bulletComponent.currentDirection == -1 ? -1 * (70 * deltaTime) : 70 * deltaTime;

        if (dist < 70) {
            bulletTransformComponent.x += incrementAmount;
        } else {
            //Destroy the bullet.
            bulletTransformComponent.x = bulletComponent.originalPosition.x;
            bulletTransformComponent.y = bulletComponent.originalPosition.y;
            bulletComponent.originalPosition = null;
        }
    }
}