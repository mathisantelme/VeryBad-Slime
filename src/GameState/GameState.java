package GameState;

public abstract class GameState {
    protected GameStateManager GSM;

    /**
     * initialise le gameState (sers a charger les differentes ressources necessaires pour l'ecran en cours)
     */
    public abstract void init ();

    /**
     * mets a jour les differents elements de l'ecran
     */
    public abstract void update ();

    /**
     * affiche les differents elements à l'ecran
     * @param g
     */
    public abstract void draw (java.awt.Graphics2D g);

    /**
     * lorsqu'une touche est enfoncee
     * @param k le keycode
     */
    public abstract void keyPressed (int k);

    /**
     * lorsqu'une touche est relachee
     * @param k le keycode
     */
    public abstract void keyReleased (int k);
}
