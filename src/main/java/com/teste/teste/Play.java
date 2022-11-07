package com.teste.teste;

public class Play {
    private int id;
    private String jogador;
    private int posicao;

    public Play(int id, String jogador, int posicao) {
        this.id = id;
        this.jogador = jogador;
        this.posicao = posicao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJogador() {
        return jogador;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }
}