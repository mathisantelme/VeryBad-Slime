package GameState.LevelState;

import GameState.GameStateManager;

public class Level1 extends LevelState {

    private int[][] ennemiesPos = {
            {400, 300},
            {880, 234},
            {1230, 170}
    };
    private double[] playerPos = {50,300.};
    private int[] end = {36, 4};

    public Level1(GameStateManager p_GSM) {
        super(p_GSM);
        currentLevel = 1;
        init("/Maps/level1.map", "/Tilesets/grass_world.png", 32, "/Backgrounds/grassbg1.png", 0.1, ennemiesPos, playerPos, end);
    }
}