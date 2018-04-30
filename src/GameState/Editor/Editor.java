package GameState.Editor;

import GameState.GameStateManager;
import GameState.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Editor extends GameState {
    public Editor (GameStateManager gsm) {
        this.GSM = gsm;
        init();
    }

    @Override
    public void init() {
        JFrame window = new JFrame("Tile Map Editor");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                GSM.setState(GameStateManager.MENU);
            }
        });
        window.setContentPane(new Panel());
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
    @Override
    public void update() {}
    @Override
    public void draw(Graphics2D g) {}
    @Override
    public void keyPressed(int k) {}
    @Override
    public void keyReleased(int k) {}
}
