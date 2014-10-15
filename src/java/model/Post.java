package model;

import java.sql.Timestamp;

public class Post {

    private Integer id;
    private Integer id_autor;
    private String titulo;
    private String conteudo;
    private boolean republicacao;
    private int id_repub;
    private Timestamp data;
    private int servidor;

    public int getServidor() {
        return servidor;
    }

    public void setServidor(int servidor) {
        this.servidor = servidor;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_autor() {
        return id_autor;
    }

    public void setId_autor(Integer id_autor) {
        this.id_autor = id_autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isRepublicacao() {
        return republicacao;
    }

    public void setRepublicacao(boolean republicacao) {
        this.republicacao = republicacao;
    }

    public int getId_repub() {
        return id_repub;
    }

    public void setId_repub(int id_repub) {
        this.id_repub = id_repub;
    }

}
