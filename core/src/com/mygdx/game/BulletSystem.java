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
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Jaden on 15/11/2015.
 */
public class BulletSystem extends EntitySystem {

    private ImmutableArray<Entity> entities;
    private ImmutableArray<Entity> collisionEntities;

    private Queue<Entity> inAirBullets;
    private Queue<Entity> inCartridgeBullets;

    private Player player;
    Engine engine;
    public BulletSystem(Engine engine, Player player) {
        inAirBullets = new LinkedList<Entity>();
        inCartridgeBullets = new LinkedList<Entity>();
        entities = engine.getEntitiesFor(Family.all(BulletComponent.class).get());
        collisionEntities = engine.getEntitiesFor(Family.all(CollisionComponent.class).get());
        this.player = player;
        this.engine = engine;
        for(Entity bullet : entities){
            inCartridgeBullets.add(bullet);
        }
    }

    private ComponentMapper<BulletComponent> bm = ComponentMapper.getFor(BulletComponent.class);

    public void addToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(BulletComponent.class).get());

    }

    @Override
    public void update(float deltaTime) {
        if(!inAirBullets.isEmpty()){
            updateInAirBullets(deltaTime);
        } else if(player.getShoot() && canPullTrigger() && inCartridgeBullets.size() != 0){
            System.out.println("shooting bullet");
            createAndShoot();
        }
    }



    public boolean canPullTrigger(){
        Entity lastBullet = inCartridgeBullets.peek();
        System.out.println(inCartridgeBullets.size());
        for(Component component : lastBullet.getComponents()){
            System.out.println(component.getClass().getSimpleName());
        }
        BulletComponent bulletComponent = lastBullet.getComponent(BulletComponent.class);
        TransformComponent transformComponent = ComponentRetriever.get(lastBullet, TransformComponent.class);
        System.out.println(bulletComponent);
        System.out.println(bulletComponent.originalPosition);
        if (bulletComponent.originalPosition != null && Math.abs(transformComponent.x - bulletComponent.originalPosition.x) > 10) {
            return false;
        } else {
            return true;
        }
    }


    public void updateInAirBullets(float deltaTime){
        for(Entity entity : inAirBullets){
            BulletComponent bulletComponent = bm.get(entity);
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
            //If player wants to shoot and there is no bullet then make one and move it
            //slightly.
            if(bulletComponent.originalPosition != null) {
                //Update in air bullet
                moveBullet(entity, deltaTime, bulletComponent, transformComponent, player.facingDirection());

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
                            transformComponent.x = bulletComponent.originalPosition.x;
                            transformComponent.y = bulletComponent.originalPosition.y;
                            bulletComponent.originalPosition = null;

                            inCartridgeBullets.add(entity);
                            inAirBullets.remove(entity);
                        }
                    } else {
                        break enemyCheckLoop;
                    }
                }
            } else {
                throw new AssertionError("Dead bullet in live bullet array!");
            }
        }
    }

    public void updateCartridgeBullets(float deltaTime){

    }

    /**
     * Creates, shoots, and adds the bullet to the in air bullet queue..
     */
    public void createAndShoot(){
        Entity bullet = inCartridgeBullets.poll();
        BulletComponent bulletComponent = bm.get(bullet);
        TransformComponent transformComponent = ComponentRetriever.get(bullet, TransformComponent.class);
        //Create bullet.
        if(bulletComponent.originalPosition == null) {
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

            inAirBullets.add(bullet);
        }  else {
            //throw new AssertionError("Bullet still in air");
        }
    }


    private void moveBullet(Entity bullet, float deltaTime, BulletComponent bulletComponent, TransformComponent bulletTransformComponent, BulletComponent.PLAYER_DIRECTION playerDirection){
        float dist = Math.abs(bulletTransformComponent.x - player.getX());
        float incrementAmount = bulletComponent.currentDirection == -1 ? -1 * (70 * deltaTime) : 70 * deltaTime;

        if (dist < 70) {
            bulletTransformComponent.x += incrementAmount;
        } else {
            //Destroy the bullet.
            bulletTransformComponent.x = bulletComponent.originalPosition.x;
            bulletTransformComponent.y = bulletComponent.originalPosition.y;
            bulletComponent.originalPosition = null;
            inAirBullets.remove(bullet);
            inCartridgeBullets.add(bullet);
        }
    }
}