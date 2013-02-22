/**
 * Class for creating Laser weapon for Players
 */

import javax.swing.*;
import java.awt.*;

public class Laser implements IWeapon {

    private int x_pos;
    private int y_pos;
    private boolean visible;
    private Image laserImg;

    private final int SHOT_SPEED = -10;

    public Laser(int x, int y){
        this.x_pos = x + 50;
        this.y_pos = y + 20;
        visible    = true;
        ImageIcon img = new ImageIcon("images/laser.png");
        laserImg      = img.getImage();
    }

    public int getYPos(){
        return this.y_pos;
    }

    public int getXPos(){
        return this.x_pos;
    }

    public Image getLaserImg(){
        return laserImg;
    }

    public void setImage(String name){
        ImageIcon img = new ImageIcon(name);
        laserImg    = img.getImage();
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean is){
        this.visible = is;
    }

    public void moveShot(){
        this.y_pos += SHOT_SPEED;
        if (this.y_pos < 0){
            visible = false;
        }
    }

    /**
     * This method creates "invisible" rectangle around the Laser, so it can be detected if
     * collisions occur.
     */
    public Rectangle getBounds(){
        return new Rectangle(x_pos, y_pos, laserImg.getWidth(null), laserImg.getHeight(null));
    }
}
