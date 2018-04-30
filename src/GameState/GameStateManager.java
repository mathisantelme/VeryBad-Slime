package GameState;

import GameState.Editor.Editor;
import GameState.LevelState.Level1State;

import java.util.ArrayList;

public class GameStateManager {
    // attributs du game state manager
    private int GAME_STATE_NBR = 5;
    private GameState[] gameStates;
    private int currentState;

    public static final int MENU = 0;
    public static final int EDITOR = 1;
    public static final int LEVEL_01 = 2;
    public static final int DEATHSCREEN = 3;

    // les differents levels present dans le jeu
    public static ArrayList<GameState> levels = new ArrayList<GameState>();

    public GameStateManager () {
        gameStates = new GameState[GAME_STATE_NBR];
        currentState = MENU;
        loadState(currentState);
    }

    public void loadState (int state) {
        switch (state) {
            case MENU:
                gameStates[state] = new MenuState(this);
                break;
            case LEVEL_01:
                gameStates[state] = new Level1State(this);
                break;
            case EDITOR:
                gameStates[state] = new Editor(this);
                break;
            case DEATHSCREEN:
                gameStates[state] = new DeathScreen(this);
                break;
        }
    }

    public void unloadState (int state) {
        gameStates[state] = null;
    }

    public void setState (int state) {
        unloadState(state);
        currentState = state;
        loadState(currentState);
    }

    public void update () {
        try {
            gameStates[currentState].update();
        } catch(Exception e) {}
    }

    public void draw (java.awt.Graphics2D g) {
        try {
            gameStates[currentState].draw(g);
        } catch (Exception e) {}
    }

    public void keyPressed (int k) {
        gameStates[currentState].keyPressed(k);
    }

    public void keyReleased (int k) {
        gameStates[currentState].keyReleased(k);
    }
}
