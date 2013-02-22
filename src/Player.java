/**
 * Class for creating the main Player in the game
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player {

    private int x_pos;
    private int y_pos;
    private Image spaceShip;

    /* speed of moving right, left and when dead */
    private int moveX;
    private int moveY;
    private int moveYdead;

    private int blasterDelay = 20;

    /* container to hold blaster shots */
    static ArrayList blasterShots;
    static ArrayList<Laser> laserShots;

    private boolean keyLeft;
    private boolean keyRight;
    private boolean keyUp;
    private boolean keyDown;
    private boolean fire;
    private boolean specialWeapon;
    private boolean isAlive;

    public Player(){

        ImageIcon img = new ImageIcon("images/spacecraft.png");
        spaceShip     = img.getImage();

        this.x_pos = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
        this.y_pos = Toolkit.getDefaultToolkit().getScreenSize().height - spaceShip.getHeight(null) - 30;
        this.moveX = 3;
        this.moveY = 3;
        this.moveYdead = 2;

        this.isAlive = true;

        blasterShots = new ArrayList();
        laserShots   = new ArrayList<Laser>();
    }

    public void moveRight(){
        x_pos += moveX;
    }

    public void moveLeft(){
        x_pos -= moveX;
    }

    public void moveForward(){
        y_pos -= moveY;
    }

    public void moveBack(){
        y_pos += moveY;
    }

    public Image getImage(){
        return spaceShip;
    }

    public void setImage(String name){
        ImageIcon img = new ImageIcon(name);
        spaceShip     = img.getImage();
    }

    public int getX(){
        return x_pos;
    }

    public int getY(){
        return y_pos;
    }

    public void setX(int x){
        this.x_pos = x;
    }

    public void setY(int y){
        this.y_pos = y;
    }

    /* generate blaster beam */
    public void generateBlaster(){
          blasterShots.add(new Blaster(x_pos, y_pos));
          blasterDelay = 20;
    }

    public static ArrayList getBlasterShots(){
        return blasterShots;
    }

    /* generate laser beam */
    public void generateLaser(){
        Laser laser = new Laser(x_pos, y_pos);

        while (laserShots.size() < 2){
            laserShots.add(laser);
        }
    }

    public ArrayList<Laser> getLaserShots(){
        return laserShots;
    }

    /**
     * Method for creating a rectangle around the player so we can check for collisions
     */
    public Rectangle getBounds(){
        return new Rectangle(x_pos, y_pos, spaceShip.getWidth(null), spaceShip.getHeight(null));
    }

    public int getBlasterDelay(){
        return this.blasterDelay;
    }

    public void setBlasterDelay(){
        --this.blasterDelay;
    }

    public boolean isKeyLeft(){
        return keyLeft;
    }

    public boolean isKeyRight(){
        return keyRight;
    }

    public boolean isKeyUp(){
        return keyUp;
    }

    public boolean isKeyDown(){
        return keyDown;
    }

    public boolean isFire(){
        return fire;
    }

    public boolean isSpecialWeapon(){
        return specialWeapon;
    }

    public boolean isAlive(){
        return isAlive;
    }

    public void setAlive(boolean is){
        this.isAlive = is;
    }

    public void moveDeadPlayer(){
        this.y_pos += moveYdead;
    }

    /* register when key is down */
    public void keyPressed(KeyEvent e){

        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            keyLeft = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            keyRight = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){
            keyUp = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            keyDown = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD0){
            fire = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD4){
            specialWeapon = true;
        }
    }

    /* register when key is released */
    public void keyReleased(KeyEvent e){

        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            keyLeft = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            keyRight = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP){
            keyUp = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN){
            keyDown = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD0){
            fire = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_NUMPAD4){
            specialWeapon = false;
        }
    }
}
