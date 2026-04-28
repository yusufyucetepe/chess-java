package chess;

import javax.swing.JFrame;

public class Game extends JFrame {
    public Game() {
        super("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
    }
}
