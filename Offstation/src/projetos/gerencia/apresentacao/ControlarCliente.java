package projetos.gerencia.apresentacao;

import java.util.Map;
import jdbchelper.JdbcException;
import projetos.gerencia.Principal;
import projetos.gerencia.exceptions.ClienteException;
import projetos.gerencia.negocio.cliente.Cliente;
import projetos.gerencia.negocio.cliente.ICliente;
import projetos.gerencia.persistencia.cliente.PersistirCliente;

public final class ControlarCliente {

    public void salvar(long id, String nome, String sobrenome, String email) {
        this.salvar(new Cliente(id, nome, sobrenome, email));
    }

    public ICliente salvar(ICliente cliente) {
        try {
            PersistirCliente.getInstancia().salvar(cliente);
            Principal.getInstancia().log(new StringBuilder().append("Cliente '").append(cliente.getNomeCompleto()).append("' salvo com sucesso! Novo ID: ").append(cliente.getId()).toString(), "CLIENTE");
        } catch (JdbcException error) {
            Principal.getInstancia().log(new StringBuilder().append("Cliente '").append(cliente.getNomeCompleto()).append("' não pode ser salvo. Erro: ").append(error.getMessage()).toString(), "CLIENTE");
            throw (new ClienteException("Não foi possível salvar o cliente. Tente novamente mais tarde."));
        }
        return cliente;
    }

    public boolean remover(ICliente produto) {
        return PersistirCliente.getInstancia().remover(produto);
    }

    public boolean remover(int id) {
        return PersistirCliente.getInstancia().remover(id);
    }

    public ICliente recuperar(int id) {
        return PersistirCliente.getInstancia().recuperar(id);
    }

    public ICliente recuperar(String nome) {
        return PersistirCliente.getInstancia().recuperar(nome);
    }

    public Map<Long, ICliente> recuperarTodos(String nome) {
        return PersistirCliente.getInstancia().recuperarTodos(nome);
    }

    public Map<Long, ICliente> recuperarTodos() {
        return PersistirCliente.getInstancia().recuperarTodos();
    }

}
