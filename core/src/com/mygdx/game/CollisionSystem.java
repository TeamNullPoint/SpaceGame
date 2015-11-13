package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 */
public class CollisionSystem extends IteratingSystem {
        private ComponentMapper<CollisionComponent> collisionComponentComponentMapper = ComponentMapper.getFor(CollisionComponent.class);

        private Player player;

        //Retrieves all platform components that match a type for management by the system.
        public CollisionSystem(Player player) {

            super(Family.all(CollisionComponent.class).get());
            this.player = player;
        }


        private CollisionComponent collisionComponent;

        //Iterates through entities managed by this system, and processes them in some way.
        @Override
        protected void processEntity(Entity entity, float deltaTime) {


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
                System.out.println("collision! ! !  ! !  ! ! ! ! ! !  ");

            }


        }
}
