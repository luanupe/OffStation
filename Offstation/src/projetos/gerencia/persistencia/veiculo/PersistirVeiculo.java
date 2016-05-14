package projetos.gerencia.persistencia.veiculo;

import java.util.HashMap;
import java.util.Map;
import jdbchelper.JdbcException;
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
            Principal.getInstancia().log("Criando instancia de persistencia do produto...");
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
            Principal.getInstancia().log("Nao é possível persistir uma instancia nula...");
        }

        return veiculo;
    }

    private IVeiculo inserir(IVeiculo veiculo) {
        try {
            Conectar.getInstancia().getJdbc().beginTransaction();
            Conectar.getInstancia().getJdbc().execute("INSERT INTO `veiculos` ( `id`, `clienteID`, `placa`, `descricao`, `saida` ) VALUES ( NULL, ?, ?, ?, NULL )",
                    new Object[]{veiculo.getDono().getId(), veiculo.getPlaca(), veiculo.getDescricao()});
        } catch (JdbcException error) {
            if ((Conectar.getInstancia().getJdbc().isInTransaction())) {
                Conectar.getInstancia().getJdbc().rollbackTransaction();
            }
            throw error;
        } finally {
            if ((Conectar.getInstancia().getJdbc().isInTransaction())) {
                veiculo.setId(Conectar.getInstancia().getJdbc().getLastInsertId());
                Conectar.getInstancia().getJdbc().commitTransaction();
            }
        }

        return veiculo;
    }

    private IVeiculo atualizar(IVeiculo veiculo) {
        try {
            Conectar.getInstancia().getJdbc().beginTransaction();
            Conectar.getInstancia().getJdbc().execute("UPDATE `veiculos` SET `placa` = ?, `descricao` = ?, `saida` = ? WHERE ( `id` = ? )",
                    new Object[]{veiculo.getDono().getId(), veiculo.getPlaca(), veiculo.getDescricao(), veiculo.getId()});
        } catch (JdbcException error) {
            if ((Conectar.getInstancia().getJdbc().isInTransaction())) {
                Conectar.getInstancia().getJdbc().rollbackTransaction();
            }
            throw error;
        } finally {
            if ((Conectar.getInstancia().getJdbc().isInTransaction())) {
                Conectar.getInstancia().getJdbc().commitTransaction();
            }
        }

        return veiculo;
    }

    private IVeiculo construir(QueryResult resultado, ICliente dono, boolean fechar) {
        IVeiculo veiculo = null;
        if ((resultado != null)) {
            if ((dono != null) && (dono.getId() > 0)) {
                veiculo = new Veiculo(dono, resultado.getInt("id"), resultado.getString("placa"), resultado.getString("descricao"), resultado.getString("entrada"), resultado.getString("saida"));
            } else {
                Principal.getInstancia().log("O objeto 'dono' ainda não foi persistido no banco de dados.");
            }

            if ((fechar)) {
                resultado.close();
            }
        }

        return veiculo;
    }

    public IVeiculo recuperar(int id) {
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
        if ((dono != null)) {
            QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT * FROM `veiculos` WHERE ( `clienteID` = ? )", new Object[]{dono.getId()});
            while (resultados.next()) {
                IVeiculo veiculo = this.construir(resultados, dono, false);
                if ((veiculo != null)) {
                    veiculos.put(veiculo.getId(), veiculo);
                    Principal.getInstancia().log(new StringBuilder().append("Veículo '").append(veiculo.getPlaca()).append("' de id '").append(veiculo.getId()).append("' adicionado com sucesso.").toString(), "LISTAR");
                }
            }

            resultados.close();
        } else {
            Principal.getInstancia().log("Não é possível recuperar o veículo, pois, o dono não existe.");
        }

        return veiculos;
    }

}
