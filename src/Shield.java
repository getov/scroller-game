/**
 * Class for creating a protecting Shield around Player One
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Shield {

    private int x_pos;
    private int y_pos;
    private Image shield;

    boolean shieldActive;

    public Shield(int x, int y){
        this.x_pos = x;
        this.y_pos = y;

        ImageIcon img = new ImageIcon("images/shield.png");
        shield        = img.getImage();

        shieldActive = false;
    }

    public int shieldX(){
        return this.x_pos;
    }

    public int shieldY(){
        return this.y_pos;
    }

    public void moveShield(Player p){
        this.x_pos = p.getX() - 15;
        this.y_pos = p.getY() - 15;
    }

    public Image getImage(){
        return shield;
    }

    public boolean isShieldActive(){
        return shieldActive;
    }

    public void setShield(boolean is){
        shieldActive = is;
    }

    public void keyPressed(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_NUMPAD5:
                shieldActive = true;
                break;
            case KeyEvent.VK_NUMPAD6:
                shieldActive = false;
                break;
        }
    }

    public void keyReleased(KeyEvent e){
        switch (e.getKeyCode()){
            case KeyEvent.VK_NUMPAD5:
                shieldActive = true;
                break;
            case KeyEvent.VK_NUMPAD6:
                shieldActive = false;
                break;
        }
    }
}
