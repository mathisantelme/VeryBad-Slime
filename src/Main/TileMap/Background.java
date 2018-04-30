package Main.TileMap;

import Main.Game;
import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Background {

    private BufferedImage image;

    private double posX, posY;
    private double dx, dy;

    private double moveScale;

    public Background (String path, double p_moveScale) throws java.lang.IllegalArgumentException {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));
            moveScale = p_moveScale;
        } catch (Exception e) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public void setPosition (double x, double y) {
        this.posX = (x * moveScale) % GamePanel.WIDTH;
        this.posY = (y * moveScale) % GamePanel.HEIGHT;
    }

    public void setVector (double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update () {
        this.posX += this.dx;
        this.posY += this.dy;
    }

    public void draw (Graphics2D g) {
        g.drawImage(image, (int) posX, (int) posY, null);
        if (posX < 0) g.drawImage(image, (int) posX + GamePanel.WIDTH, (int) posY, null);
        if (posX > 0) g.drawImage(image, (int) posX - GamePanel.WIDTH, (int) posY, null);
    }
}
