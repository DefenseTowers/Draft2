package ntnu.codt.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class AttackComponent implements Component {
    public Circle attackRadius = new Circle();
    public int attackDamage = 0;
    public Array<Entity> creepsInRange = new Array<Entity>();
    public long lastShot;
    public float targetDistanceX;
    public float targetDistanceY;
}
