/**
 * Class for creating Weapon for enemies of type Space Craft
 */

import javax.swing.*;
import java.awt.*;

public class SpaceCraftWeapon implements IWeapon {

    private int x_pos;
    private int y_pos;
    private boolean visible;
    private Image enemyFireImg;

    private int fireDelay = 500;

    private final int FIRE_SPEED = 3;

    private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    public SpaceCraftWeapon(int x, int y){
        this.x_pos = x + 20;
        this.y_pos = y + 20;
        visible    = true;

        ImageIcon img = new ImageIcon("images/enemyFire.png");
        enemyFireImg  = img.getImage();
    }

    public int getXPos(){
        return this.x_pos;
    }

    public int getYPos(){
        return this.y_pos;
    }

    public void moveShot(){
        y_pos += FIRE_SPEED;
        if (y_pos > screenHeight){
            visible = false;
        }
    }

    public int getFireDelay(){
        return fireDelay;
    }

    public boolean isVisible(){
        return visible;
    }

    public void setVisible(boolean is){
        this.visible = is;
    }

    public Image getEnemyFireImg(){
        return enemyFireImg;
    }

    /**
     * Method for creating an "invisible" rectangle around the weapon, so
     * collisions can be detected
     */
    public Rectangle getBounds(){
        return new Rectangle(x_pos, y_pos, enemyFireImg.getWidth(null), enemyFireImg.getHeight(null));
    }
}
