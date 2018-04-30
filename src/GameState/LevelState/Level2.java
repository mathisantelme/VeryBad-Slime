package GameState.LevelState;

import GameState.GameStateManager;

public class Level2 extends LevelState {
    private int[][] ennemiesPos = {
            {600, 440},
            {330, 360},
            {660, 310},
            {1170, 280}
    };
    private double[] playerPos = {50,300};
    private int[] end = {37, 8};

    public Level2 (GameStateManager p_GSM) {
        super(p_GSM);
        init("/Maps/level2.map", "/Tilesets/grass_world.png", 32, "/Backgrounds/grassbg1.png", 0.1, ennemiesPos, playerPos, end);
    }
}
