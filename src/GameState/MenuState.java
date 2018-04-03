package GameState;

import Main.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private Background bg;
    private int currentChoice = 0;
    private String[] options = {
        "Start",
        "Help",
        "Quit"
    };

    private Color titleColor;
    private Font titleFont;
    private Font font;

    public MenuState (GameStateManager p_GSM) {
        this.GSM = p_GSM;

        try {
            bg = new Background("/Backgrounds/menubg.gif", 1);
            bg.setVector( -0.1, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font("Century Gothic", Font.PLAIN, 28);
            font = new Font("Arial", Font.PLAIN, 12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // methods
    private void select () {
        switch (currentChoice) {
            case 0:
                System.out.println("Starting the game !");
                GSM.setState(GameStateManager.LEVEL_01);
                break;
            case 1:
                System.out.println("Help page");
                break;
            case 2:
                System.exit(0);
                break;
        }
    }

    // overriding
    @Override
    public void init () {}

    @Override
    public void update () { bg.update(); }

    @Override
    public void draw (java.awt.Graphics2D g) {
        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("VeryBad Slime", 60, 70); // faire une fonction qui centre une chaine de caractere d'une taille donnée

        // draw options
        g.setFont(font);

        for (int i = 0; i < options.length; i++) {
            if (i == currentChoice) g.setColor(Color.BLACK);
            else g.setColor(Color.RED);

            g.drawString(options[i], 145, 140 + i * 15);
        }
    }

    @Override
    public void keyPressed(int k) {
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
    public void keyReleased(int k) {

    }
}
