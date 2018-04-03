package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion {

    private int posX, posY;
    private int xMap, yMap;

    private int width, height;

    private Animation animation;
    private BufferedImage[] sprites;

    private boolean remove;

    public Explosion (int x, int y) {
        this.posX = x;
        this.posY = y;

        width = height = 30;
        int framesLength = 6;

        // load the sprites
        try {
            BufferedImage spritesheet = ImageIO.read(
                getClass().getResourceAsStream(
                    "/Sprites/Enemies/explosion.gif"
                )
            );

            sprites = new BufferedImage[framesLength];

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

        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(50);
    }

    public void update () {
        animation.update();
        if (animation.hasPlayedOnce()) remove = true;
    }

    public boolean shouldRemove () { return remove; }

    public void setMapPosition (int x, int y) {
        xMap = x;
        yMap = y;
    }

    public void draw (Graphics2D g) {
        g.drawImage(
            animation.getImage(),
            posX + xMap - width / 2,
            posY + yMap - height / 2,
            null
        );
    }

}
