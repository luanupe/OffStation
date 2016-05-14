package projetos.gerencia.apresentacao;

import java.util.Map;
import jdbchelper.JdbcException;
import projetos.gerencia.Principal;
import projetos.gerencia.exceptions.VeiculoException;
import projetos.gerencia.negocio.cliente.ICliente;
import projetos.gerencia.negocio.veiculo.IVeiculo;
import projetos.gerencia.negocio.veiculo.Veiculo;
import projetos.gerencia.persistencia.veiculo.PersistirVeiculo;

public class ControlarVeiculo {

    public IVeiculo salvar(ICliente dono, int id, String placa, String descricao, String entrada, String saida) {
        return this.salvar(new Veiculo(dono, id, placa, descricao, entrada, saida));
    }

    public IVeiculo salvar(IVeiculo veiculo) {
        try {
            PersistirVeiculo.getInstancia().salvar(veiculo);
            Principal.getInstancia().log(new StringBuilder().append("Veículo '").append(veiculo.getPlaca()).append("' salvo com sucesso! Novo ID: ").append(veiculo.getId()).toString(), "VEÍCULO");
        } catch (JdbcException error) {
            Principal.getInstancia().log(new StringBuilder().append("Veículo '").append(veiculo.getPlaca()).append("' não pode ser salvo. Erro: ").append(error.getMessage()).toString(), "VEÍCULO");
            throw (new VeiculoException("Não foi possível salvar o produto. Tente novamente mais tarde."));
        }
        return veiculo;
    }

    public IVeiculo recuperar(int id) {
        return PersistirVeiculo.getInstancia().recuperar(id);
    }

    public Map<Long, IVeiculo> recuperarPeloDono(int idDono) {
        return PersistirVeiculo.getInstancia().recuperarPeloDono(idDono);
    }

    public Map<Long, IVeiculo> recuperarPeloDono(ICliente dono) {
        return PersistirVeiculo.getInstancia().recuperarPeloDono(dono);
    }

}
