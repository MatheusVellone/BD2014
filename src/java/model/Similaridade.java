package model;

public class Similaridade {
    private float similaridade;
    private int id;
    private String nome;
    private String foto;

    public float getSimilaridade() {
        return similaridade;
    }

    public void setSimilaridade(float similaridade) {
        this.similaridade = similaridade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
