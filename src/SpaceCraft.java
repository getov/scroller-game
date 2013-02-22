/**
 * Class for Creating Enemies of type Space Craft
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SpaceCraft implements IEnemy {

    private Image enemyImg;
    private int x_pos;
    private int y_pos;
    private boolean alive;

    private int currentFireDelay = 0;

    public ArrayList<SpaceCraftWeapon> fireShots = new ArrayList<SpaceCraftWeapon>();

    public SpaceCraft(int x, int y){
        this.x_pos = x;
        this.y_pos = y;
        this.alive = true;
        ImageIcon img = new ImageIcon("images/enemy.png");
        enemyImg      = img.getImage();
    }

    public void moveRight(){
        x_pos += 1;
    }

    public void moveLeft(){
        x_pos -= 1;
    }

    public void moveForward(int moveSpeed){
        y_pos += moveSpeed;
    }

    public void setAlive(boolean is){
        this.alive = is;
    }

    public boolean isAlive() {
        return alive;
    }

    public Image getImage(){
        return enemyImg;
    }

    public void setImage(String name){
        ImageIcon img = new ImageIcon(name);
        enemyImg      = img.getImage();
    }

    public int getX(){
        return this.x_pos;
    }

    public int getY(){
        return this.y_pos;
    }

    public void setX(int x){
        this.x_pos = x;
    }

    public void setY(int y){
        this.y_pos = y;
    }

    public SpaceCraftWeapon shoot(){
        SpaceCraftWeapon fireShot = new SpaceCraftWeapon(x_pos, y_pos);
        fireShots.add(fireShot);

        return fireShot;
    }

    public int getCurrentFireDelay() {
        return currentFireDelay;
    }

    public void setCurrentFireDelay(int delay) {
        currentFireDelay = delay;
    }

    /**
     * Method that creates an "invisible" rectangle around the enemy, so
     * we can check for collisions
     */
    public Rectangle getBounds(){
        return new Rectangle(x_pos, y_pos, enemyImg.getWidth(null), enemyImg.getHeight(null));
    }
}
