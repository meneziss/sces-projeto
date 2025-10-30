package br.pucgoias.sces.model;

public class Produto {
    private long id;
    private String nome;
    private String descricao;
    private int quantidade;

    public Produto(long id, String nome, String descricao, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
    }

    // Getters
    public long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public int getQuantidade() { return quantidade; }

    // Setters
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}