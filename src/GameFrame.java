/**
 * Class that creates the main frame/window where the game is run
 */

import javax.swing.*;
import java.awt.*;

public class GameFrame {

    public GameFrame(){
        JFrame frame = new JFrame();
        frame.add(new Game(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Spacecraft Battles");
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void main(String[] args){
        new GameFrame();
    }
}