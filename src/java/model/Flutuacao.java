package model;

import java.util.List;

public class Flutuacao {

    private String[] nome;
    List<String> horas;
    List<Integer>[] flutuacao;

    public Flutuacao() {
        nome = new String[3];
    }

    public List<String> getHoras() {
        return horas;
    }

    public void setHoras(List<String> horas) {
        this.horas = horas;
    }

    public List<Integer> getFlutuacao(int i) {
        return flutuacao[i];
    }

    public void setFlutuacao(List<Integer> flutuacao, int i) {
        this.flutuacao[i] = flutuacao;
    }

    public String getNome(int i) {
        return nome[i];
    }

    public void setNome(String nome, int i) {
        this.nome[i] = nome;
    }
}
