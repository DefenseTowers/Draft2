package ntnu.codt.entities;

/**
 * Created by Gemcluster on 10/04/2018.
 */

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import ntnu.codt.components.*;
import ntnu.codt.core.prototype.Prototype;
import ntnu.codt.systems.CreepSystem;

public enum Creeps implements Prototype<Entity, Creeps.Pack> {

    SMALL_BOI("badlogic.jpg", 20, 20, 250, 20*30, 0),
    BIG_BOI("bigboi.png", 40, 40, 600, 20*30, 0);

    private final String texture;
    private final float width;
    private final float height;
    private final int hp;
    private final int startx;
    private final int starty;

    Creeps(String texture, float width, float height, int hp, int startx, int starty){
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.startx = startx;
        this.starty = starty;
    }

    @Override
    public Entity copy(Pack pack) {
        Entity entity = pack.engine.createEntity();

        TransformComponent trm = pack.engine.createComponent(TransformComponent.class);
        TextureComponent tem = pack.engine.createComponent(TextureComponent.class);
        PositionComponent pm = pack.engine.createComponent(PositionComponent.class);
        VelocityComponent vs = pack.engine.createComponent(VelocityComponent.class);
        HealthComponent hc = pack.engine.createComponent(HealthComponent.class);

        pm.pos = new Vector3(startx, starty, 0);
        tem.region = new TextureRegion(new Texture(Gdx.files.internal(this.texture)));
        hc.health = this.hp;
        vs.velocity = new Vector3(0, 10, 0);

        trm.rotation = 0.0f;
        trm.scale = new Vector2(1, 1);

        entity.add(trm);
        entity.add(tem);
        entity.add(pm);
        entity.add(vs);
        entity.add(hc);

        pack.engine.addEntity(entity);

        return entity;
    }

    public static class Pack {
        public final Vector3 pos;
        public final PooledEngine engine;

        public Pack(Vector3 pos, PooledEngine engine) {
            this.pos = pos;
            this.engine = engine;
        }

    }
}