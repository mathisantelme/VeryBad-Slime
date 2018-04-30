package Entity;

import Main.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SlimeBall extends MapObject {

    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites;
    private BufferedImage[] hitSprites;
    private int spritesLength;
    private int hitspriteLength;

    public SlimeBall(TileMap tm, boolean right) {
        super(tm);

        moveSpeed = 3.8;
        if (right) dx = moveSpeed;
        else dx = -moveSpeed;

        width = height = 32;
        cWidth = cHeight = 32;
        spritesLength = 4;
        hitspriteLength = 3;

        // load the sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                getClass().getResourceAsStream(
                    "/Sprites/Player/projectile.png"
                )
            );

            sprites = new BufferedImage[spritesLength];
            for (int i = 0; i < sprites.length; i++) {
                sprites[i] = spritesheet.getSubimage(
                    i * width,
                    0,
                    width,
                    height
                );
            }

            hitSprites = new BufferedImage[hitspriteLength];
            for (int i = 0; i < hitSprites.length; i++) {
                hitSprites[i] = spritesheet.getSubimage(
                    i * width,
                    height,
                    width,
                    height
                );
            }

            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHit () {
        if (hit) return;
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(70);
        dx = 0;
    }

    public boolean shouldRemove () { return remove; }

    public void update () {
        checkTileMapCollision();
        setPosition(xTemp, yTemp);

        if (dx == 0 && !hit) setHit();

        animation.update();
        if (hit && animation.hasPlayedOnce()) remove = true;
    }

    public void draw (Graphics2D g) {

        setMapPosition();

        super.draw(g);

    }
}
