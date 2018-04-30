package GameState.Editor;

import java.awt.*;
import java.awt.image.*;

public class Block {

    BufferedImage image;
    int x;
    int y;

    Block (BufferedImage b) {
        image = b;
    }

    void setPosition (int i1, int i2) {
        x = i1;
        y = i2;
    }

    BufferedImage getImage () {
        return image;
    }

    void draw (Graphics2D g) {
        g.drawImage(image, x, y, null);
    }
}
