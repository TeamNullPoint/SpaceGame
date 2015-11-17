package com.mygdx.game;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

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

    public void update(float deltaTime) {
        Entity entity = entities.get(0);
        BulletComponent bulletComponent = bm.get(entity);
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        //If player wants to shoot and there is no bullet then make one and move it
        //slightly.
        if(bulletComponent.originalPosition == null && player.getShoot()) {
            bulletComponent.originalPosition = new Vector2(transformComponent.x, transformComponent.y);
            bulletComponent.currentDirection = transformComponent.scaleX;

            bulletComponent.isLive = true;

            transformComponent.x = player.getX() + player.getWidth() / 2 + 10;
            transformComponent.y = player.getY();

            transformComponent.x += 70 * deltaTime;
        } else if(bulletComponent.originalPosition != null) {
            //The bullet is in the air if the original position is not null.
            //Check if it is not out of range, if it is
            //set it's position to null, if it is in range then
            //keep moving it.
            float dist = Math.abs(transformComponent.x - player.getX());
            if (dist < 200) {
                transformComponent.x += 70 * deltaTime;
            } else {
                //Destroy the bullet.
                transformComponent.x = bulletComponent.originalPosition.x;
                transformComponent.y = bulletComponent.originalPosition.y;
                bulletComponent.originalPosition = null;
            }


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