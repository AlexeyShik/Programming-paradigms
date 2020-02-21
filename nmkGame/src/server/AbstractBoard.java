package server;

import java.util.Arrays;
import java.util.Map;

public class AbstractBoard implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.W, '-',
            Cell.Z, '|',
            Cell.E, '.'
    );

    private static int remain;
    private final Cell[][] cells;
    private Cell turn;

    public AbstractBoard() {
        this.cells = new Cell[Settings.n][Settings.m];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
        if (Settings.typeOfBoard.equals("Rhombus")) {
            remain = 0;
            for (int i = 0; i < Settings.n; ++i)
                for (int j = 0; j < Settings.n; ++j)
                    if (onBoard(i, j))
                        remain++;
        } else {
            remain = Settings.n * Settings.m;
        }
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }
        remain--;
        int i0 = move.getRow();
        int j0 = move.getColumn();
        cells[i0][j0] = move.getValue();

        if (checkWin(i0, j0) == Result.WIN)
            return Result.WIN;

        turn = nextTurn(turn);
        if (remain == 0)
            return Result.DRAW;
        return Result.UNKNOWN;
    }

    boolean onBoard(int i, int j) {
        int d = Settings.n;
        if (i <= (d - 1) / 2) {
            return (d - 1) / 2 - i <= j && j <= d / 2 + i;
        }
        return (d - 1) / 2 - ((d - 1) - i) <= j && j <= (d - 1) / 2 + d - i - (d % 2);
    }

    boolean correctIndex(int i, int j) {
        if (Settings.typeOfBoard.equals("Rhombdus")) {
            return onBoard(i, j) && cells[i][j] == turn;
        }
        return i >= 0 && i < Settings.n && j >= 0 && j < Settings.m && cells[i][j] == turn;
    }

    Result calc(int i0, int j0, int di, int dj) {
        int streak = 0;
        for (int i = i0, j = j0; correctIndex(i, j) && streak < Settings.k; i += di, j += dj)
            streak++;
        for (int i = i0 - di, j = j0 - dj; correctIndex(i, j) && streak < Settings.k; i -= di, j -= dj)
            streak++;
        if (streak == Settings.k)
            return Result.WIN;
        return Result.UNKNOWN;
    }

    Result checkWin(int i0, int j0) {
        boolean isWin = calc(i0, j0, 0, 1) == Result.WIN;
        isWin |= calc(i0, j0, 1, 0) == Result.WIN;
        isWin |= calc(i0, j0, 1, 1) == Result.WIN;
        isWin |= calc(i0, j0, 1, -1) == Result.WIN;
        if (isWin)
            return Result.WIN;
        return Result.UNKNOWN;
    }

    Cell nextTurn(Cell turn) {
        switch (turn) {
            case X : {
                return Cell.O;
            }
            case O: {
                if (Settings.numberOfPlayers > 2)
                    return Cell.W;
                return Cell.X;
            }
            case W: {
                if (Settings.numberOfPlayers > 3)
                    return Cell.Z;
                return Cell.X;
            }
            case Z: {
                return Cell.X;
            }
            default: {
                return Cell.E;
            }
        }
    }

    @Override
    public boolean isValid(final Move move) {
        if (!(cells[move.getRow()][move.getColumn()] == Cell.E && turn == move.getValue()))
            return false;
        return Settings.typeOfBoard.equals("Rhombus") ? onBoard(move.getRow(), move.getColumn()) :
                0 <= move.getRow() && move.getRow() < Settings.n
                && 0 <= move.getColumn() && move.getColumn() < Settings.m;
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" ");
        for (int i = 0; i < Settings.m; ++i)
            sb.append(i);
        for (int r = 0; r < Settings.n; r++) {
            sb.append("\n");
            sb.append(r);
            if (Settings.typeOfBoard.equals("Rhombus") && Settings.n >= 10 && r < 10)
                sb.append(' ');
            for (int c = 0; c < Settings.m; c++) {
                if (Settings.typeOfBoard.equals("Rhombus")) {
                    if (onBoard(r, c)) {
                        sb.append(SYMBOLS.get(cells[r][c]));
                    } else {
                        sb.append(' ');
                    }
                } else {
                    sb.append(SYMBOLS.get(cells[r][c]));
                }
            }
        }
        return sb.toString();
    }
}
