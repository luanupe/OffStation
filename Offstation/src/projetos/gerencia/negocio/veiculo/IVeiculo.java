package projetos.gerencia.negocio.veiculo;

import java.util.List;
import projetos.gerencia.exceptions.ComprarException;
import projetos.gerencia.negocio.cliente.ICliente;
import projetos.gerencia.persistencia.veiculo.PersistirOrcamento;

public interface IVeiculo {

    public ICliente getDono();

    public long getId();

    public void setId(long id);

    public String getPlaca();

    public String getDescricao();

    public String getEntrada();

    public String getSaida();

    public PersistirOrcamento getPersistencia();

    public List<IOrcamento> pegarOrcamentos();

    public void adicionarProduto(IOrcamento orcamento) throws ComprarException;

}
