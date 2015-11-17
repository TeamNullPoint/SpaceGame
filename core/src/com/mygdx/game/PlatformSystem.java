package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.components.PolygonComponent;
import com.uwsoft.editor.renderer.components.TextureRegionComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Manages all the platforms. Is only called for entities that have this particular component.
 *
 */
public class PlatformSystem extends IteratingSystem {
    public PlatformSystem(){
        super(Family.all(PlatformComponent.class).get());
    }
    //We must create a component mapper for this system that maps all components of a type to it for
    //management.
    private ComponentMapper<PlatformComponent> collisionSystemComponentMapper = ComponentMapper.getFor(PlatformComponent.class);

    public PhysicsBodyComponent physicsBodyComponent;

    //Retrieves all platform components that match a type for management by the system.

    //Iterates through entities managed by this system, and processes them in some way.
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlatformComponent platformComponent = collisionSystemComponentMapper.get(entity);
        //Retrieve and entity's transform component.
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);

        if(platformComponent.originalPosition == null){
            platformComponent.originalPosition = new Vector2(transformComponent.x, transformComponent.y);
            platformComponent.timePassed = MathUtils.random(0, 2000);
        }

        platformComponent.timePassed += deltaTime;
        Vector2 newPosition = new Vector2();

        newPosition.x = physicsBodyComponent.body.getPosition().x;
        newPosition.y = (platformComponent.originalPosition.y +
                MathUtils.cos(platformComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f)
                * PhysicsBodyLoader.getScale();

        physicsBodyComponent.body.setTransform(newPosition, physicsBodyComponent.body.getAngle());


    }

    public boolean bodyIsPlatform(Body body) {
        System.out.println("body is " + physicsBodyComponent.body + "from Platform system.");
        return physicsBodyComponent.body == body;
    }
}
