package GameState;

import java.util.ArrayList;

public class GameStateManager {
    public static final int MENU = 0;
    public static final int LEVEL_01 = 1;
    public static final int DEATHSCREEN = 2;

    private ArrayList<GameState> gameStates;
    private int currentState;

    public GameStateManager () {
        gameStates = new ArrayList<GameState>();
        currentState = MENU;
        gameStates.add(new MenuState(this));
        gameStates.add(new Level1State(this));
        gameStates.add(new DeathScreen(this));
    }

    // methods

    public void setState (int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }

    public void update () {
        gameStates.get(currentState).update();
    }

    public void draw (java.awt.Graphics2D g) {
        gameStates.get(currentState).draw(g);
    }

    public void keyPressed (int k) {
        gameStates.get(currentState).keyPressed(k);
    }

    public void keyReleased (int k) {
        gameStates.get(currentState).keyReleased(k);
    }
}
