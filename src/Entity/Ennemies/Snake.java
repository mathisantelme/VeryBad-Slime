package Entity.Ennemies;

import Entity.Animation;
import Main.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Snake extends Ennemy {

    private BufferedImage[] sprites;

    public Snake(TileMap tm) {
        super(tm);

        moveSpeed = 0.5;
        maxSpeed = 0.5;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = height = 32;
        cWidth = 20;
        cHeight = 15;

        health = maxHealth = 2;
        damage = 1;

        // load sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/Sprites/Enemies/sprite_enemi.png"
                    )
            );

            sprites = new BufferedImage[3];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                        i * width,
                        0,
                        width,
                        height
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // animations
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(100);

        right = true;
        facingRight = true;
    }

    private void getNextPosition () {
        // deplacement a gauche
        if (left) {
            dx -= moveSpeed;
            if (dx < -maxSpeed) dx = -maxSpeed;
        } // deplacement a droite
        else if (right) {
            dx += moveSpeed;
            if (dx > maxSpeed) dx = maxSpeed;
        }

        if (falling) dy += fallSpeed;

    }

    public void update () {
        if (!notOnScreen()) {
            // updating position
            getNextPosition();
            checkTileMapCollision();
            setPosition(xTemp, yTemp);

            // check fliching
            if (flinching) {
                long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
                if (elapsed > 400) {
                    flinching = false;
                }
            }

            // wall check
            if (right && dx == 0) {
                right = false;
                left = true;
                facingRight = false;
            } else if (left && dx == 0) {
                right = true;
                left = false;
                facingRight = true;
            }

            // update animation
            animation.update();
        }
    }

    public void draw (Graphics2D g) {
        // if (notOnScreen()) return;
        setMapPosition();
        super.draw(g);
    }

}
