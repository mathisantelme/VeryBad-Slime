package Entity;

import Entity.Ennemies.Ennemy;
import Main.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Player extends MapObject {
    // player
    private int health, maxHealth;
    private int slime, maxSlime;
    private boolean dead;
    private boolean flinching;
    private long flinchTimer;

    // sticking to wall
    private boolean sticking;

    // attacks - slime
    private boolean firing;
    private int slimeAttackCost;
    private int slimeAttackDmg;
    private ArrayList<SlimeBall> slimeBalls;

    // attacks - slime
    /*private boolean scratching;
    private int scratchDmg;
    private int scratchRange;*/

    // animations
    private ArrayList<BufferedImage[]> sprites;

    // array that stores the number of frames per animations
    private static final int nbrAnim = 5;
    private final int[] framesAmount = { 7, 7, 5, 1, 3};
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int SLIMEATTACK = 4; //temp value change it to 4
    // private static final int STICKING = 5;


    //====== Constructor ======//
    public Player (TileMap tm) {
        super(tm);

        width = 32;
        height = 32;
        cWidth = 15;
        cHeight = 25;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        slime = maxSlime = 25;

        slimeAttackCost = 2;
        slimeAttackDmg = 5;
        slimeBalls = new ArrayList<SlimeBall>();

        /*scratchDmg = 8;
        scratchRange = 40; // in pixels*/

        // loading sprites
        sprites = new ArrayList<BufferedImage[]>();
        try {
            BufferedImage spritesheet = ImageIO.read(
                getClass().getResourceAsStream("/Sprites/Player/perso_animm.png")
            );

            for (int i = 0; i < nbrAnim; i++) {
                BufferedImage[] bi = new BufferedImage[framesAmount[i]];
                for (int j = 0; j < framesAmount[i]; j++) {
                    bi[j] = spritesheet.getSubimage(
                            j * width,
                            i * height,
                            width,
                            height
                    );
                }
                sprites.add(bi);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // animations
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(50);
    }

    int getHealth() { return health; }
    int getMaxHealth() { return maxHealth; }
    // return if the player is dead or out of the world
    public boolean isDead () { return dead || outOfTheWorld; }
    int getSlime() { return slime; }
    int getMaxSlime() { return maxSlime; }
    boolean isSticking () { return sticking; }

    public void setFiring () { /*if (!falling)*/ firing = true; }

    // methods
    /**
     * gère les différents mouvements du joueur
     */
    private void getNextPosition () {
        // deplacement a gauche
        if (left) {
            sticking = (bottomLeft && topLeft);
            dx -= moveSpeed;
            if (dx < -maxSpeed) dx = -maxSpeed;
        } else
        // deplacement a droite
        if (right) {
            sticking = (bottomRight && topRight);
            dx += moveSpeed;
            if (dx > maxSpeed) dx = maxSpeed;
        } // arret du personnage
        else {
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) dx = 0;
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) dx = 0;
            }
        }

        // jumping
        if (jumping && !falling && !sticking) {
            dy = jumpStart;
            falling = true;
        }

        // falling
        if (falling) {
            /*if (sticking){
                System.out.println("sticking");
                dy =+ fallSpeed * 0.9;
            } else*/ dy += fallSpeed;

            if (dy > 0) {
                jumping = false;
                // si le joueur tombe il ne peut plus sauter
            }
            if (dy < 0 && !jumping) dy += stopJumpSpeed; // retour au sol si le joueur ne saute plus
            if (dy > maxFallSpeed) dy = maxFallSpeed; // on cap la vitesse de saut
        }

        // cannot move while attacking except in air
        if ((currentAction == SLIMEATTACK) && !(jumping || falling)) dx = 0;
    }

    /**
     * met a jour la position du joueur et met a jour les animations du joueur
     */
    public void update () {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xTemp, yTemp);

        // check attacK has sTopped
        if (currentAction == SLIMEATTACK) {
            if (animation.hasPlayedOnce()) firing = false;
        }

        // slimeballs ===================================================================
        slime += 1;
        if (slime > maxSlime) slime = maxSlime;

        if (firing && currentAction != SLIMEATTACK) {
            if (slime > slimeAttackCost) {
                slime -= slimeAttackCost;
                SlimeBall sb = new SlimeBall(tileMap, facingRight);
                sb.setPosition(posX, posY);
                slimeBalls.add(sb);
            }
        }

        for (int i = 0; i < slimeBalls.size(); i++) {
            slimeBalls.get(i).update();
            if (slimeBalls.get(i).shouldRemove()) {
                slimeBalls.remove(i);
                i--;
            }
        }

        // check done flinching ===================================================================
        if (flinching) {
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed > 1000) {
                flinching = false;
            }
        }

        // set animation ===================================================================
        if (firing) { // gestion de l'attaque a distance
            if (currentAction != SLIMEATTACK) {
                currentAction = SLIMEATTACK;
                animation.setFrames(sprites.get(SLIMEATTACK));
                animation.setDelay(30);
            }
        } else if (dy > 0) { // gestion de la chute
            if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(-1);
            }
        } else if (dy < 0) { // gestion du saut
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(50); // si il n'a pas besoin d'animation mettre le delay sur -1 (cf Entity.Animation)
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(50);
            }
        }

        animation.update();

        // set the direction
        if (currentAction != SLIMEATTACK) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }
    }

    /**
     * permet d'afficher le joueur a l'écran
     * @param g
     */
    public void draw (Graphics2D g) {
        setMapPosition(); // always call this method in any MapObject descendent's draw function

        // draw player
        if (flinching) { // permet de faire clignoter le personnage si il prends des degats
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) return;
        }

        // draw slimeball
        for (SlimeBall s: slimeBalls) {
            s.draw(g);
        }

        super.draw(g);
    }

    public void checkAttack (ArrayList<Ennemy> ennemies) {
        for (Ennemy e: ennemies) {
            for (SlimeBall s: slimeBalls) {
                if (s.intersects(e)) {
                    e.hit(slimeAttackDmg);
                    s.setHit();
                    break;
                }
            }

            // check ennemies collison
            if (intersects(e)) this.hit(e.getDamage());
        }
    }

    private void hit(int dmg) {
        if (flinching) return;
        health -= dmg;
        if (health < 0) health = 0;
        if (health == 0) dead = true;
        flinching = true;
        flinchTimer = System.nanoTime();
    }
}
