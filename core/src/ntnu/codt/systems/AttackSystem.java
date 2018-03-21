package ntnu.codt.systems;

/**
 * Created by JMS on 20/03/2018.
 */

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Array;

import ntnu.codt.components.AttackComponent;
import ntnu.codt.components.PositionComponent;
import ntnu.codt.components.TextureComponent;
import ntnu.codt.components.TransformComponent;
import ntnu.codt.components.VelocityComponent;


public class AttackSystem extends IteratingSystem {
    private ComponentMapper<AttackComponent> am;
    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<TransformComponent> trm;
    private ComponentMapper<TextureComponent> tem;
    private ComponentMapper<AttackComponent> ac;
    private Array<Entity> queue;

    public AttackSystem() {
        super(Family.all(AttackComponent.class, PositionComponent.class, TransformComponent.class, AttackComponent.class).get());

        trm = ComponentMapper.getFor(TransformComponent.class);
        am = ComponentMapper.getFor(AttackComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);
        ac = ComponentMapper.getFor(AttackComponent.class);
        queue = new Array<Entity>();

    }
/*TEST*/



    public void update(float deltaTime){
        super.update(deltaTime);


        for (Entity entity : queue) {
            PositionComponent pc = pm.get(entity);
            VelocityComponent vc = entity.getComponent(VelocityComponent.class);
            PositionComponent pm = entity.getComponent(PositionComponent.class);

            //System.out.println(pc.pos.x + " " + pc.pos.y);
            //System.out.println(layer.getCell((int)Math.floor(pc.pos.x/tileWidth), (int)Math.floor(pc.pos.y/tileHeight)));
            System.out.println("tower is active");

        }


        queue.clear();
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }
}
