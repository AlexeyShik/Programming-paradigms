package server;

import java.util.Arrays;

class Scoreboard {
    public int[] scoreboard;

    public Scoreboard(int numberOfPlayers) {
        scoreboard = new int[numberOfPlayers];
        Arrays.fill(scoreboard, 0);
    }

    public void increaseScore(int i, int x) {
        scoreboard[i] += x;
    }

    public void printScoreboard() {
        System.out.println("Scoreboard:");
        for (int i = 0; i < scoreboard.length; ++i) {
            System.out.println("Player " + (i + 1) + " has score: " + scoreboard[i]);
        }
    }
}
