package com.mygdx.game;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.uwsoft.editor.renderer.components.DimensionsComponent;
import com.uwsoft.editor.renderer.components.TransformComponent;
import com.uwsoft.editor.renderer.components.physics.PhysicsBodyComponent;
import com.uwsoft.editor.renderer.physics.PhysicsBodyLoader;
import com.uwsoft.editor.renderer.utils.ComponentRetriever;
import com.badlogic.gdx.physics.box2d.World;



public class EnemySystem extends IteratingSystem { // this class is in charge of Artificial intelligence of Enemy.
    private Player player;
    private World world;
    //We must create a component mapper for this system that maps all components of a type to it for
    //management.
    private ComponentMapper<EnemyComponent> enemyComponentMapper = ComponentMapper.getFor(EnemyComponent.class);
    private final Vector2 speed = new Vector2(22, 0);

    public EnemySystem(Player player, World world) {
        super(Family.all(EnemyComponent.class).get());
        this.player = player;
        this.world = world;
    }

    //Retrieves all platform components that match a type for management by the system.

    //Iterates through entities managed by this system, and processes them in some way.
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);
        EnemyComponent enemyComponent = enemyComponentMapper.get(entity);

        Vector2 currentPlayerPosition = new Vector2(player.getX(), player.getY());

        float dist = transformComponent.x - currentPlayerPosition.x;

        //if(Math.abs(dist) >= 40){
            //moveUnaware(entity, deltaTime);

//        }else{
            moveAware(entity, deltaTime, dimensionsComponent, transformComponent, new Vector2(33, 0));
//        }

    }

    public void moveAware(Entity entity, float delta, DimensionsComponent dimensionsComponent, TransformComponent transformComponent, Vector2 speed) {
        //keep moving the enemies left.
        transformComponent.x -= speed.x * delta;
        transformComponent.scaleX = -1f;

        //Adjust the (y) position for gravity
        //Do ray cast and check for collisions
        speed.y += NullConstants.WORLD_GRAVITY * delta;
        transformComponent.y += speed.y * delta;
        rayCast(dimensionsComponent, transformComponent);
        checkForBodyCollision(dimensionsComponent, transformComponent, speed);

        EnemyComponent enemyComponent = enemyComponentMapper.get(entity);
        enemyComponent.originalPosition = null;
    }

    private void rayCast(final DimensionsComponent dimensionsComponent, final TransformComponent transformComponent) {
        float rayGap = (dimensionsComponent.height) / 2;

        float raySize = -(speed.y + Gdx.graphics.getDeltaTime()) * Gdx.graphics.getDeltaTime();


        if (speed.y > 0) return;

        Vector2 rayFrom = new Vector2((transformComponent.x + (dimensionsComponent.width / 2)) * PhysicsBodyLoader.getScale(),
                (transformComponent.y + rayGap) * PhysicsBodyLoader.getScale());

        Vector2 rayTo = new Vector2((transformComponent.x + dimensionsComponent.width / 2) * PhysicsBodyLoader.getScale(),
                (transformComponent.y - raySize) * PhysicsBodyLoader.getScale());

        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                speed.y = 0;
                transformComponent.y = (point.y / PhysicsBodyLoader.getScale()) + 0.01f;
                return 0;
            }
        }, rayFrom, rayTo);
    }

    private void checkForBodyCollision(final DimensionsComponent dimensionsComponent, final TransformComponent transformComponent, final Vector2 speed) {
        float rayGap = (dimensionsComponent.width) / 2;
        float raySize = 2;

        if (speed.x > 0) return;

        Vector2 rayFrom = new Vector2((transformComponent.y + (dimensionsComponent.height / 2)) * PhysicsBodyLoader.getScale(),
                (transformComponent.y + rayGap) * PhysicsBodyLoader.getScale());

        Vector2 rayTo = new Vector2((transformComponent.y + dimensionsComponent.height / 2) * PhysicsBodyLoader.getScale(),
                (transformComponent.y - raySize) * PhysicsBodyLoader.getScale());


        world.rayCast(new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                speed.x = 0;
                transformComponent.x = point.x / PhysicsBodyLoader.getScale() + 0.01f;
                return 0;
            }
        }, rayFrom, rayTo);

    }

    protected void moveUnaware(Entity entity, float deltaTime) {
        EnemyComponent enemyComponent = enemyComponentMapper.get(entity);
        //Retrieve and entity's transform component.
        TransformComponent transformComponent = ComponentRetriever.get(entity, TransformComponent.class);
        DimensionsComponent dimensionsComponent = ComponentRetriever.get(entity, DimensionsComponent.class);

        if(enemyComponent.originalPosition == null){
            enemyComponent.originalPosition = new Vector2(transformComponent.x, transformComponent.y);
            enemyComponent.timePassed = MathUtils.random(0, 100);
        }

        enemyComponent.timePassed += deltaTime;
        Vector2 newPosition = new Vector2();

        System.out.println("enemyComponent.originalPosition.x : " + enemyComponent.originalPosition.x);
        System.out.println("MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f) : " + MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f);
        System.out.println("PhysicsBodyLoader.getScale()) : " + PhysicsBodyLoader.getScale());
        newPosition.x = (enemyComponent.originalPosition.x +
                MathUtils.cos(enemyComponent.timePassed * MathUtils.degreesToRadians * 20f) * 20f)
                * PhysicsBodyLoader.getScale();

        transformComponent.x = newPosition.x;

    }
}
