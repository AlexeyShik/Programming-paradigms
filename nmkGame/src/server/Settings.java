package server;

import client.HumanPlayer;
import client.RandomPlayer;
import client.SequentialPlayer;
import java.util.List;

public class Settings {
    static final String typeOfBoard = "Rhombus";  // "Square" and "Rhombus"
    public static final int n = 12;
    public static final int m = 12;
    static final int k = 3;
    static final List<Player> players = List.of(new HumanPlayer(), new RandomPlayer(), new RandomPlayer());
    static final int numberOfPlayers = players.size();
}

//  Если игра на ромбе, то в качестве диагоналей стоит указать n, m. По условию они равны.