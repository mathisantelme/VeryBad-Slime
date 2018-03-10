package Entity;

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

    // attacks - slime
    private boolean firing;
    private int slimeAttackCost;
    private int slimeAttackDmg;
    //private ArrayList<SlimeBalls> slimeBalls;

    // attacks - slime
    private boolean scratching;
    private int scratchDmg;
    private int scratchRange;

    // sticking
    private boolean sticking;
    private long stickingTimer;

    // animations
    private ArrayList<BufferedImage[]> sprites;
    // array that stores the number of frames per animations
    private final int[] framesAmount = { 7, 7, 5, 1 };

    // animations actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int SLIMEATTACK = 4;
    private static final int SCRATCHING = 5;
    private static final int STICKING = 6;

    //====== Constructor ======//
    public Player (TileMap tm) {
        super(tm);

        width = 30;
        height = 30;
        cWidth = 15;
        cHeight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        slime = maxSlime = 2500;

        slimeAttackCost = 200;
        slimeAttackDmg = 5;
        // slimeBalls = new ArrayList<SlimeBall>();

        scratchDmg = 8;
        scratchRange = 40; // in pixels

        // loading sprites
        sprites = new ArrayList<BufferedImage[]>();
        try {
            BufferedImage spritesheet = ImageIO.read(
                getClass().getResourceAsStream("/Sprites/Player/perso_anim.gif")
            );

            for (int i = 0; i < 4; i++) {
                BufferedImage[] bi = new BufferedImage[framesAmount[i]];

                if (i != 6) {
                    for (int j = 0; j < framesAmount[i]; j++) {
                        bi[j] = spritesheet.getSubimage(
                                j * width,
                                i * height,
                                width,
                                height
                        );
                    }
                } else {
                    for (int j = 0; j < framesAmount[i]; j++) {
                        bi[j] = spritesheet.getSubimage(
                                j * width * 2,
                                i * height,
                                width * 2,
                                height
                        );
                    }
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

    public int getHealth () { return health; }
    public int getMaxHealth () { return maxHealth; }
    public int getSlime () { return slime; }
    public int getMaxSlime () { return maxSlime; }

    public void setFiring () { firing = true; }
    public void setScratching () { scratching = true; }
    public void setSticking (boolean sticking) { this.sticking = sticking; }

    // methods
    private void getNextPosition() {
        // movement
        if (left) { // deplacement a gauche
            dx -= moveSpeed;
            if (dx < -maxSpeed) dx = -maxSpeed;
        } else if (right) { // deplacement a droite
            dx += moveSpeed;
            if (dx > maxSpeed) dx = maxSpeed;
        } else { // arret du personnage
            if (dx > 0) {
                dx -= stopSpeed;
                if (dx < 0) dx = 0;
            } else if (dx < 0) {
                dx += stopSpeed;
                if (dx > 0) dx = 0;
            }
        }

        // cannot move while attacking except in air
        if ((currentAction == SCRATCHING || currentAction == SLIMEATTACK) && !(jumping || falling)) dx = 0;

        // jumping
        if (jumping && !falling) {
            sticking = false;
            dy = jumpStart;
            falling = true;
        }

        // falling
        if (falling) {
            dy += fallSpeed;

            if (dy > 0) jumping = false;
            if (dy < 0 && !jumping) dy += stopJumpSpeed;

            if (bottomLeft && topLeft) sticking = true;
            if (dy > maxFallSpeed) dy = maxFallSpeed;
        }

        // sticking
        if (sticking) {
            dy += fallSpeed * 0.1;
        }
    }

    public void update () {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xTemp, yTemp);

        // check attack has played once
        if (currentAction == JUMPING) {
            if (animation.hasPlayedOnce()) jumping = false;
        }

        // set animations
        if (scratching) { // gestion de l'attaque corps a corps
            if (currentAction != SCRATCHING) {
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 60;
            }
        } else if (firing) { // gestion de l'attaque a distance
            if (currentAction != SLIMEATTACK) {
                currentAction = SLIMEATTACK;
                animation.setFrames(sprites.get(SLIMEATTACK));
                animation.setDelay(100);
                width = 30;
            }
        } else if (dy > 0) { // gestion de la chute
            if (currentAction != FALLING) {
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(-1);
                width = 30;
            }
        } else if (dy < 0) { // gestion du saut
            if (currentAction != JUMPING) {
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(50); // si il n'posY a pas besoin d'animation mettre le delay sur -1 (cf Entity.Animation)
                width = 30;
            }
        } else if (left || right) {
            if (currentAction != WALKING) {
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        } else {
            if (currentAction != IDLE) {
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(50);
                width = 30;
            }
        }

        animation.update();

        // set the direction
        if (currentAction != SCRATCHING && currentAction != SLIMEATTACK) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }
    }

    public void draw (Graphics2D g) {
        setMapPosition(); // always call this method in any MapObject descendent's draw function

        // draw player
        if (flinching) { // permet de faire clignoter le personnage si il prends des degats
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if (elapsed / 100 % 2 == 0) return;
        }

        if (facingRight) { // si le personnage regarde a droite alors on dessine l'animation dans le bon sens
            g.drawImage(
                    animation.getImage(),
                    (int) (posX + xMap - width / 2),
                    (int) (posY + yMap - height / 2),
                    null
            );
        } else { // sinon on la retourne
            g.drawImage(
                    animation.getImage(),
                    (int) (posX + xMap - width / 2 + width),
                    (int) (posY + yMap - height / 2),
                    -width,
                    height,
                    null
            );
        }
    }
}
