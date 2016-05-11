package projetos.gerencia.apresentacao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import projetos.gerencia.exceptions.ComprarException;
import projetos.gerencia.negocio.cliente.ICliente;
import projetos.gerencia.negocio.veiculo.IOrcamento;
import projetos.gerencia.negocio.veiculo.IVeiculo;
import projetos.gerencia.negocio.veiculo.Orcamento;
import projetos.gerencia.negocio.veiculo.Veiculo;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("ui/FormLogin.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("LOGIN");

        stage.setMaxWidth(320);
        stage.setMaxHeight(315);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("ui/icone.png")));
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        ControlarVeiculo controleVeiculo = new ControlarVeiculo();
        ControlarCliente controleCliente = new ControlarCliente();

        ICliente dono = controleCliente.recuperar(1);
        
        // Coloquei ID 1 no construtor só pra facilitar minha vida, só um exemplo no caso...
        IVeiculo veiculo = new Veiculo(dono, 1, "PDF-0142", "Falta de gasolina ressecou a bomba.", null, null);
        controleVeiculo.salvar(veiculo);
        
        ControlarProduto controleProduto = new ControlarProduto();
        IOrcamento orc1 = new Orcamento(veiculo, controleProduto.recuperar(1), 0, 4, null);
        try {
            veiculo.getPersistencia().adicionarProduto(orc1);
        } catch (ComprarException ex) {
        }
        
        IOrcamento orc2 = new Orcamento(veiculo, controleProduto.recuperar(4), 0, 1, null);
        try {
            veiculo.getPersistencia().adicionarProduto(orc2);
        } catch (ComprarException ex) {
        }
    }

}
