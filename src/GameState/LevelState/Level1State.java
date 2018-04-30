package GameState.LevelState;

import GameState.GameStateManager;

public class Level1State extends LevelState {

    private int[][] ennemiesPos = {{300, 100}};
    private double[] playerPos = {100.,100.};
    private double[] end = {};

    public Level1State(GameStateManager p_GSM) {
        super(p_GSM);
        init("/Maps/map1.map", "/Tilesets/grass_world.gif", 30, "/Backgrounds/grassbg1.gif", 0.1, ennemiesPos, playerPos, end);
    }
}
