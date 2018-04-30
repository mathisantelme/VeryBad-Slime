package GameState.LevelState;

import GameState.GameStateManager;

public class Level3 extends LevelState {
    private int[][] ennemiesPos = {
            {750, 400}
    };
    private double[] playerPos = {100.,100.};
    private int[] end = {39, 9};

    public Level3 (GameStateManager p_GSM) {
        super(p_GSM);
        init("/Maps/level3.map", "/Tilesets/grass_world.png", 32, "/Backgrounds/grassbg1.png", 0.1, ennemiesPos, playerPos, end);
    }
}
