package server;

class SquareTicTacToeBoard extends AbstractBoard {
    public SquareTicTacToeBoard() {
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
