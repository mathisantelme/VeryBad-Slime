package GameState;



import Entity.Ennemies.Ennemy;
import Entity.*;
import Entity.Ennemies.Knight;
import Main.GamePanel;
import Main.TileMap.Background;
import Main.TileMap.TileMap;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static java.lang.System.exit;

public class Level1State extends GameState {
    private TileMap tileMap;
    private Background bg;

    private Player player;
    private HUD hud;

    private ArrayList<Ennemy> ennemies;
    private ArrayList<Explosion> explosions;

    Level1State(GameStateManager p_GSM) {
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
        //hud = new HUD(player);

        populateLevel();

        explosions = new ArrayList<Explosion>();
    }

    private void populateLevel () {
        ennemies = new ArrayList<Ennemy>();
        ennemies.add(new Knight(tileMap));
        ennemies.get(0).setPosition(200, 100);
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

        // update the enemies
        for (int i = 0; i < ennemies.size(); i++) {
            ennemies.get(i).update();
            if (ennemies.get(i).isDead()) {
                //System.out.println(ennemies.get(i).getPosX() + " " + ennemies.get(i).getPosY());
                explosions.add(new Explosion(ennemies.get(i).getPosX(), ennemies.get(i).getPosY()));
                ennemies.remove(i);
                i--;
            }
        }

        // attack enemies
        player.checkAttack(ennemies);

        // update explosions
        for (int i = 0 ; i < explosions.size() ; i++) {
            Explosion e = explosions.get(i);
            e.update();
            if (e.shouldRemove()) {
                explosions.remove(e);
                i--;
            }
        }

        /// check if player is alive
        if (player.isDead()) GSM.setState(GameStateManager.DEATHSCREEN);
    }

    @Override
    public void draw (java.awt.Graphics2D g) {
        // draw background
        bg.draw(g);

        // draw tilemap
        tileMap.draw(g);

        // draw player
        player.draw(g);

        // draw the hud
        //hud.draw(g);

        // draw the enemies
        for (Ennemy ennemy : ennemies) ennemy.draw(g);

        // draw explosions
        for (Explosion e: explosions) {
            e.setMapPosition(tileMap.getPosX(), tileMap.getPosY());
            e.draw(g);
        }
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
            case KeyEvent.VK_Z:
                player.setUp(true);
                break;
            case KeyEvent.VK_S:
                player.setDown(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJumping(true);
                break;
            case KeyEvent.VK_A:
                player.setFiring();
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
