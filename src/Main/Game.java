package Main;

import javax.swing.JFrame;

public class Game {
    public static void main (String[] args) {
        JFrame window = new JFrame("Plateformer");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setVisible(true);
    }
}
