package GameState;

import Main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DeathScreen extends GameState {
    private int currentChoice = 0;
    private int pos = 10;
    private String[] options = {
        "Restart",
        "Quit"
    };

    public DeathScreen (GameStateManager p_GSM) {
        this.GSM = p_GSM;
        init();
    }

    public void select () {
        switch (currentChoice) {
            case 0:
                System.out.print("Restarting the game !");
                GSM.setState(GameStateManager.LEVEL_01);
                break;
            case 1:
                System.exit(0);
                break;
        }
    }

    @Override
    public void init () {

    }

    @Override
    public void update () {}

    @Override
    public void draw (Graphics2D g) {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 640, 640);

        g.setColor(Color.WHITE);
        g.drawString("You're dead !", 10, pos);

        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) g.setColor(Color.BLACK);
            else g.setColor(Color.RED);

            g.drawString(options[i], 145, 140 + i * 15);
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
