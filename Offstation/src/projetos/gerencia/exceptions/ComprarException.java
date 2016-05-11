package projetos.gerencia.exceptions;

public class ComprarException extends Exception {

    private String mensagem;
    
    public ComprarException(String mensagem) {
        super(mensagem);
        this.setMensagem(mensagem);
    }

    public String getMensagem() {
        return this.mensagem;
    }

    private void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
