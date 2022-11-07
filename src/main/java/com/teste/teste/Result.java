package com.teste.teste;

public class Result {
   private String winner;
   private int nbr_plays;
   private boolean draw;

    public Result(String winner, int nbr_plays, boolean draw) {
        this.winner = winner;
        this.nbr_plays = nbr_plays;
        this.draw = draw;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getNbr_plays() {
        return nbr_plays;
    }

    public void setNbr_plays(int nbr_plays) {
        this.nbr_plays = nbr_plays;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }
}
