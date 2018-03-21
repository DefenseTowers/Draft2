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
    private ComponentMapper<VelocityComponent> vm;
    private Array<Entity> queue;

    public AttackSystem() {
        super(Family.all(AttackComponent.class, PositionComponent.class, VelocityComponent.class).get());

        trm = ComponentMapper.getFor(TransformComponent.class);
        am = ComponentMapper.getFor(AttackComponent.class);
        pm = ComponentMapper.getFor(PositionComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);
        queue = new Array<Entity>();

    }
/*TEST*/



    public void update(float deltaTime){
        super.update(deltaTime);


        for (Entity entity : queue) {
            PositionComponent pc = pm.get(entity);
            VelocityComponent vc = vm.get(entity);
            AttackComponent pm = am.get(entity);
            //System.out.println(pc.pos.x + " " + pc.pos.y);
            //System.out.println(layer.getCell((int)Math.floor(pc.pos.x/tileWidth), (int)Math.floor(pc.pos.y/tileHeight)));
         if(pc.pos.x < 1280 && pc.pos.x > 0 && pc.pos.y < 720 && pc.pos.y > 0) {
             pc.pos.x += vc.velocity.x * deltaTime;
             pc.pos.y += vc.velocity.y * deltaTime;
         }
         else {

             System.out.println("removing");
             entity.removeAll();

         }
        }


        queue.clear();
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        queue.add(entity);
    }
}
