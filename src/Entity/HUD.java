package Entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HUD {

    private Player player;
    private BufferedImage img;
    private Font font;

    public HUD (Player player) {
        this.player = player;

        try {

            img = ImageIO.read(
                    getClass().getResourceAsStream(
                            "/HUD/hud.gif"
                    )
            );
            font = new Font("Arial", Font.PLAIN, 14);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw (Graphics2D g) {
        g.drawImage(img, 0, 10, null);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(player.getHealth() + " / " + player.getMaxHealth(), 20, 25);
        g.drawString(player.getSlime() + " / " + player.getMaxSlime(), 20, 45);
    }

}
