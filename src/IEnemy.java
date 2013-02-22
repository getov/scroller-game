/**
 * Interface for Enemy types of AI
 */

import java.awt.Image;

public interface IEnemy {

    void moveRight();
    void moveLeft();

    Image getImage();

    int getX();
    int getY();

    boolean isAlive();
}
