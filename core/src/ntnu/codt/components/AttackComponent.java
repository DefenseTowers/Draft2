package ntnu.codt.components;


import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Circle;

public class AttackComponent implements Component {
    Circle attackRadius = new Circle();
    int attackDamage = 0;
}
