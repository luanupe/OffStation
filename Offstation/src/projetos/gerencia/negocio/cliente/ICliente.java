package projetos.gerencia.negocio.cliente;

public interface ICliente {

    public long getId();

    public void setId(long id);

    public String getNome();

    public String getSobrenome();

    public String getNomeCompleto();

    public String getEmail();

}
