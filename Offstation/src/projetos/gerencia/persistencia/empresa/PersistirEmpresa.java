package projetos.gerencia.persistencia.empresa;

import java.util.ArrayList;
import java.util.List;
import jdbchelper.QueryResult;
import projetos.gerencia.Principal;
import projetos.gerencia.persistencia.Conectar;

public class PersistirEmpresa {

    private static PersistirEmpresa INSTANCIA;

    private PersistirEmpresa() {
    }

    public static PersistirEmpresa getInstancia() {
        if ((PersistirEmpresa.INSTANCIA == null)) {
            Principal.getInstancia().log("Criando instancia de persistencia de finanças...");
            PersistirEmpresa.INSTANCIA = new PersistirEmpresa();
        }
        return PersistirEmpresa.INSTANCIA;
    }

    private void construirFinanca(QueryResult resultado, List<Financa> orcamentos) {
        Financa financa = new Financa(resultado);
        if ((financa.getDescricao() != null)) {
            Principal.getInstancia().log("CONSTRUÍNDO FINANÇA -> " + financa.getDescricao());
            orcamentos.add(financa);
        }
    }

    public List<Financa> getOrcamentos() {
        List<Financa> orcamentos = new ArrayList();
        QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT SUM(`quantidade`) AS `Total`, `pecaID` FROM `orcamento` `O` GROUP BY `pecaID`");

        while (resultados.next()) {
            this.construirFinanca(resultados, orcamentos);
        }
        return orcamentos;
    }

    public List<Financa> getOrcamentos(int ano, int de, int para) {
        List<Financa> orcamentos = new ArrayList();
        QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT SUM(`quantidade`) AS `Total`, `pecaID` FROM `orcamento` `O` WHERE ( `O`.`data` BETWEEN ? AND ? ) GROUP BY `pecaID`", new Object[]{(ano + "-" + de + "-01"), (ano + "-" + de + "-31")});

        while (resultados.next()) {
            this.construirFinanca(resultados, orcamentos);
        }
        return orcamentos;
    }

}
