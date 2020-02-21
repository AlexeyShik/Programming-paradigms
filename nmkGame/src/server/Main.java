package server;

public class Main {
    public static void main(String[] args) {
        Scoreboard scoreboard = new Scoreboard(Settings.numberOfPlayers);
        final Game game = new Game(false, Settings.players);
        int result = Settings.typeOfBoard.equals("Rhombus") ? game.play(new RhombusTicTacToeBoard()) :
                game.play(new SquareTicTacToeBoard());
        if (result == 0) {
            for (int i = 0; i < Settings.numberOfPlayers; ++i) {
                scoreboard.increaseScore(i, 1);
            }
        } else if (result > 0) {
            scoreboard.increaseScore(result - 1, 3);
        }
        if (result != 0)
            System.out.println("Game result: Winner is Player " + result);
        else
            System.out.println("Game result: Draw");
        scoreboard.printScoreboard();
    }
}