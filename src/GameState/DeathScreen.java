package GameState;

import Main.GamePanel;
import Main.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DeathScreen extends GameState {
    private Background bg;
    private int currentChoice = 0;
    private String death_str = "You're dead !";
    private String[] options = {
        "Restart",
        "Quit to Main menu",
        "Quit game"
    };

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public DeathScreen (GameStateManager p_GSM) {
        this.GSM = p_GSM;
        init();
    }

    public void select () {
        switch (currentChoice) {
            case 0:
                GSM.setState(GameStateManager.LEVEL_01);
                break;
            case 1:
                GSM.setState(GameStateManager.MENU);
                break;
            case 2:
                System.exit(0);
        }
    }

    @Override
    public void init () {
        try {
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector( -0.1, 0);

            titleColor = new Color(0, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Arial", Font.PLAIN, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update () { bg.update(); }

    @Override
    public void draw (Graphics2D g) {
        bg.draw(g);

        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString(death_str, (GamePanel.WIDTH - g.getFontMetrics().stringWidth(death_str)) / 2, 30);

        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) g.setColor(Color.DARK_GRAY);
            else g.setColor(Color.RED);

            g.drawString(options[i], (GamePanel.WIDTH - g.getFontMetrics().stringWidth(options[i])) / 2, 100 + i * 15);
        }
    }

    @Override
    public void keyPressed (int k) {
        switch (k) {
            case KeyEvent.VK_ENTER:
                select();
                break;
            case KeyEvent.VK_UP:
                if (currentChoice > 0) currentChoice--;
                break;
            case KeyEvent.VK_DOWN:
                if (currentChoice < options.length - 1) currentChoice++;
                break;
        }
    }

    @Override
    public void keyReleased (int k) {}
}
