package projetos.gerencia.persistencia.cliente;

import java.util.HashMap;
import java.util.Map;
import jdbchelper.QueryResult;
import projetos.gerencia.Principal;
import projetos.gerencia.negocio.cliente.Cliente;
import projetos.gerencia.negocio.cliente.ICliente;
import projetos.gerencia.persistencia.Conectar;

public class PersistirCliente {

    private static PersistirCliente INSTANCIA = null;

    private PersistirCliente() {
    }

    public static PersistirCliente getInstancia() {
        if ((PersistirCliente.INSTANCIA == null)) {
            Principal.getInstancia().log("Criando instancia de persistencia do cliente...");
            PersistirCliente.INSTANCIA = new PersistirCliente();
        }

        return PersistirCliente.INSTANCIA;
    }

    public ICliente salvar(ICliente cliente) {
        if ((cliente != null)) {
            if ((cliente.getId() > 0)) {
                cliente = this.atualizar(cliente);
            }
            cliente = this.inserir(cliente);
        } else {
            Principal.getInstancia().log("Nao é possível salvar uma instancia nula...", "CLIENTE");
        }

        return cliente;
    }

    private ICliente inserir(ICliente cliente) {
        String sql = ("INSERT INTO `clientes` ( `id`, `nome`, `sobrenome`, `email` ) VALUES ( NULL, ?, ?, ? )");
        Object[] params = new Object[]{cliente.getNome(), cliente.getSobrenome(), cliente.getEmail()};
        cliente.setId(Principal.getInstancia().gerenciarTransacao(sql, params));
        return cliente;
    }

    private ICliente atualizar(ICliente cliente) {
        String sql = ("UPDATE `clientes` SET ( `nome` = ? ), ( `sobrenome` = ? ), ( `email` = ? ) WHERE ( `id` = ? )");
        Object[] params = new Object[]{cliente.getNome(), cliente.getSobrenome(), cliente.getEmail()};
        Principal.getInstancia().gerenciarTransacao(sql, params);
        return cliente;
    }

    public boolean remover(ICliente cliente) {
        if ((cliente != null) && (cliente.getId() > 0)) {
            return (this.remover(cliente.getId()));
        }
        return false;
    }

    public boolean remover(long id) {
        return (Conectar.getInstancia().getJdbc().execute("DELETE FROM `clientes` WHERE ( `id` = ? )", new Object[]{id}) == 1);
    }

    private ICliente construir(QueryResult resultado, boolean fechar) {
        ICliente cliente = null;
        if ((resultado != null)) {
            cliente = new Cliente(resultado.getInt("id"), resultado.getString("nome"), resultado.getString("sobrenome"), resultado.getString("email"));
            Principal.getInstancia().log(new StringBuilder().append("Construindo o CLIENTE com nome '").append(cliente.getNomeCompleto()).append("' e ID ").append(cliente.getId()).append(".").toString(), "PRODUTO");
            if ((fechar)) {
                resultado.close();
            }
        }
        return cliente;
    }

    public ICliente recuperar(int id) {
        Principal.getInstancia().log(new StringBuilder().append("Recuperando CLIENTE com ID '").append(id).append("' no banco de dados.").toString(), "CLIENTE");
        QueryResult resultado = Conectar.getInstancia().getJdbc().query("SELECT * FROM `clientes` WHERE ( `id` = ? )", new Object[]{id});
        ICliente cliente = null;

        if ((resultado.next())) {
            cliente = this.construir(resultado, true);
        }

        return cliente;
    }

    public ICliente recuperar(String nome) {
        Principal.getInstancia().log(new StringBuilder().append("Recuperando CLIENTE com NOME '").append(nome).append("' no banco de dados.").toString(), "CLIENTE");
        QueryResult resultado = Conectar.getInstancia().getJdbc().query("SELECT * FROM `clientes` WHERE ( `nome` = ? )", new Object[]{nome});
        ICliente cliente = null;

        if ((resultado.next())) {
            cliente = this.construir(resultado, true);
        }

        return cliente;
    }

    private Map<Long, ICliente> recuperarTodos(QueryResult resultados) {
        Map<Long, ICliente> clientes = new HashMap();
        while (resultados.next()) {
            ICliente cliente = this.construir(resultados, false);
            clientes.put(cliente.getId(), cliente);
        }
        return clientes;
    }

    public Map<Long, ICliente> recuperarTodos(String nome) {
        Principal.getInstancia().log(new StringBuilder().append("Recuperando TODOS OS CLIENTES que contenha '").append(nome).append("' no nome.").toString(), "CLIENTE");
        QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT * FROM `clientes` WHERE ( `nome` = ? ) OR ( `nome` LIKE ? )", new Object[]{nome, (new StringBuilder().append("%").append(nome).append("%").toString())});
        Map<Long, ICliente> clientes = this.recuperarTodos(resultados);
        resultados.close();
        return clientes;
    }

    public Map<Long, ICliente> recuperarTodos() {
        Principal.getInstancia().log("Recuperando TODOS OS CLIENTES no banco de dados.", "CLIENTE");
        QueryResult resultados = Conectar.getInstancia().getJdbc().query("SELECT * FROM `clientes`");
        Map<Long, ICliente> clientes = this.recuperarTodos(resultados);
        resultados.close();
        return clientes;
    }

}
