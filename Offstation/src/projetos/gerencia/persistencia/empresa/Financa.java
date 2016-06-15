package projetos.gerencia.persistencia.empresa;

import jdbchelper.QueryResult;
import projetos.gerencia.negocio.produto.IProduto;
import projetos.gerencia.persistencia.produto.PersistirProduto;

public class Financa {
    
    public String descricao;
    public int quantidade;
    public double preco;
    public double total;
    
    protected Financa(QueryResult resultado) {
        PersistirProduto controle = PersistirProduto.getInstancia();
        IProduto produto = controle.recuperar(resultado.getInt("pecaID"));
        if ((produto != null)) {
            this.setDescricao(produto.getNome());
            this.setQuantidade(resultado.getInt("Total"));
            this.setPreco(produto.getPreco());
            this.setTotal(produto.getPreco() * resultado.getDouble("Total"));
        }
    }

    public String getDescricao() {
        return descricao;
    }

    private void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    private void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    private void setPreco(double preco) {
        this.preco = preco;
    }

    public double getTotal() {
        return total;
    }

    private void setTotal(double total) {
        this.total = total;
    }
    
}
