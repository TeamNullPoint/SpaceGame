package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

import java.util.ArrayList;

/**
 */
public class CollisionSystem extends IteratingSystem {
    private ComponentMapper<CollisionComponent> collisionComponentComponentMapper = ComponentMapper.getFor(CollisionComponent.class);
    private ComponentMapper<BulletComponent> bulletComponentComponentMapper = ComponentMapper.getFor(BulletComponent.class);
    private ImmutableArray<Entity> bulletEntities;
    private Engine engine;
    private Player player;

    //Retrieves all platform components that match a type for management by the system.
    public CollisionSystem(Player player, Engine engine) {
        super(Family.all(CollisionComponent.class).get());
        this.player = player;
        this.engine = engine;
        this.bulletEntities = engine.getEntitiesFor(Family.all(BulletComponent.class).get());
    }

    private CollisionComponent collisionComponent;
    private BulletComponent bulletComponent;

    //Iterates through entities managed by this system, and processes them in some way.
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        boolean isBullet = bulletComponentComponentMapper.has(entity);
        if(!isBullet) {
            collisionComponent = collisionComponentComponentMapper.get(entity);
            //Retrieve and entity's  component.
            TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
            DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);


            if (collisionComponent.position == null) {
                collisionComponent.position = new Vector2(transformComponent.x, transformComponent.y);
                collisionComponent.width = dimensionsComponent.width;
                collisionComponent.height = dimensionsComponent.height; // initializing

            }

            collisionComponent.position.set(transformComponent.x, transformComponent.y);
            collisionComponent.width = dimensionsComponent.width;
            collisionComponent.height = dimensionsComponent.height; // initializing

            Vector2 currentPlayerPosition = new Vector2(player.getX(), player.getY());


            if (currentPlayerPosition.dst(collisionComponent.position) <= ((collisionComponent.width / 2) + (player.getWidth() / 2))) {
                System.out.println("**Collision Detected**");
            }
        }
    }
}
