package projetos.gerencia.negocio.cliente;

public final class Cliente implements ICliente {

    private long id;
    private String nome;
    private String sobrenome;
    private String email;

    public Cliente(String nome, String sobrenome, String email) {
        this(0, nome, sobrenome, email);
    }

    public Cliente(long id, String nome, String sobrenome, String email) {
        this.setId(id);
        this.setNome(nome);
        this.setSobrenome(sobrenome);
        this.setEmail(email);
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
    public String getNome() {
        return this.nome;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getSobrenome() {
        return this.sobrenome;
    }

    private void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    @Override
    public String getNomeCompleto() {
        String nomeCompleto = new StringBuffer().append(this.getNome()).append(" ").append(this.getSobrenome()).toString();
        return nomeCompleto;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
