package GameState.LevelState;

import Entity.Ennemies.Ennemy;
import Entity.Ennemies.Snake;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import GameState.GameState;
import GameState.GameStateManager;
import Main.GamePanel;
import Main.TileMap.Background;
import Main.TileMap.TileMap;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static java.lang.System.exit;

public class LevelState extends GameState {

    public static final int[] levels = {1, 2, 3};
    public static int currentLevel;

    protected TileMap tileMap;
    protected Background bg;

    protected Player player;
    protected HUD hud;

    protected ArrayList<Ennemy> ennemies;
    protected ArrayList<Explosion> explosions;

    protected int[] endPos;
    //===============================================

    public LevelState (GameStateManager p_GSM) {
        this.GSM = p_GSM;
    }

    //===============================================
    // methods
    @Override
    public void init() {}

    // surcharge de la methode init() de GameState afin de charger les differents fichiers liés aux niveaux
    public void init(
            String pathToMap, String pathToTileSet, int tileSize,
            String pathToBackground, double moveScale,
            int[][] ennemiesPositions,
            double[] playerPos, int[] endPos
    ) {
        // initialisation de tileMap
        tileMap = new TileMap(tileSize);

        // on charge le fichier .map
        try {
            tileMap.loadMap(pathToMap);
        } catch (java.lang.IllegalArgumentException e) {
            // si le fichier .map n'est pas valide ou introuvable on redirige l'utilisateur vers le selecteur de niveau
            System.out.print("The specified path isn't valid please specify valid path for a .map file");
            this.GSM.setState(GameStateManager.MENU);
        }

        try {
            tileMap.loadTiles(pathToTileSet);
        } catch (java.lang.IllegalArgumentException e) {
            // si le tileSet n'est pas valide ou introuvable on redirige l'utilisateur vers le selecteur de niveau
        }

        tileMap.setPosition(0, 0);

        // on charge le background
        try {
            bg = new Background(pathToBackground, moveScale);
        } catch (java.lang.IllegalArgumentException e) {
            System.out.println("path to backgroudn isn't valid please specify valid path for background");
            GSM.setState(GameStateManager.MENU);
        }

        // on initialise les composant liés au joueur
        player = new Player(tileMap);
        player.setPosition(playerPos[0], playerPos[1]);
        //hud = new HUD(player);

        // on charge l'array d'explosion lié aux mort des ennemis
        explosions = new ArrayList<Explosion>();

        this.endPos = endPos;

        populateLevel(ennemiesPositions);
    }

    @Override
    public void update() {
        // updates the player
        player.update();
        if (player.getCurrCol() == endPos[0] && player.getCurrRow() == endPos[1]) {
            switch (currentLevel) {
                case 1:
                    GSM.setState(GameStateManager.LEVEL_02);
                    currentLevel++;
                    break;
                case 2:
                    GSM.setState(GameStateManager.LEVEL_03);
                    currentLevel++;
                    break;
                case 3:
                    GSM.setState(GameStateManager.MENU);
                    currentLevel = 1;
                    break;
            }
        }

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
        if (player.isDead()) {
            GSM.setState(GameStateManager.DEATHSCREEN);
        }
    }

    public void populateLevel (int[][] positions) {
        ennemies = new ArrayList<Ennemy>();
        for (int i = 0; i < positions.length; i++) {
            ennemies.add(new Snake(tileMap));
            ennemies.get(i).setPosition(positions[i][0], positions[i][1]);
        }
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
            case KeyEvent.VK_P:
                System.out.print(player.getPosX() + " : " + player.getPosY());
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
