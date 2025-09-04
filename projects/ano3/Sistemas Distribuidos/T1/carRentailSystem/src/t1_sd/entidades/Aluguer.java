/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.entidades;

import java.io.Serializable;

/**
 *
 * @author gustavooliveira
 */


public class Aluguer implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private int id ;
    private Veiculo veiculo ;
    private Cliente cliente ;  
    private double valor ; 
    private String dataFim ;
    private String dataInc ; 
    
    public Aluguer (int id, Veiculo veiculo, Cliente cliente, double valor, String dataFim, String dataInc)
    {  
        this.id = id ;
        this.veiculo = veiculo ;
        this.cliente = cliente ;
        this.valor = valor ;
        this.dataFim = dataFim;
        this.dataInc = dataInc ;    
    }
    

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }

    public Veiculo getVeiculo() 
    {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) 
    {
        this.veiculo = veiculo;
    }

    public Cliente getCliente() 
    {
        return cliente;
    }

    public void setCliente(Cliente cliente) 
    {
        this.cliente = cliente;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDataInicio() {
        return dataInc;
    }

    public void setDataInicio(String dataInicio) 
    {
        this.dataInc = dataInicio;
    }

    public String getDataFim() 
    {
        return dataFim;
    }

    public void setDataFim(String dataFim) 
    {
        this.dataFim = dataFim;
    }


    @Override
    public String toString() 
    {
        return "Aluguer{" +
                "id=" + id +
                ", veiculo=" + veiculo +
                ", cliente=" + cliente +
                ", valor=" + valor +
                ", dataInicio='" + dataInc + '\'' +
                ", dataFim='" + dataFim + '\'' +
                '}';
    } 
    
    
}
