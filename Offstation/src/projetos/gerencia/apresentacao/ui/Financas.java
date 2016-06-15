/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetos.gerencia.apresentacao.ui;

/**
 *
 * @author Sostenes
 */
public class Financas {

    private String descricao;
    private int quantidade;
    private double preco;
    private double total;

    public Financas(String descricao, int quantidade, double preco, double total) {
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.preco = preco;
        this.total = total;
    }
    
    public Financas(Financas f) {
        this.descricao = f.getDescricao();
        this.quantidade = f.getQuantidade();
        this.preco = f.getPreco();
        this.total = f.getTotal();
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
 
   
    
}
