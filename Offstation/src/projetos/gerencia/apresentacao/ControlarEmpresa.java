package projetos.gerencia.apresentacao;

import java.util.List;
import projetos.gerencia.persistencia.empresa.Financa;
import projetos.gerencia.persistencia.empresa.PersistirEmpresa;

public class ControlarEmpresa {

    public List<Financa> getFinancas() {
        return PersistirEmpresa.getInstancia().getOrcamentos();
    }

    public List<Financa> getFinancas(int ano, int de, int para) {
        return PersistirEmpresa.getInstancia().getOrcamentos(ano, de, para);
    }

}
