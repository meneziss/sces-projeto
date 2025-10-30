package br.pucgoias.sces.service;

import br.pucgoias.sces.exception.EstoqueException;
import br.pucgoias.sces.model.Produto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EstoqueService {

    // Persistência em memória [cite: 18]
    private final List<Produto> produtos = new ArrayList<>();
    private long proximoId = 1; // Para gerar ID único sequencial [cite: 36]

    /**
     * User Story #1: Cadastrar novo produto [cite: 30]
     */
    public Produto cadastrarProduto(String nome, String descricao, int quantidadeInicial) {
        // Critério de Aceite: A quantidade inicial não pode ser negativa [cite: 38]
        if (quantidadeInicial < 0) {
            throw new EstoqueException("A quantidade inicial não pode ser negativa.");
        }

        // Critério de Aceite: Não deve ser possível cadastrar um produto com nome duplicado [cite: 37]
        if (produtos.stream().anyMatch(p -> p.getNome().equalsIgnoreCase(nome))) {
            throw new EstoqueException("Já existe um produto com o nome: " + nome);
        }

        Produto novoProduto = new Produto(proximoId++, nome, descricao, quantidadeInicial);
        produtos.add(novoProduto);
        return novoProduto;
    }

    /**
     * User Story #2: Listar todos os produtos [cite: 40]
     */
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtos); // Retorna uma cópia da lista
    }

    /**
     * User Story #3: Adicionar unidades ao estoque de um produto [cite: 48]
     */
    public Produto adicionarEstoque(long idProduto, int quantidadeAdicionar) {
        // Critério de Aceite: A quantidade a ser adicionada deve ser um número positivo (> 0) [cite: 53]
        if (quantidadeAdicionar <= 0) {
            throw new EstoqueException("A quantidade a ser adicionada deve ser maior que zero.");
        }

        // Critério de Aceite: O sistema deve localizar o produto pelo seu ID [cite: 52]
        Optional<Produto> produtoOpt = produtos.stream().filter(p -> p.getId() == idProduto).findFirst();

        if (produtoOpt.isEmpty()) {
            // Critério de Aceite: Se o ID do produto não for encontrado, uma mensagem de erro deve ser retornada [cite: 54]
            throw new EstoqueException("Produto com ID " + idProduto + " não encontrado.");
        }

        Produto produto = produtoOpt.get();
        produto.setQuantidade(produto.getQuantidade() + quantidadeAdicionar);
        return produto;
    }
}