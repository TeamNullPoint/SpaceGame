package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Jaden on 07/11/2015.
 */
public class BulletSystem extends IteratingSystem {
    //We must create a component mapper for this system that maps all components of a type to it for
    //management.
    private ComponentMapper<Bullet> bulletSystemComponentMapper = ComponentMapper.getFor(Bullet.class);

    //Retrieves all platform components that match a type for management by the system.
    public BulletSystem(){
        super(Family.all(Bullet.class).get());
    }

    //Iterates through entities managed by this system, and processes them in some way.
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Bullet bullet = bulletSystemComponentMapper.get(entity);
        //Retrieve and entity's transform component.
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);

        if(bullet.originalPosition == null){
            bullet.originalPosition = new Vector2(transformComponent.x, transformComponent.y);
            bullet.timePassed = MathUtils.random(0, 100);
        }

        bullet.timePassed += deltaTime;
        Vector2 newPosition = new Vector2();


        newPosition.x = (bullet.originalPosition.x +
                MathUtils.cos(bullet.timePassed * MathUtils.degreesToRadians * 20f) * 20f)
                * PhysicsBodyLoader.getScale();
        newPosition.y = physicsBodyComponent.body.getPosition().y;
        System.out.println(newPosition.x + "," + newPosition.y);


        physicsBodyComponent.body.setTransform(newPosition, physicsBodyComponent.body.getAngle());


    }
}
