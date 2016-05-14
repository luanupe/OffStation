package projetos.gerencia;

import jdbchelper.JdbcException;
import projetos.gerencia.apresentacao.ControlarFuncionario;
import projetos.gerencia.exceptions.LoginException;
import projetos.gerencia.negocio.funcionario.IFuncionario;
import projetos.gerencia.persistencia.Conectar;

public final class Principal {

    private static Principal INSTANCIA;

    private IFuncionario funcionario;
    private boolean logs;

    private Principal() {
        this.setLogs(true);
    }

    public static Principal getInstancia() {
        if ((Principal.INSTANCIA == null)) {
            Principal.INSTANCIA = new Principal();
        }
        return Principal.INSTANCIA;
    }

    public void fazerLogin(String cpf, String senha) throws LoginException {
        if ((this.getFuncionario() == null)) {
            try {
                ControlarFuncionario controlar = new ControlarFuncionario();
                IFuncionario conectar = controlar.recuperar(Integer.parseInt(cpf));

                if ((conectar != null) && (conectar.getSenha().equals(senha))) {
                    if ((conectar.getTipo() <= 0)) {
                        this.log(LoginException.SEM_PERMISSAO, "LOGIN");
                        throw new LoginException(LoginException.SEM_PERMISSAO, LoginException.SEM_PERMISSAO_ID);
                    } else {
                        this.log(new StringBuilder().append("Conectado com sucesso como: '").append(conectar.getNomeCompleto()).append("'").toString(), "LOGIN");
                        this.setFuncionario(conectar);
                    }
                } else {
                    this.log(LoginException.NAO_ENCONTRADO, "LOGIN");
                    throw new LoginException(LoginException.NAO_ENCONTRADO, LoginException.NAO_ENCONTRADO_ID);
                }
            } catch (JdbcException error) {
                this.log(LoginException.SEM_CONEXOES, "LOGIN");
                throw new LoginException(LoginException.SEM_CONEXOES, LoginException.SEM_CONEXOES_ID);
            } catch (NumberFormatException error) {
                this.log(LoginException.CPF_INVALIDO, "LOGIN");
                throw new LoginException(LoginException.CPF_INVALIDO, LoginException.CPF_INVALIDO_ID);
            }
        } else {
            this.log(LoginException.SESSAO_ATIVA, "LOGIN");
            throw new LoginException(LoginException.SESSAO_ATIVA, LoginException.SESSAO_ATIVA_ID);
        }
    }

    public boolean fazerLogout() {
        boolean sair = false;
        IFuncionario conectado = this.getFuncionario();

        if ((conectado == null)) {
            this.log("Você não está conectado.", "LOGOUT");
        } else {
            sair = true;
            this.log("Usuário desconectado! Sessão finalizada.", "LOGOUT");
            this.setFuncionario(null);
        }

        return sair;
    }

    public void log(String mensagem) {
        this.log(mensagem, "SISTEMA");
    }

    public void log(String mensagem, String tipo) {
        if ((this.isLogs())) {
            tipo = tipo.toUpperCase();
            System.out.println(new StringBuilder().append(tipo).append(" > ").append(mensagem));
        }
    }

    public boolean isLogs() {
        return this.getLogs();
    }

    private boolean getLogs() {
        return this.logs;
    }

    private void setLogs(boolean logs) {
        this.logs = logs;
    }

    public IFuncionario getFuncionario() {
        return this.funcionario;
    }

    private void setFuncionario(IFuncionario funcionario) {
        this.funcionario = funcionario;
    }

    public long gerenciarTransacao(String sql, Object[] params) {
        long retornar = 0;

        try {
            Conectar.getInstancia().getJdbc().beginTransaction();
            Conectar.getInstancia().getJdbc().execute(sql, params);
        } catch (JdbcException error) {
            if ((Conectar.getInstancia().getJdbc().isInTransaction())) {
                Conectar.getInstancia().getJdbc().rollbackTransaction();
            }
            throw error;
        } finally {
            if ((Conectar.getInstancia().getJdbc().isInTransaction())) {
                retornar = Conectar.getInstancia().getJdbc().getLastInsertId();
                Conectar.getInstancia().getJdbc().commitTransaction();
            }
        }

        return retornar;
    }

}
