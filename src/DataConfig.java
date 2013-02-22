/**
 * Class that holds the saved game data
 * and then the data can be loaded back.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class DataConfig implements Serializable {

    public int player_one_x_pos;
    public int player_one_y_pos;
    public int player_one_score;

    public int player_two_x_pos;
    public int player_two_y_pos;
    public int player_two_score;

    public int key_two_counter;

    public boolean player_one_shield;
    public boolean is_player_one_alive;
    public boolean is_player_two_alive;
    public boolean is_game_lost;

    public int background_position;
    public int background_motion;
    public int background_motion_sec;
}
