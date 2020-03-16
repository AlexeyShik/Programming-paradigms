package client;

import server.Cell;
import server.Move;
import server.Position;

import java.lang.*;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class HumanPlayer implements server.Player {
    private final PrintStream out;
    private final Scanner in;
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.W, '-',
            Cell.Z, '|',
            Cell.E, '.'
    );

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    public boolean checkInput(String inputString) {
        boolean fail = false;
        int cntWhitespace = 0;
        for (int i = 0; i < inputString.length(); ++i) {
            fail |= (!Character.isDigit(inputString.charAt(i)) && !Character.isWhitespace(inputString.charAt(i)));
            if (Character.isWhitespace(inputString.charAt(i))) {
                cntWhitespace++;
                while (i < inputString.length() && Character.isWhitespace(inputString.charAt(i)))
                    ++i;
            }
            if (i == inputString.length())
                cntWhitespace = -1;
        }
        if (fail || cntWhitespace != 1) {
            out.println("Данный ввод некорректный. " +
                    "Введите через пробел в одной строчке два числа");
            return false;
        }
        return true;
    }

    public int makeParseInt(Scanner cin) {
        String r = cin.next();
        int ri = -1;
        if (r.length() < 9)
            ri = parseInt(r);
        return ri;
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            out.println(SYMBOLS.get(cell) + "'s move");
            out.println("Enter row and column");
            final String inputString = in.nextLine();
            checkInput(inputString);
            Scanner cin = new Scanner(inputString);
            int ri = makeParseInt(cin);
            int ci = makeParseInt(cin);
            if (ci == -1 || ri == -1) {
                out.println("Данный ввод некорректный. " +
                        "Введите через пробел в одной строчке два числа");
                continue;
            }
            final Move move = new Move(ri, ci, cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Данный ход " + move + " не является корректным. " +
                    "Повторите ввод данных.");
        }
    }
}
