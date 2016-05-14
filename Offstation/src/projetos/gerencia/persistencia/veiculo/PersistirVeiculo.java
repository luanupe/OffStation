package projetos.gerencia.persistencia.veiculo;

import java.util.HashMap;
import java.util.Map;
import jdbchelper.QueryResult;
import projetos.gerencia.Principal;
import projetos.gerencia.apresentacao.ControlarCliente;
import projetos.gerencia.negocio.cliente.ICliente;
import projetos.gerencia.negocio.veiculo.IVeiculo;
import projetos.gerencia.negocio.veiculo.Veiculo;
import projetos.gerencia.persistencia.Conectar;

public class PersistirVeiculo {

    private static PersistirVeiculo INSTANCIA = null;

    private PersistirVeiculo() {
    }

    public static PersistirVeiculo getInstancia() {
        if ((PersistirVeiculo.INSTANCIA == null)) {
            Principal.getInstancia().log("Criando instancia de persistencia do veiculo...");
            PersistirVeiculo.INSTANCIA = new PersistirVeiculo();
        }
        return PersistirVeiculo.INSTANCIA;
    }

    public IVeiculo salvar(IVeiculo veiculo) {
        if ((veiculo != null)) {
            if ((veiculo.getId() > 0)) {
                veiculo = this.atualizar(veiculo);
            }
            veiculo = this.inserir(veiculo);
        } else {
            Principal.getInstancia().log("Nao é possível persistir uma instancia nula...", "VEÍCULO");
        }

        return veiculo;
    }

    private IVeiculo inserir(IVeiculo veiculo) {
        String sql = ("INSERT INTO `veiculos` ( `id`, `clienteID`, `placa`, `descricao`, `saida` ) VALUES ( NULL, ?, ?, ?, NULL )");
        Object[] params = new Object[]{veiculo.getDono().getId(), veiculo.getPlaca(), veiculo.getDescricao()};
        veiculo.setId(Principal.getInstancia().gerenciarTransacao(sql, params));
        return veiculo;
    }

    private IVeiculo atualizar(IVeiculo veiculo) {
        String sql = ("UPDATE `veiculos` SET `placa` = ?, `descricao` = ?, `saida` = ? WHERE ( `id` = ? )");
        Object[] params = new Object[]{veiculo.getDono().getId(), veiculo.getPlaca(), veiculo.getDescricao(), veiculo.getId()};
        Principal.getInstancia().gerenciarTransacao(sql, params);
        return veiculo;
    }

    private IVeiculo construir(QueryResult resultado, ICliente dono, boolean fechar) {
        IVeiculo veiculo = null;
        if ((resultado != null)) {
            if ((dono != null) && (dono.getId() > 0)) {
                veiculo = new Veiculo(dono, resultado.getInt("id"), resultado.getString("placa"), resultado.getString("descricao"), resultado.getString("entrada"), resultado.getString("saida"));
                Principal.getInstancia().log(new StringBuilder().append("Construindo o VEICULO com PLACA '").append(veiculo.getPlaca()).append("' e ID: ").append(veiculo.getId()).append(".").toString(), "VEÍCULO");
            } else {
                Principal.getInstancia().log("O objeto 'dono' ainda não foi persistido no banco de dados.", "VEICULO");
            }

            if ((fechar)) {
                resultado.close();
            }
        }

        return veiculo;
    }

    public IVeiculo recuperar(int id) {
        Principal.getInstancia().log(new StringBuilder().append("Recuperando VEICULO com ID '").append(id).append("' no banco de dados.").toString(), "VEÍCULO");
        QueryResult resultado = Conectar.getInstancia().getJdbc().query("SELECT * FROM `veiculos` WHERE ( `id` = ? )", new Object[]{id});
        IVeiculo veiculo = null;

        if ((resultado.next())) {
            ControlarCliente controle = new ControlarCliente();
            ICliente dono = controle.recuperar(resultado.getInt("clienteID"));
            veiculo = this.construir(resultado, dono, true);
        }

        return veiculo;
    }

    public Map<Long, IVeiculo> recuperarPeloDono(int idDono) {
        Map<Long, IVeiculo> veiculos = new HashMap();

        if ((idDono > 0)) {
            ControlarCliente controle = new ControlarCliente();
            ICliente dono = controle.recuperar(idDono);
            veiculos = this.recuperarPeloDono(dono);
        }

        return veiculos;
    }

    public Map<Long, IVeiculo> recuperarPeloDono(ICliente dono) {
        Map<Long, IVeiculo> veiculos = new HashMap();
        
        if ((dono != null) && (dono.getId() > 0)) {
            QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT * FROM `veiculos` WHERE ( `clienteID` = ? )", new Object[]{dono.getId()});
            while (resultados.next()) {
                IVeiculo veiculo = this.construir(resultados, dono, false);
                if ((veiculo != null)) {
                    veiculos.put(veiculo.getId(), veiculo);
                }
            }

            resultados.close();
        } else {
            Principal.getInstancia().log("Não é possível recuperar o veículo, pois, o dono não existe.", "VEICULO");
        }

        return veiculos;
    }

}
