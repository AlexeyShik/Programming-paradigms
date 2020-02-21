package server;

class RhombusTicTacToeBoard extends AbstractBoard {

    public RhombusTicTacToeBoard() {
        super();
    }

    @Override
    public Position getPosition() {
        return super.getPosition();
    }

    @Override
    public Cell getCell() {
        return super.getCell();
    }

    @Override
    public Result makeMove(final Move move) {
        return super.makeMove(move);
    }

    boolean correctIndex(int i, int j) {
        return super.correctIndex(i, j);
    }

    Result calc(int i0, int j0, int di, int dj) {
        return super.calc(i0, j0, di, dj);
    }

    Result checkWin(int i0, int j0) {
        return super.checkWin(i0, j0);
    }

    Cell nextTurn(Cell turn) {
        return super.nextTurn(turn);
    }

    @Override
    public boolean isValid(final Move move) {
        return super.isValid(move);
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return super.getCell(r, c);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
