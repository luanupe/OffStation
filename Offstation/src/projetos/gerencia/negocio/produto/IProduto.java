package projetos.gerencia.negocio.produto;

public interface IProduto {

    public long getId();

    public void setId(long id);

    public int getEstoque();

    public double getPreco();

    public String getNome();

    public String getMarca();

}
