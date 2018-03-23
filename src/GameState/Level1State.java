package GameState;

import Entity.Player;
import Main.GamePanel;
import Main.TileMap.Background;
import Main.TileMap.TileMap;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.System.exit;

public class Level1State extends GameState {
    private TileMap tileMap;
    private Background bg;

    private Player player;

    public Level1State (GameStateManager p_GSM) {
        this.GSM = p_GSM;
        init();
    }

    @Override
    public void init () {
        tileMap = new TileMap(32    );
        tileMap.loadTiles("/Tilesets/sprite_glace.gif");
        tileMap.loadMap("/Maps/level1-2.map");
        tileMap.setPosition(0, 0);

        bg = new Background("/Backgrounds/grassbg1.gif", 0.1);

        player = new Player(tileMap);
        player.setPosition(100, 100);
    }

    @Override
    public void update () {
        // updates the player
        player.update();
        tileMap.setPosition(
                GamePanel.WIDTH / 2 - player.getPosX(),
                GamePanel.HEIGHT / 2 - player.getPosY()
        );

        // update the background
        bg.setPosition(tileMap.getPosX(), tileMap.getPosY());
    }

    @Override
    public void draw (java.awt.Graphics2D g) {
        // draw background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);
    }

    @Override
    public void keyPressed (int k) {
        switch (k) {
            case KeyEvent.VK_Q:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_LEFT:
                player.setLeft(true);
                break;
            case KeyEvent.VK_RIGHT:
                player.setRight(true);
                break;
            case KeyEvent.VK_Z:
                player.setUp(true);
                break;
            case KeyEvent.VK_S:
                player.setDown(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(true);
                break;
            case KeyEvent.VK_ESCAPE:
                exit(0);
                break;
        }
    }

    @Override
    public void keyReleased (int k) {
        switch (k) {
            case KeyEvent.VK_Q:
                player.setLeft(false);
                break;
            case KeyEvent.VK_D:
                player.setRight(false);
                break;
            case KeyEvent.VK_LEFT:
                player.setLeft(false);
                break;
            case KeyEvent.VK_RIGHT:
                player.setRight(false);
                break;
            case KeyEvent.VK_Z:
                player.setUp(false);
                break;
            case KeyEvent.VK_S:
                player.setDown(false);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(false);
                break;
        }
    }
}
