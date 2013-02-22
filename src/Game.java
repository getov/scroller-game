/**
 * WARNING !!!
 * The game is tested only on monitors with maximum resolution of 1366x768 and 1600x900
 * It works also good on 1280x720, 1280x768, 1360x768
 * On resolutions different from the above, the game may not work correctly, may be too slow(on higher resolutions) or
 * too fast(on lower resolutions)
 *
 * This Game class is the main class of the game and it is responsible for handling animations, collisions, drawing,
 * taking mouse and keyboard input.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener {

    private Player player;
    private PlayerTwo playerTwo;
    private Shield shield;
    private Image background;
    private Image backgroundMenu;
    private Timer time;

    private int backgroundY;
    private int bgMotion;
    private int bgMotionSec;
    private int playerOneScore;
    private int playerTwoScore;

    /* GAME VARIABLES */
    private int escapeCounter = 0;
    private int keyTwoCounter = 0;

    /* GAME BOOLEAN data */
    private boolean isGameLost;
    private boolean isGameStarted = false;

    // get the screen dimensions
    private int screenWidth  = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

    private DataConfig cfg = new DataConfig();

    /* add all the enemies and their weapons into ArrayList */
    public ArrayList<SpaceCraft> enemies = new ArrayList<SpaceCraft>();
    public ArrayList<SpaceCraftWeapon> fireShots = new ArrayList<SpaceCraftWeapon>();

    Random randGen = new Random();

    /* GAME CONSTANTS */
    private final int ENEMY_SPEED        = 2;
    private final int BUTTON_PADDING_TOP = 35;
    private final int ENEMIES_SPAWN_Y    = 6000;

    /* MENU BUTTONS AND VALUES*/
    GameMenu menu = new GameMenu();

    private boolean isStartButtonHovered;
    private boolean isExitButtonHovered;
    private boolean isSettingsButtonHovered;
    private boolean isBackButtonHovered;

    /**
     * Class for registering key presses
     */
    private class MyActionListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e){
            player.keyPressed(e);
            playerTwo.keyPressed(e);
            if (player.isAlive()){
                shield.keyPressed(e);
            }

            if (e.getKeyCode() == KeyEvent.VK_F1){
                saveConfig();
            }
            if (e.getKeyCode() == KeyEvent.VK_F12){
                loadConfig();
                playerTwo.setImage("images/playerTwo.png");
            }

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && escapeCounter < 1){
                isGameStarted = false;
                ++escapeCounter;
            }
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && escapeCounter > 0){
                isGameStarted = true;
                --escapeCounter;
            }

            if (e.getKeyCode() == KeyEvent.VK_2 && keyTwoCounter == 0 && !isGameLost){
                playerTwo.setAlive(true);
                ++keyTwoCounter;
            }
        }

        @Override
        public void keyReleased(KeyEvent e){
            player.keyReleased(e);
            playerTwo.keyReleased(e);
            if (player.isAlive()){
                shield.keyPressed(e);
            }

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE && escapeCounter > 0){
                isGameStarted = false;
            }
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && escapeCounter < 1){
                isGameStarted = true;
            }
        }
    }

    /**
     * Class for registering mouse events
     */
    private class MouseEvents extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent e){
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (mouseX > menu.startButton().x && mouseX < menu.startButton().x + menu.startButton().width &&
                mouseY > menu.startButton().y && mouseY < menu.startButton().y + menu.startButton().height &&
                    !isGameStarted && menu.isMainMenuActive()){

                isStartButtonHovered = true;

            } else {
                isStartButtonHovered = false;
            }

            if (mouseX > menu.settingsButton().x && mouseX < menu.settingsButton().x + menu.settingsButton().width &&
                mouseY > menu.settingsButton().y && mouseY < menu.settingsButton().y + menu.settingsButton().height &&
                    !isGameStarted && menu.isMainMenuActive()){

                isSettingsButtonHovered = true;

            } else {
                isSettingsButtonHovered = false;
            }

            if (mouseX > menu.exitButton().x && mouseX < menu.exitButton().x + menu.exitButton().width &&
                mouseY > menu.exitButton().y && mouseY < menu.exitButton().y + menu.exitButton().height &&
                    !isGameStarted && menu.isMainMenuActive()){

                isExitButtonHovered = true;

            } else {
                isExitButtonHovered = false;
            }

            if (mouseX > menu.backButton().x && mouseX < menu.backButton().x + menu.backButton().width &&
                mouseY > menu.backButton().y && mouseY < menu.backButton().y + menu.backButton().height &&
                    !isGameStarted){

                isBackButtonHovered = true;

            } else {
                isBackButtonHovered = false;
            }
        }

        @Override
        public void mousePressed(MouseEvent e){
            int mouseX = e.getX();
            int mouseY = e.getY();

            if (mouseX > menu.startButton().x && mouseX <  menu.startButton().x +  menu.startButton().width &&
                mouseY >  menu.startButton().y && mouseY <  menu.startButton().y +  menu.startButton().height &&
                    !isGameStarted && menu.isMainMenuActive()){

                isGameStarted = true;
            }
            if (mouseX > menu.settingsButton().x && mouseX < menu.settingsButton().x + menu.settingsButton().width &&
                mouseY > menu.settingsButton().y && mouseY < menu.settingsButton().y + menu.settingsButton().height &&
                    !isGameStarted && menu.isMainMenuActive()){

                menu.setSettingsOpened(true);
            }
            if (mouseX > menu.backButton().x && mouseX < menu.backButton().x + menu.backButton().width &&
                mouseY > menu.backButton().y && mouseY < menu.backButton().y + menu.backButton().height &&
                    !isGameStarted){

                menu.setBackClicked(true);
            }
            if (mouseX > menu.exitButton().x && mouseX < menu.exitButton().x + menu.exitButton().width &&
                mouseY > menu.exitButton().y && mouseY < menu.exitButton().y + menu.exitButton().height &&
                    !isGameStarted && menu.isMainMenuActive()){

                System.exit(0);
            }
        }
    }

    public Game(){

        player    = new Player();
        playerTwo = new PlayerTwo();
        shield    = new Shield(player.getX(), player.getY());

        /* spawn enemies */
        for (int i = 0; i < 10 ; ++i){
            int x_position = 50 + randGen.nextInt(screenWidth - 100);
            int y_position = -randGen.nextInt(ENEMIES_SPAWN_Y);

            enemies.add(new SpaceCraft(x_position, y_position));
        }

        /* add key and mouse listeners */
        addKeyListener(new MyActionListener());
        addMouseListener(new MouseEvents());
        addMouseMotionListener(new MouseEvents());

        ImageIcon img = new ImageIcon("images/background.jpg");
        background    = img.getImage();

        // set the background for the game menu for different resolutions
        String menuBackground = "images/gameMenuBG.jpg";
        if (screenWidth == 1366 && screenHeight == 768){
            menuBackground = "images/gameMenu2.jpg";
        } else if (screenWidth == 1440 && screenHeight == 900){
            menuBackground = "images/gameMenu3.jpg";
        }
        ImageIcon bgImg = new ImageIcon(menuBackground);
        backgroundMenu  = bgImg.getImage();

        bgMotion    = background.getHeight(null);
        bgMotionSec = 0;
        backgroundY = 0;

        playerOneScore = 0;

        isGameLost = false;

        setFocusable(true);

        time = new Timer(2, this);
        time.start();
    }

    /**
     * Method for calculating background speed
     */
    public void backgroundMovement(){

        bgMotion    -= 1;
        bgMotionSec += 1;
        backgroundY += 1;
    }

    /**
     * This function handles collision detection
     * for all aspects of the game
     */
    public void detectCollisions(){

        ArrayList blasterShots = player.getBlasterShots();

        Rectangle playerBounds = player.getBounds();

        ArrayList<Rectangle> enemiesBounds = new ArrayList<Rectangle>();

        /* HANDLE COLLISION DETECTION FOR ALL THE ENEMIES */
        for (int i = 0; i < enemies.size(); ++i){
            enemiesBounds.add(enemies.get(i).getBounds());
        }
        /* check if blaster hits the enemies */
        for (int i = 0; i < blasterShots.size(); ++i){
            Blaster temp = (Blaster)blasterShots.get(i);
            Rectangle blasterBounds = temp.getBounds();

            for (int j = 0; j < enemies.size(); ++j){
                if (enemiesBounds.get(j).intersects(blasterBounds) && enemies.get(j).isAlive()){
                    enemies.get(j).setAlive(false);
                    temp.setVisible(false);
                    playerOneScore += 10;
                }
            }
        }
        /* check if laser hits the enemies */
        ArrayList<Laser> laserShots = player.getLaserShots();
        for (int i = 0; i < laserShots.size(); ++i){
            Laser temp = laserShots.get(i);
            Rectangle laserBounds = temp.getBounds();

            for (int j = 0; j < enemies.size(); ++j){
                if (enemiesBounds.get(j).intersects(laserBounds) && enemies.get(j).isAlive()){
                    enemies.get(j).setAlive(false);
                    temp.setVisible(false);
                    playerOneScore += 10;
                }
            }
        }

        /* check if player collides with enemies */
        for (int i = 0; i < enemies.size(); ++i){
            if (playerBounds.intersects(enemies.get(i).getBounds()) &&
                    !shield.isShieldActive() && enemies.get(i).isAlive()){

                player.setAlive(false);
            }
        }

        // create rectangles around enemies fire
        ArrayList<Rectangle> enemiesFire = new ArrayList<Rectangle>();
        for (int i = 0; i < fireShots.size(); ++i){
            enemiesFire.add(fireShots.get(i).getBounds());
        }

        // check for collisions between player and enemies' fire
        for (int i = 0; i < fireShots.size(); ++i){
            if (playerBounds.intersects(fireShots.get(i).getBounds()) &&
                    !shield.isShieldActive() && fireShots.get(i).isVisible()){

                fireShots.get(i).setVisible(false);
                player.setAlive(false);
            }
        }

        // PLAYER TWO COLLISIONS
        ArrayList blasterShotsTwo = playerTwo.getBlasterShots();

        Rectangle playerTwoBounds = playerTwo.getBounds();

        /* check if blaster hits the enemies */
        for (int i = 0; i < blasterShotsTwo.size(); ++i){
            Blaster temp = (Blaster)blasterShotsTwo.get(i);
            Rectangle blasterBounds = temp.getBounds();

            for (int j = 0; j < enemies.size(); ++j){
                if (enemiesBounds.get(j).intersects(blasterBounds) && enemies.get(j).isAlive()){
                    enemies.get(j).setAlive(false);
                    temp.setVisible(false);
                    playerTwoScore += 10;
                }
            }
        }
        /* check if laser hits the enemies */
        ArrayList<Laser> laserShotsTwo = playerTwo.getLaserShots();
        for (int i = 0; i < laserShotsTwo.size(); ++i){
            Laser temp = laserShotsTwo.get(i);
            Rectangle laserBounds = temp.getBounds();

            for (int j = 0; j < enemies.size(); ++j){
                if (enemiesBounds.get(j).intersects(laserBounds) && enemies.get(j).isAlive()){
                    enemies.get(j).setAlive(false);
                    temp.setVisible(false);
                    playerTwoScore += 10;
                }
            }
        }

        /* check if player two collides with enemies */
        for (int i = 0; i < enemies.size(); ++i){
            if (playerTwoBounds.intersects(enemies.get(i).getBounds()) &&
                    enemies.get(i).isAlive() && playerTwo.isAlive()){

                playerTwo.setAlive(false);
            }
        }
        // check for collisions between player and enemies' fire
        for (int i = 0; i < fireShots.size(); ++i){
            if (playerTwoBounds.intersects(fireShots.get(i).getBounds()) &&
                    fireShots.get(i).isVisible() && playerTwo.isAlive()){

                fireShots.get(i).setVisible(false);
                playerTwo.setAlive(false);
            }
        }

        if (!player.isAlive() && !playerTwo.isAlive()){
            isGameLost = true;
        }

    }

    /**
     * This method takes all the actions that are performed in the game
     * and repeats them every x ms, where x is the value in Timer(x, this);
     * In my case x = 2.
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if (isGameStarted){

            if (player.isAlive()){
                if (player.isKeyLeft() && player.getX() > 20){
                    player.moveLeft();
                }
                if (player.isKeyRight() && player.getX() < (screenWidth - 116)){
                    player.moveRight();
                }
                if (player.isKeyUp() && player.getY() > 20){
                    player.moveForward();
                }
                if (player.isKeyDown() && player.getY() < (screenHeight - 118)){
                    player.moveBack();
                }

                if (player.getBlasterDelay() > 0){
                    player.setBlasterDelay();
                }
                if (player.isFire() && player.getBlasterDelay() == 0){
                    player.generateBlaster();
                }
                if (player.isSpecialWeapon()){
                    player.generateLaser();
                }

                if (player.isKeyLeft()){
                    player.setImage("images/spacecraft-turn-left.png");
                }
                else if (player.isKeyRight()){
                    player.setImage("images/spacecraft-turn-right.png");
                }
                else {
                    player.setImage("images/spacecraft.png");
                }
            }

            if (playerTwo.isAlive()){
                if (playerTwo.isKeyLeft() && playerTwo.getX() > 20){
                    playerTwo.moveLeft();
                }
                if (playerTwo.isKeyRight() && playerTwo.getX() < (screenWidth - 116)){
                    playerTwo.moveRight();
                }
                if (playerTwo.isKeyUp() && playerTwo.getY() > 20){
                    playerTwo.moveForward();
                }
                if (playerTwo.isKeyDown() && playerTwo.getY() < (screenHeight - 118)){
                    playerTwo.moveBack();
                }

                if (playerTwo.getBlasterDelay() > 0){
                    playerTwo.setBlasterDelay();
                }
                if (playerTwo.isFire() && playerTwo.getBlasterDelay() == 0){
                    playerTwo.generateBlaster();
                }
                if (playerTwo.isSpecialWeapon()){
                    playerTwo.generateLaser();
                }
            }

            backgroundMovement();

            if (player.isAlive()){
                shield.moveShield(player);
            }

            if (player.isAlive() || playerTwo.isAlive() || (player.isAlive() && playerTwo.isAlive())){
                detectCollisions();
                movePlayerWeapons();
            }

            if (!player.isAlive()){
                player.moveDeadPlayer();
            }

            if (!playerTwo.isAlive() && keyTwoCounter > 0){
                playerTwo.moveDeadPlayer();
            }

            moveEnemies();
            enemiesFire();

            repaint();
        }
    }

    /**
     * Method for controlling enemies' movement
     */
    public void moveEnemies(){
        Random rand = new Random();

        for (int i = 0; i < enemies.size(); ++i){
            SpaceCraft enemy = enemies.get(i);

            if (enemies.get(i).isAlive()){
                enemies.get(i).moveForward(ENEMY_SPEED);
                if (i % 2 == 0 && enemies.get(i).getY() > 0){
                    if (enemies.get(i).getX() > 0 && enemies.get(i).getX() < 400){
                        enemies.get(i).moveRight();
                    }
                    if (enemies.get(i).getX() > 500 && enemies.get(i).getX() < screenWidth - 60){
                        enemies.get(i).moveLeft();
                    }
                }
            }

            if (!enemy.isAlive() || enemy.getY() > screenHeight){
                int x_position = 50 + rand.nextInt(screenWidth - 100);
                int y_position = -rand.nextInt(ENEMIES_SPAWN_Y);
                enemy = new SpaceCraft(x_position, y_position);

                enemies.set(i, enemy);
            }
        }
    }

    private static Random rand = new Random();
    /**
     * This method enables shooting for enemies
     */
    public void enemiesFire(){
        //enemies shooting
        int countTime = 2000;
        if (countTime - 10 > 1){
            int randIndex = rand.nextInt(enemies.size());
            randIndex     = Math.abs(randIndex);
            SpaceCraft enemy = enemies.get(randIndex);
            int fireDelay = enemy.getCurrentFireDelay();

            if (fireDelay == 0 && enemy.isAlive()){
                SpaceCraftWeapon fire = enemies.get(randIndex).shoot();
                fireShots.add(fire);
                enemy.setCurrentFireDelay(fire.getFireDelay());
            } else {
                enemy.setCurrentFireDelay(fireDelay - 1);
            }
            countTime = 0;
        }
        countTime++;
        //move shots
        for (int i = 0; i < fireShots.size(); ++i){
            SpaceCraftWeapon fireShot = fireShots.get(i);
            if (fireShot.isVisible()){
                fireShot.moveShot();
            } else {
                fireShots.remove(i);
            }
        }
    }

    /**
     * Method for moving Players' weapon types
     */
    public void movePlayerWeapons(){
        /* blaster shots */
        ArrayList blasterShots = player.getBlasterShots();
        /* if the blaster is visible - move */
        for (int i = 0; i < blasterShots.size(); ++i){
            Blaster temp = (Blaster)blasterShots.get(i);
            if (temp.isVisible() == true){
                temp.moveShot();
            } else {
                blasterShots.remove(i);
            }
        }

        // laser
        ArrayList<Laser> laserShots = player.getLaserShots();
        for (int i = 0; i < laserShots.size(); ++i){
            Laser temp = laserShots.get(i);
            if (temp.isVisible() == true){
                temp.moveShot();
            } else {
                laserShots.remove(i);
            }
        }

        // PLAYER TWO
        /* blaster shots */
        ArrayList blasterShotsTwo = playerTwo.getBlasterShots();
        /* if the blaster is visible - move */
        for (int i = 0; i < blasterShotsTwo.size(); ++i){
            Blaster temp = (Blaster)blasterShotsTwo.get(i);
            if (temp.isVisible() == true){
                temp.moveShot();
            } else {
                blasterShotsTwo.remove(i);
            }
        }

        // laser
        ArrayList<Laser> laserShotsTwo = playerTwo.getLaserShots();
        for (int i = 0; i < laserShotsTwo.size(); ++i){
            Laser temp = laserShotsTwo.get(i);
            if (temp.isVisible() == true){
                temp.moveShot();
            } else {
                laserShotsTwo.remove(i);
            }
        }
    }

    /**
     * Main method for drawing the whole game:
     * Game Menu
     * Background
     * Weapons
     * Enemies
     * Players ... etc.
     */
    @Override
    public void paint(Graphics g){

        super.paint(g);
        Graphics2D graphics2D = (Graphics2D)g;

        if (!isGameStarted){

            drawGameMenu(graphics2D);

        } else {

            drawBackground(graphics2D);

            drawWeapons(graphics2D);

            drawEnemies(graphics2D);

            drawPlayers(graphics2D);

            /* draw shield */
            if (shield.isShieldActive()){
                graphics2D.drawImage(shield.getImage(), shield.shieldX(), shield.shieldY(), null);
            }

            drawStats(graphics2D);

            if (isGameLost){
                graphics2D.setFont(new Font("SanSerif", Font.BOLD, 50));
                graphics2D.setColor(Color.red);
                graphics2D.drawString("YOU SUCK", (screenWidth - 200) / 2, screenHeight / 2);
            }
        }
    }

    /**
     * Method for drawing the Game Menu
     */
    public void drawGameMenu(Graphics g){
        menu.drawGameMenu(g, backgroundMenu, BUTTON_PADDING_TOP,
                          isStartButtonHovered, isExitButtonHovered,
                          isSettingsButtonHovered, isBackButtonHovered);

        repaint();
    }

    /**
     * Method for calculating and drawing the scrolling background
     */
    public void drawBackground(Graphics g){
        if ((backgroundY - 0) % (background.getHeight(null) * 2) == 0){
            bgMotionSec = 0;
        } else if ((backgroundY - background.getHeight(null)) % (background.getHeight(null) * 2) == 0){
            bgMotion = (background.getHeight(null) * 2);
        }
        g.drawImage(background, 0, background.getHeight(null) - bgMotion, null);
        if (backgroundY > 0){
            g.drawImage(background, 0, -(background.getHeight(null) - bgMotionSec), null);
        }
    }

    /**
     * Method for drawing weapons in the game
     */
    public void drawWeapons(Graphics g){
        if (player.isAlive()){
            /* paint blaster beams */
            /* create arraylist to store blaster shots array */
            ArrayList blasterShots = player.getBlasterShots();
            for (int i = 0; i < blasterShots.size(); ++i){
                Blaster temp = (Blaster)blasterShots.get(i);
                g.drawImage(temp.getBlasterImg(), temp.getXPos(), temp.getYPos(), null);
            }
            // paint laser
            ArrayList<Laser> laserShots = player.getLaserShots();
            for (int i = 0; i < laserShots.size(); ++i){
                Laser temp = laserShots.get(i);
                g.drawImage(temp.getLaserImg(), temp.getXPos(), temp.getYPos(), null);
            }
        }

        //enemies weapon
        for (int i = 0; i < fireShots.size(); ++i){
            SpaceCraftWeapon fire = fireShots.get(i);
            if (fire.isVisible()){
                g.drawImage(fire.getEnemyFireImg(), fire.getXPos(), fire.getYPos(), null);
            }
        }

        // PLAYER TWO
        if (playerTwo.isAlive()){
            ArrayList blasterShotsTwo = playerTwo.getBlasterShots();
            for (int i = 0; i < blasterShotsTwo.size(); ++i){
                Blaster temp = (Blaster)blasterShotsTwo.get(i);
                temp.setImage("images/blasterTwo.png");
                g.drawImage(temp.getBlasterImg(), temp.getXPos(), temp.getYPos(), null);
            }
            // paint laser
            ArrayList<Laser> laserShotsTwo = playerTwo.getLaserShots();
            for (int i = 0; i < laserShotsTwo.size(); ++i){
                Laser temp = laserShotsTwo.get(i);
                temp.setImage("images/laserTwo.png");
                g.drawImage(temp.getLaserImg(), temp.getXPos(), temp.getYPos(), null);
            }
        }
    }

    /**
     * Method for drawing enemies in the game
     */
    public void drawEnemies(Graphics g){
        for (int i = 0; i < enemies.size(); ++i){
            if (enemies.get(i).isAlive()){
                g.drawImage(enemies.get(i).getImage(), enemies.get(i).getX(), enemies.get(i).getY(), null);
            }
        }
    }

    /**
     * Method for drawing players in the game
     */
    public void drawPlayers(Graphics g){
        if (player.isAlive()){
            g.drawImage(player.getImage(), player.getX(), player.getY(), null);
        } else {
            player.setImage("images/explosion.gif");
            g.drawImage(player.getImage(), player.getX(), player.getY(), null);
        }

        if (playerTwo.isAlive()){
            g.drawImage(playerTwo.getImage(), playerTwo.getX(), playerTwo.getY(), null);
        } else if (!playerTwo.isAlive() && keyTwoCounter > 0){
            playerTwo.setImage("images/explosion.gif");
            g.drawImage(playerTwo.getImage(), playerTwo.getX(), playerTwo.getY(), null);
        }
    }

    /**
     * Method for drawing game statistics
     */
    public void drawStats(Graphics g){
        g.setFont(new Font("SanSerif", Font.BOLD, 20));
        g.setColor(Color.GREEN);
        g.drawString("PLAYER ONE", screenWidth - 200, 30);
        g.drawString("POINTS: " + playerOneScore, screenWidth - 200, 70);
        if (shield.isShieldActive()){
            g.drawString("SHIELD: ACTIVE", screenWidth - 200, 100);
        } else {
            g.drawString("SHIELD: DISABLED", screenWidth - 200, 100);
        }
        // Player 2
        if (playerTwo.isAlive() || keyTwoCounter > 0){
            g.drawString("PLAYER TWO", 5, 30);
            g.drawString("POINTS: " + playerTwoScore, 5, 70);
        }
    }

    /**
     * Method for saving the current game progress
     */
    public void saveConfig(){
        ObjectOutputStream out;
        /* save the data */
        cfg.player_one_x_pos  = player.getX();
        cfg.player_one_y_pos  = player.getY();
        cfg.player_one_score  = playerOneScore;
        cfg.player_one_shield = shield.isShieldActive();

        cfg.player_two_x_pos = playerTwo.getX();
        cfg.player_two_y_pos = playerTwo.getY();
        cfg.player_two_score = playerTwoScore;

        cfg.key_two_counter  = keyTwoCounter;

        cfg.is_player_one_alive = player.isAlive();
        cfg.is_player_two_alive = playerTwo.isAlive();

        cfg.background_position   = backgroundY;
        cfg.background_motion     = bgMotion;
        cfg.background_motion_sec = bgMotionSec;

        cfg.is_game_lost = false;

        try{
            out = new ObjectOutputStream(new FileOutputStream("Game.cfg"));
            out.writeObject(cfg);
            out.flush();
            out.close();
        }
        catch(IOException e){}
    }

    /**
     * Method for loading the last saved game progress
     */
    public void loadConfig(){
        ObjectInputStream in;
        try{
            in = new ObjectInputStream(new FileInputStream("Game.cfg"));
            try{
                cfg = (DataConfig)in.readObject();
                /* load the data */
                player.setX(cfg.player_one_x_pos);
                player.setY(cfg.player_one_y_pos);
                playerOneScore = cfg.player_one_score;
                shield.setShield(cfg.player_one_shield);

                playerTwo.setX(cfg.player_two_x_pos);
                playerTwo.setY(cfg.player_two_y_pos);
                playerTwoScore = cfg.player_two_score;

                keyTwoCounter = cfg.key_two_counter;

                player.setAlive(cfg.is_player_one_alive);
                playerTwo.setAlive(cfg.is_player_two_alive);

                backgroundY = cfg.background_position;
                bgMotion    = cfg.background_motion;
                bgMotionSec = cfg.background_motion_sec;

                isGameLost = cfg.is_game_lost;
            }
            catch(ClassNotFoundException e){}
            catch(IOException e){}
            in.close();
        }
        catch(IOException e){}
    }
}
