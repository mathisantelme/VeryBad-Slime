package Entity;

import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private long startTimer, delay;
    private boolean playedOnce;

    public Animation () {
        playedOnce = false;
    }

    public void setFrames (BufferedImage[] p_frames) {
        this.frames = p_frames;
        currentFrame = 0;
        startTimer = System.nanoTime();
        playedOnce = false;
    }

    public void setDelay (long d) { this.delay = d; }
    public void setFrame (int i) { this.currentFrame = i; }

    public void update () {
        if (delay == -1) return;

        long elapsed = (System.nanoTime() - startTimer) / 1000000;

        if (elapsed > delay) {
            currentFrame++;
            startTimer = System.nanoTime();
        }
        if (currentFrame == frames.length) {
            currentFrame = 0;
            playedOnce = true;
        }
    }

    public int getFrame () { return currentFrame; }
    public BufferedImage getImage () { return this.frames[currentFrame]; }
    public boolean hasPlayedOnce () { return this.playedOnce; }
}
