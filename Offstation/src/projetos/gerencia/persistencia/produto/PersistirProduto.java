package projetos.gerencia.persistencia.produto;

import java.util.HashMap;
import java.util.Map;
import jdbchelper.QueryResult;
import projetos.gerencia.Principal;
import projetos.gerencia.negocio.produto.IProduto;
import projetos.gerencia.negocio.produto.Peca;
import projetos.gerencia.negocio.produto.Servico;
import projetos.gerencia.persistencia.Conectar;

public class PersistirProduto {

    private static PersistirProduto INSTANCIA = null;

    private PersistirProduto() {
    }

    public static PersistirProduto getInstancia() {
        if ((PersistirProduto.INSTANCIA == null)) {
            Principal.getInstancia().log("Criando instancia de persistencia do produto...");
            PersistirProduto.INSTANCIA = new PersistirProduto();
        }
        return PersistirProduto.INSTANCIA;
    }

    public IProduto salvar(IProduto produto) {
        if ((produto != null)) {
            if ((produto.getId() > 0)) {
                produto = this.atualizar(produto);
            }
            produto = this.inserir(produto);
        } else {
            Principal.getInstancia().log("Nao é possível salvar uma instancia nula...", "PRODUTO");
        }
        return produto;
    }

    private IProduto inserir(IProduto produto) {
        String sql = ("INSERT INTO `produtos` (`id`, `nome`, `preco`, `tipo`, `marca`, `estoque`) VALUES ( NULL, ?, ?, ?, ?, ? )");
        int tipo = (produto instanceof Peca) ? 1 : 2;
        Object[] params = new Object[]{produto.getNome(), produto.getPreco(), tipo, produto.getMarca(), produto.getEstoque()};
        produto.setId(Principal.getInstancia().gerenciarTransacao(sql, params));
        return produto;
    }

    private IProduto atualizar(IProduto produto) {
        String sql = ("UPDATE `produtos` SET `nome` = ?, `preco` = ?, `tipo` = ?, `marca` = ?, `estoque` = ? WHERE ( `id` = ? )");
        int tipo = (produto instanceof Peca) ? 1 : 2;
        Object[] params = new Object[]{produto.getNome(), produto.getPreco(), tipo, produto.getMarca(), produto.getEstoque()};
        Principal.getInstancia().gerenciarTransacao(sql, params);
        return produto;
    }

    public boolean remover(IProduto produto) {
        if ((produto != null) && (produto.getId() > 0)) {
            return (this.remover(produto.getId()));
        }
        return false;
    }

    public boolean remover(long id) {
        return (Conectar.getInstancia().getJdbc().execute("DELETE FROM `produtos` WHERE ( `id` = ? )", new Object[]{id}) == 1);
    }

    public IProduto construir(QueryResult resultado, boolean fechar) {
        IProduto produto = null;
        if ((resultado != null)) {
            if ((resultado.getInt("tipo") == 1)) {
                produto = new Peca(resultado.getInt("id"), resultado.getInt("estoque"), resultado.getDouble("preco"), resultado.getString("nome"), resultado.getString("marca"));
                Principal.getInstancia().log(new StringBuilder().append("Construindo PRODUTO '").append(produto.getNome()).append("' do tipo PEÇA.").toString(), "PRODUTO");
            } else if ((resultado.getInt("tipo") == 2)) {
                produto = new Servico(resultado.getInt("id"), resultado.getInt("estoque"), resultado.getDouble("preco"), resultado.getString("nome"), resultado.getString("marca"));
                Principal.getInstancia().log(new StringBuilder().append("Construindo PRODUTO '").append(produto.getNome()).append("' do tipo SERVIÇO.").toString(), "PRODUTO");
            }

            if ((fechar)) {
                resultado.close();
            }
        }
        return produto;
    }

    public IProduto recuperar(int id) {
        Principal.getInstancia().log(new StringBuilder().append("Recuperando PRODUTO com ID '").append(id).append("' no banco de dados.").toString(), "PRODUTO");
        QueryResult resultado = Conectar.getInstancia().getJdbc().query("SELECT * FROM `produtos` WHERE ( `id` = ? )", new Object[]{id});
        IProduto produto = null;

        if ((resultado.next())) {
            produto = this.construir(resultado, true);
        }

        return produto;
    }

    public IProduto recuperar(String nome) {
        Principal.getInstancia().log(new StringBuilder().append("Recuperando PRODUTO com NOME '").append(nome).append("' no banco de dados.").toString(), "PRODUTO");
        QueryResult resultado = Conectar.getInstancia().getJdbc().query("SELECT * FROM `produtos` WHERE ( `nome` = ? )", new Object[]{nome});
        IProduto produto = null;

        if ((resultado.next())) {
            produto = this.construir(resultado, true);
        }

        return produto;
    }

    private Map<Long, IProduto> recuperarTodos(QueryResult resultados) {
        Map<Long, IProduto> produtos = new HashMap();
        while (resultados.next()) {
            IProduto produto = this.construir(resultados, false);
            produtos.put(produto.getId(), produto);
        }
        return produtos;
    }

    public Map<Long, IProduto> recuperarTodos(String nome) {
        Principal.getInstancia().log(new StringBuilder().append("Recuperando TODOS OS PRODUTOS que contenha '").append(nome).append("' no nome.").toString(), "PRODUTO");
        QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT * FROM `produtos` WHERE ( `nome` = ? ) OR ( `nome` LIKE ? )", new Object[]{nome, (new StringBuilder().append("%").append(nome).append("%").toString())});
        Map<Long, IProduto> produtos = this.recuperarTodos(resultados);
        resultados.close();
        return produtos;
    }

    public Map<Long, IProduto> recuperarTodos() {
        Principal.getInstancia().log("Recuperando TODOS OS PRODUTOS no banco de dados.", "PRODUTO");
        QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT * FROM `produtos`");
        Map<Long, IProduto> produtos = this.recuperarTodos(resultados);
        resultados.close();
        return produtos;
    }

}
