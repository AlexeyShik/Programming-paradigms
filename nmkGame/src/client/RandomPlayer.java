package client;

import server.Cell;
import server.Move;
import server.Position;
import server.Settings;
import java.util.Random;

public class RandomPlayer implements server.Player {
    private final Random random;

    public RandomPlayer(final Random random) {
        this.random = random;
    }

    public RandomPlayer() {
        this(new Random());
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            int r = random.nextInt(Settings.n);
            int c = random.nextInt(Settings.m);
            final Move move = new Move(r, c, cell);
            if (position.isValid(move)) {
                return move;
            }
        }
    }
}
