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

    private float currentX;
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
            enemyComponent.currentPosition = new Vector2(enemyComponent.originalPosition);
            enemyComponent.timePassed = MathUtils.random(0, 100);

        }



//
       Vector2 currentPlayerPosition = new Vector2(player.getX(), player.getY());

        float dist = enemyComponent.currentPosition.x - currentPlayerPosition.x;
        //System.out.println("enemyposition = " + enemyComponent.currentPosition.x + "current player x =" + currentPlayerPosition.x);
        System.out.println(dist);
         // player is not close to the enemy.
        if(Math.abs(dist) >= 30) {


            enemyComponent.timePassed += deltaTime;

            enemyComponent.currentPosition.x = (enemyComponent.originalPosition.x +
                        MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f)
                        * PhysicsBodyLoader.getScale();

        //System.out.println(Math.abs(MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f));
        //System.out.println(enemyComponent.originalPosition.x);
            enemyComponent.currentPosition.y = physicsBodyComponent.body.getPosition().y;


        } else {

            enemyComponent.currentPosition.y = physicsBodyComponent.body.getPosition().y;
          //      System.out.println("less than 40f");
                if (dist < 0) {


//                    System.out.println("dist<0 player = " +currentPlayerPosition.x +"| enemy ="+enemyComponent.currentPosition.x);
            //        System.out.println("dist <0");

                } else {
                    //enemyComponent.currentPosition.x -= 33*deltaTime;
    //                System.out.println(enemyComponent.currentPosition.x);
   //                 System.out.println("dist>0 player = " + currentPlayerPosition.x + "| enemy =" + enemyComponent.currentPosition.x);
              //      System.out.println("dist >0");

                }


        }
        physicsBodyComponent.body.setTransform(enemyComponent.currentPosition, physicsBodyComponent.body.getAngle());
    }


}
