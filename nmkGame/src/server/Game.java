package server;
import java.util.List;

class Game {
    private final boolean log;
    private List<Player> players;

    public Game(final boolean log, List<Player> players) {
        if (Settings.n <= 0 || Settings.m <= 0) {
            err("Введите корректные значения n, m - размер доски, целые положительные числа");
        }
        if (Settings.n > 1000 || Settings.m > 1000) {
            err("Введите размеры доски поменьше");
        }
        if (Settings.k <= 1 || Settings.k > Math.max(Settings.n, Settings.m)) {
            err("Введите корректное k - количество подряд символов для победы," +
                    " целое положительное число, большее 1 и не большее размера доски");
        }
        if (Settings.numberOfPlayers <= 0) {
            err("Введите корректное число игроков - целое положительное число: 2, 3 или 4");
        }
        if (players.size() != Settings.numberOfPlayers) {
            err("Количество фактических игроков должно совпадать" +
                    " с количеством игроков, объявленным в настройках");
        }
        this.log = log;
        this.players = players;
    }

    public int play(Board board) {
        while (true) {
            for (int i = 0; i < Settings.numberOfPlayers; ++i) {
                final int result = move(board, players.get(i), i + 1);
                if (result == -i - 2) {
                    err("Player " + (i + 1) + " попытался нарушить правила." +
                            " Считаем его проигравшим, а игру завершенной");
                }
                if (result != -1) {
                    return result;
                }
            }
        }
    }

    private int move(final Board board, final Player player, final int no) {
        final Move move = player.move(board.getPosition(), board.getCell());
        final Result result = board.makeMove(move);
        log("Player " + no + " move: " + move);
        log("Position:\n" + board);
        if (result == Result.WIN) {
            log("Player " + no + " won");
            return no;
        } else if (result == Result.LOSE) {
            log("Player " + no + " lose");
            return -1 - no;  // Код читерства игрока с номером no
        } else if (result == Result.DRAW) {
            log("Draw");
            return 0;
        } else {
            return -1;
        }
    }

    private void err(final String message) {
        System.err.println("Ошибка: " + message);
        System.exit(0);
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
