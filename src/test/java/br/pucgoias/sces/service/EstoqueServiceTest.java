package br.pucgoias.sces.service;

import br.pucgoias.sces.exception.EstoqueException;
import br.pucgoias.sces.model.Produto;

// Importações do JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EstoqueServiceTest {

    private EstoqueService estoqueService;

    @BeforeEach
    void setUp() {
        // Cria uma nova instância para cada teste, garantindo isolamento
        estoqueService = new EstoqueService();
    }

    // --- Testes para US#1: Cadastrar Produto [cite: 39] ---
    @Test
    void deveCadastrarProdutoComSucesso() {
        Produto produto = estoqueService.cadastrarProduto("Monitor", "Monitor 24pol", 10);
        
        assertNotNull(produto);
        assertEquals(1L, produto.getId()); // Valida ID único [cite: 36]
        assertEquals("Monitor", produto.getNome());
        assertEquals(10, produto.getQuantidade());
        assertEquals(1, estoqueService.listarProdutos().size());
    }

    @Test
    void naoDeveCadastrarProdutoComNomeDuplicado() { // [cite: 37]
        estoqueService.cadastrarProduto("Teclado", "Teclado Mecânico", 5);
        
        EstoqueException exception = assertThrows(EstoqueException.class, () -> {
            estoqueService.cadastrarProduto("teclado", "Outro teclado", 2);
        });
        
        assertEquals("Já existe um produto com o nome: teclado", exception.getMessage());
    }

    @Test
    void naoDeveCadastrarProdutoComQuantidadeNegativa() { // [cite: 38]
        EstoqueException exception = assertThrows(EstoqueException.class, () -> {
            estoqueService.cadastrarProduto("Mouse", "Mouse sem fio", -1);
        });
        
        assertEquals("A quantidade inicial não pode ser negativa.", exception.getMessage());
    }

    // --- Testes para US#2: Listar Produtos ---
    @Test
    void deveListarTodosOsProdutos() { // [cite: 46]
        estoqueService.cadastrarProduto("Produto A", "Desc A", 1);
        estoqueService.cadastrarProduto("Produto B", "Desc B", 2);
        
        List<Produto> produtos = estoqueService.listarProdutos();
        
        assertNotNull(produtos);
        assertEquals(2, produtos.size());
    }

    @Test
    void deveRetornarListaVaziaSeNaoHouverProdutos() { // [cite: 47]
        List<Produto> produtos = estoqueService.listarProdutos();
        
        assertNotNull(produtos);
        assertTrue(produtos.isEmpty()); // A UI trataria a mensagem
    }

    // --- Testes para US#3: Adicionar Estoque [cite: 55] ---
    @Test
    void deveAdicionarEstoqueAoProduto() {
        Produto produto = estoqueService.cadastrarProduto("Cadeira", "Cadeira Gamer", 5);
        long idProduto = produto.getId();
        
        Produto produtoAtualizado = estoqueService.adicionarEstoque(idProduto, 10);
        
        assertEquals(15, produtoAtualizado.getQuantidade());
        assertEquals(15, estoqueService.listarProdutos().get(0).getQuantidade());
    }

    @Test
    void naoDeveAdicionarEstoqueComIdInexistente() { // [cite: 54]
        EstoqueException exception = assertThrows(EstoqueException.class, () -> {
            estoqueService.adicionarEstoque(99L, 5); // ID 99 não existe
        });
        
        assertEquals("Produto com ID 99 não encontrado.", exception.getMessage());
    }

    @Test
    void naoDeveAdicionarEstoqueComQuantidadeZeroOuNegativa() { // [cite: 53]
        Produto produto = estoqueService.cadastrarProduto("Mesa", "Mesa de escritório", 3);
        long idProduto = produto.getId();

        // Teste com 0
        EstoqueException exZero = assertThrows(EstoqueException.class, () -> {
            estoqueService.adicionarEstoque(idProduto, 0);
        });
        assertEquals("A quantidade a ser adicionada deve ser maior que zero.", exZero.getMessage());

        // Teste com -5
        EstoqueException exNegativo = assertThrows(EstoqueException.class, () -> {
            estoqueService.adicionarEstoque(idProduto, -5);
        });
        assertEquals("A quantidade a ser adicionada deve ser maior que zero.", exNegativo.getMessage());
    }
}