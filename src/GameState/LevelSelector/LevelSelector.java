package GameState.LevelSelector;

import GameState.GameState;
import GameState.GameStateManager;
import GameState.LevelState.LevelState;

import java.awt.*;
import java.util.ArrayList;

public class LevelSelector extends GameState {

    private ArrayList<LevelState> levels;

    public LevelSelector (GameStateManager gsm) {
        this.GSM = gsm;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public void keyPressed (int k) {}
    @Override
    public void keyReleased (int k) {}
}
