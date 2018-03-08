package Main.TileMap;

import Main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileMap {
    // position
    private double posX, posY;

    // bounds
    private double xMin, yMin;
    private double xMax, yMax;

    private double tween;

    // map
    private int[][] map;
    private int tileSize;
    private int width, height;
    private int nbRows, nbCols;

    // tileSet
    private BufferedImage tileSet;
    private int nbTilesAcross;
    private Tile[][] tiles;

    // drawwing
    private int rowOffset, colOffset;
    private int nbRowsToDraw, nbColsToDraw;

    public TileMap (int p_tileSize) {
        this.tileSize = p_tileSize;
        nbRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
        nbColsToDraw = GamePanel.WIDTH / tileSize + 2;
        tween = 0.07;
    }

    // methodes

    /**
     * charge une tile en memoire
     * @param path
     */
    public void loadTiles (String path) {
        try {
            tileSet = ImageIO.read(getClass().getResourceAsStream(path));
            nbTilesAcross = tileSet.getWidth() / tileSize;
            tiles = new Tile[2][nbTilesAcross];

            BufferedImage subImage;
            for (int col = 0; col < nbTilesAcross; col++) {
                subImage = tileSet.getSubimage(
                        col * tileSize,
                        0,
                        tileSize,
                        tileSize
                );
                tiles[0][col] = new Tile(subImage, Tile.NORMAL);

                subImage = tileSet.getSubimage(
                        col * tileSize,
                        tileSize,
                        tileSize,
                        tileSize
                );
                tiles[1][col] = new Tile(subImage, Tile.BLOCKED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * charge une map en memoire
     * @param path
     */
    public void loadMap (String path) {
        try {
            InputStream in = getClass().getResourceAsStream(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            nbCols = Integer.parseInt(br.readLine());
            nbRows = Integer.parseInt(br.readLine());
            map = new int[nbRows][nbCols];
            width = nbCols * tileSize;
            height = nbRows * tileSize;

            xMin = GamePanel.WIDTH - width;
            xMax = 0;

            yMin = GamePanel.HEIGHT - height;
            yMax = 0;

            String delims = "\\s+";
            for (int row = 0; row < nbRows; row++) {
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for (int col = 0; col < nbCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTileSize () { return this.tileSize; }
    public int getPosX () { return (int)posX; }
    public int getPosY () { return (int)posY; }
    public int getWidth () { return width; }
    public int getHeight () { return height; }
    public int getType (int row, int col) {
        int rc = map[row][col];
        int r = rc / nbTilesAcross;
        int c = rc % nbTilesAcross;
        return tiles[r][c].getType();
    }

    public void setPosition (double posX, double posY) {
        this.posX += (posX - this.posX) * tween;
        this.posY += (posY - this.posY) * tween;

        fixBounds();

        colOffset = (int) - this.posX / tileSize;
        rowOffset = (int) - this.posY / tileSize;
    }

    private void fixBounds () {
        if (posX < xMin) posX = xMin;
        if (posY < yMin) posY = yMin;
        if (posX > xMax) posX = xMax;
        if (posY > yMax) posY = yMax;
    }

    public void draw (Graphics2D g) {
        for (int row = rowOffset ; row < rowOffset + nbRowsToDraw ; row++) {

            if (row >= nbRows) break;

            for (int col = colOffset ; col < colOffset + nbColsToDraw ; col++) {
                if (col >= nbCols) break;
                if (map[row][col] == 0) continue;

                int rc = map[row][col];
                int r = rc / nbTilesAcross;
                int c = rc % nbTilesAcross;

                g.drawImage(
                        tiles[r][c].getImage(),
                        (int)posX + col * tileSize,
                        (int)posY + row * tileSize,
                        null
                );
            }
        }
    }
}
