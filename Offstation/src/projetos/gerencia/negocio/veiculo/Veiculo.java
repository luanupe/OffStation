package projetos.gerencia.negocio.veiculo;

import java.util.List;
import projetos.gerencia.exceptions.ComprarException;
import projetos.gerencia.negocio.cliente.ICliente;
import projetos.gerencia.persistencia.veiculo.PersistirOrcamento;

public final class Veiculo implements IVeiculo {

    private ICliente dono;
    private long id;
    private String placa;
    private String descricao;
    private String entrada;
    private String saida;
    private PersistirOrcamento persistencia;

    public Veiculo(ICliente dono, int id, String placa, String descricao, String entrada, String saida) {
        this.setDono(dono);
        this.setId(id);
        this.setPlaca(placa);
        this.setDescricao(descricao);
        this.setEntrada(entrada);
        this.setSaida(saida);
        this.setPersistencia(PersistirOrcamento.getInstancia(this));
    }

    @Override
    public ICliente getDono() {
        return this.dono;
    }

    private void setDono(ICliente dono) {
        this.dono = dono;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getPlaca() {
        return this.placa;
    }

    private void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String getDescricao() {
        return this.descricao;
    }

    private void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String getEntrada() {
        return this.entrada;
    }

    private void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    @Override
    public String getSaida() {
        return this.saida;
    }

    private void setSaida(String saida) {
        this.saida = saida;
    }

    @Override
    public PersistirOrcamento getPersistencia() {
        return this.persistencia;
    }

    private void setPersistencia(PersistirOrcamento persistencia) {
        this.persistencia = persistencia;
    }

    @Override
    public List<IOrcamento> pegarOrcamentos() {
        return this.getPersistencia().pegarOrcamentos();
    }

    @Override
    public void adicionarProduto(IOrcamento orcamento) throws ComprarException {
        this.getPersistencia().adicionarProduto(orcamento);
    }

}
