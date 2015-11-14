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

    public EnemySystem(Player player){
        super(Family.all(EnemyComponent.class).get());
        this.player = player;
    }
    //We must create a component mapper for this system that maps all components of a type to it for
    //management.
    private ComponentMapper<EnemyComponent> enemyComponentMapper = ComponentMapper.getFor(EnemyComponent.class);

    public PhysicsBodyComponent physicsBodyComponent;

    //Retrieves all platform components that match a type for management by the system.

    //Iterates through entities managed by this system, and processes them in some way.
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemyComponent = enemyComponentMapper.get(entity);
        //Retrieve and entity's transform component.
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);

        PhysicsBodyComponent physicsBodyComponent = ComponentRetriever.get(entity, PhysicsBodyComponent.class);

        if(enemyComponent.originalPosition == null){
            enemyComponent.originalPosition = new Vector2(transformComponent.x, transformComponent.y);
            enemyComponent.timePassed = MathUtils.random(0, 100);
        }

        enemyComponent.timePassed += deltaTime;
        Vector2 newPosition = new Vector2();

        Vector2 currentPlayerPosition = new Vector2(player.getX(), player.getY());


         // player is not close to the enemy.

        newPosition.x = (enemyComponent.originalPosition.x +
                    MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f)
                    * PhysicsBodyLoader.getScale();
        newPosition.y = physicsBodyComponent.body.getPosition().y;
        System.out.println("current player x,y = " +currentPlayerPosition.x+ "," +currentPlayerPosition.y);
        System.out.println("enemy x,y = " +newPosition.x+ "," +newPosition.y);
        System.out.println(currentPlayerPosition.dst(newPosition));
            if(currentPlayerPosition.dst(newPosition) <= 20f) {
                System.out.println("less than 20f");
                System.out.println(differenceBtw(currentPlayerPosition.x, newPosition.x));
                if(differenceBtw(currentPlayerPosition.x, newPosition.x) == -1) {
                    newPosition.x = Math.abs(newPosition.x) * -1;
                } else if(differenceBtw(currentPlayerPosition.x, newPosition.x) == 1) {
                    newPosition.x = Math.abs(newPosition.x);
                }
            }




        physicsBodyComponent.body.setTransform(newPosition, physicsBodyComponent.body.getAngle());


    }

    private int differenceBtw(float a, float b) {
        int direction = 1;
        if(a == b) {
            direction = 0;
        } else if(a < b) {
            direction = -1;
        }

        return direction;
    }

}
