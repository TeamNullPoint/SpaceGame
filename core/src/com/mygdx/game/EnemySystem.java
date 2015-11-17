package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;

/**
 * Created by Jaden on 13/11/2015.
 */
public class EnemySystem extends IteratingSystem { // this class is in charge of Artificial intelligence of Enemy.

    private Player player;
    private EnemyComponent enemyComponent;


    public EnemySystem(Player player){
        super(Family.all(EnemyComponent.class).get());
        this.player = player;
        speed = new Vector2(22,0);
    }
    //We must create a component mapper for this system that maps all components of a type to it for
    //management.
   // private ComponentMapper<EnemyComponent> enemyComponentMapper = ComponentMapper.getFor(EnemyComponent.class);
    private ComponentMapper<EnemyComponent> enemyComponentMapper = ComponentMapper.getFor(EnemyComponent.class);

    //Retrieves all platform components that match a type for management by the system.

    //Iterates through entities managed by this system, and processes them in some way.
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        enemyComponent = enemyComponentMapper.get(entity);
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);

        if(enemyComponent.originalPosition == null){
            enemyComponent.originalPosition = new Vector2(transformComponent.x, transformComponent.y);
            enemyComponent.timePassed = MathUtils.random(0, 2000);
        }

        enemyComponent.timePassed += deltaTime;
        Vector2 newPosition = new Vector2();

        newPosition.y = physicsBodyComponent.body.getPosition().y;
        newPosition.x = (enemyComponent.originalPosition.x +
                MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f)
                * PhysicsBodyLoader.getScale();
        if(Math.abs(MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f))
                > MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f)) {
            transformComponent.scaleX = -1f;
        } else {
            transformComponent.scaleX = 1f;
        }

        physicsBodyComponent.body.setTransform(newPosition, physicsBodyComponent.body.getAngle());


    }

    private static Vector2 speed;

    private void moveLeft(float delta) {

    }

}
