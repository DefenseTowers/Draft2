package ntnu.codt.entities;


import com.badlogic.gdx.math.Vector3;

public enum Player {

  P1(2338, 20 * 30, 0),
  P2(2337, 20 * 20, 719);

  public final int towerTile;
  private final Vector3 startPos;

  Player(int towerTile, float x, float y) {
    this.towerTile = towerTile;
    this.startPos = new Vector3(x, y, 0);
  }

  public Vector3 getStartPos() {
    return startPos.cpy();
  }

}
