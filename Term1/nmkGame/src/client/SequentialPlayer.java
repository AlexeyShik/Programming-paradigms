package client;

import server.Cell;
import server.Move;
import server.Position;
import server.Settings;

public class SequentialPlayer implements server.Player {
    @Override
    public Move move(final Position position, final Cell cell) {
        for (int r = 0; r < Settings.n; r++) {
            for (int c = 0; c < Settings.m; c++) {
                final Move move = new Move(r, c, cell);
                if (position.isValid(move)) {
                    return move;
                }
            }
        }
        throw new IllegalStateException("No valid moves");
    }
}
