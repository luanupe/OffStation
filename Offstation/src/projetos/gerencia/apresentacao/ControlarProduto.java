package projetos.gerencia.apresentacao;

import java.util.Map;
import jdbchelper.JdbcException;
import projetos.gerencia.Principal;
import projetos.gerencia.exceptions.ProdutoException;
import projetos.gerencia.negocio.produto.IProduto;
import projetos.gerencia.negocio.produto.Peca;
import projetos.gerencia.negocio.produto.Servico;
import projetos.gerencia.persistencia.produto.PersistirProduto;

public class ControlarProduto {

    public IProduto salvar(int tipo, int id, int estoque, double preco, String nome, String marca) {
        if ((tipo == 1)) {
            return this.salvar(new Peca(id, estoque, preco, nome, marca));
        }
        return this.salvar(new Servico(id, estoque, preco, nome, marca));
    }

    public IProduto salvar(IProduto produto) {
        try {
            PersistirProduto.getInstancia().salvar(produto);
            Principal.getInstancia().log(new StringBuilder().append("Produto '").append(produto.getNome()).append("' salvo com sucesso! Novo ID: ").append(produto.getId()).toString());
        } catch (JdbcException error) {
            Principal.getInstancia().log(new StringBuilder().append("Produto '").append(produto.getNome()).append("' não pode ser salvo.").toString());
            throw (new ProdutoException("Não foi possível salvar o produto, tente novamente."));
        }
        return produto;
    }

    public boolean remover(IProduto produto) {
        return PersistirProduto.getInstancia().remover(produto);
    }

    public boolean remover(int id) {
        return PersistirProduto.getInstancia().remover(id);
    }

    public IProduto recuperar(int id) {
        return PersistirProduto.getInstancia().recuperar(id);
    }

    public IProduto recuperar(String nome) {
        return PersistirProduto.getInstancia().recuperar(nome);
    }

    public Map<Long, IProduto> recuperarTodos(String nome) {
        return PersistirProduto.getInstancia().recuperarTodos(nome);
    }

    public Map<Long, IProduto> recuperarTodos() {
        return PersistirProduto.getInstancia().recuperarTodos();
    }

}
