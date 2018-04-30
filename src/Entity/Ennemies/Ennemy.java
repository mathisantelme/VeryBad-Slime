package Entity.Ennemies;

import Entity.MapObject;
import Main.TileMap.TileMap;

public abstract class Ennemy extends MapObject {

    protected int health, maxHealth;
    protected boolean dead;
    protected int damage;

    protected boolean flinching;
    protected long flinchTimer;

    public Ennemy(TileMap tm) {
        super(tm);

    }

    public boolean isDead () { return dead || outOfTheWorld; }

    public int getDamage () { return damage; }

    public void hit (int dmg) {
        if (dead || flinching || outOfTheWorld) return;
        health -= dmg;
        if (health < 0) health = 0;
        if (health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }

    public abstract void update();
}
