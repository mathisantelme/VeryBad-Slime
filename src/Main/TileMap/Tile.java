package Main.TileMap;

import java.awt.image.BufferedImage;

/**
 * Tile
 */
public class Tile {
    private BufferedImage image;
    private int type;

    // tile type
    public static final int NORMAL = 0, BLOCKED = 1;

    // constructor
    public Tile(BufferedImage p_image, int p_type) {
        this.image = p_image;
        this.type = p_type;
    }

    public BufferedImage getImage () { return image; }
    public int getType () { return type; }
}
