package Entity;

import Main.GamePanel;
import Main.TileMap.Tile;
import Main.TileMap.TileMap;

import java.awt.*;

public abstract class MapObject {
    //====== Variables ======//
    // tiles
    protected TileMap tilemap;
    protected int tilesize;
    protected double xMap, yMap;

    // position
    protected double posX, posY;
    protected double dx, dy;

    // dimensions
    protected int width, height;

    // collisions box
    protected int cWidth, cHeight;

    // collision
    protected int currentRow, currentCol;
    protected double xDest, yDest;
    protected double xTemp, yTemp;
    protected boolean topLeft, topRight, bottomLeft, bottomRight;

    // animation
    protected Animation animation;
    protected int currentAction, previousAction;
    protected boolean facingRight;

    // movement
    protected boolean left, right, up, down;
    protected boolean jumping, falling;

    // movement attributes
    protected double moveSpeed, maxSpeed;
    protected double stopSpeed; // deceleration when stopping to press a key
    protected double fallSpeed, maxFallSpeed;
    protected double jumpStart, stopJumpSpeed;

    //====== Constructor ======//

    public MapObject(TileMap tilemap) {
        this.tilemap = tilemap;
        tilesize = tilemap.getTileSize();
    }

    //====== Methods ======//
    public boolean intersects (MapObject o) {
        Rectangle r1 = this.getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }

    public Rectangle getRectangle () {
        return new Rectangle(
            (int)posX - cWidth,
            (int)posY - cHeight,
            cWidth,
            cHeight
        );
    }

    public void calculateCorners (double x, double y) {
        int leftTile = (int)(posX - cWidth / 2) / tilesize;
        int rightTile = (int)(posX + cWidth / 2 - 1) / tilesize;
        int topTile = (int)(posY - cHeight / 2) / tilesize;
        int downTile = (int)(posY + cHeight / 2 - 1) / tilesize;

        int tl = tilemap.getType(topTile, leftTile);
        int tr = tilemap.getType(topTile, rightTile);
        int bl = tilemap.getType(downTile, leftTile);
        int br = tilemap.getType(downTile, rightTile);

        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;

    }

    public void checkTileMapCollision () {
        currentCol = (int)posX / tilesize;
        currentRow = (int)posY / tilesize;

        xDest = posX + dx;
        yDest = posY + dy;

        xTemp = posX;
        yTemp = posY;

        // checking the collisions on the Y axis
        calculateCorners(posX, yDest);
        if (dy < 0) {
            if (topLeft || topRight) {
                dy = 0;
                yTemp = currentRow * tilesize + cHeight / 2;
            } else {
                yTemp += dy;
            }
        }
        if (dy > 0) {
            if (bottomLeft || bottomRight) {
                dy = 0;
                falling = false;
                yTemp = (currentRow + 1) * tilesize - cHeight / 2;
            } else {
                yTemp += dy;
            }
        }

        // checking the collisions on the X axis
        calculateCorners(xDest, posY);
        if (dx < 0) {
            if (topLeft || bottomLeft) {
                dx = 0;
                xTemp = currentCol * tilesize + cWidth / 2;
            } else {
                xTemp += dx;
            }
        }
        if (dx > 0) {
            if (topRight || bottomRight) {
                dx = 0;
                xTemp = (currentCol + 1) * tilesize - cWidth / 2;
            } else {
                xTemp += dx;
            }
        }

        if (!falling) {
            calculateCorners(posX, yDest + 1);
            if (!bottomLeft && !bottomRight) falling = true;
        }
    }

    public double getPosX () { return (int)posX; }
    public double getPosY () { return (int)posY; }
    public int getWidth () { return width; }
    public int getHeight () { return height; }
    public int getcWidth () { return cWidth; }
    public int getcHeight () { return cHeight; }

    public void setPosition (double x, double y) {
        this.posX = x;
        this.posY = y;
    }

    public void setVector (double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void setMapPosition () {
        xMap = tilemap.getPosX();
        yMap = tilemap.getPosY();
    }

    public void setLeft (boolean left) { this.left = left; }
    public void setRight (boolean right) { this.right = right; }
    public void setUp (boolean up) { this.up = up; }
    public void setDown (boolean down) { this.down = down; }
    public void setJumping (boolean jumping) { this.jumping = jumping; }

    public boolean notOnScreen () {
        return  posX + xMap + width < 0 || posX + xMap - width > GamePanel.WIDTH ||
                posY + yMap + height < 0 || posY + yMap - height > GamePanel.HEIGHT;
    }
}
