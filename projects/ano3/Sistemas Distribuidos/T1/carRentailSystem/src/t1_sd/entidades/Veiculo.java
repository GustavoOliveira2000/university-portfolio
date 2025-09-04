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
public class Veiculo implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    private String matricula;
    private String tipo;
    private String modelo ;
    private String localizacao ;
    private Boolean estadoAdm ;
    private Boolean estadoAlg ;
    
    public Veiculo (String matricula, String tipo, String modelo, String localizacao,Boolean estadoAdm, Boolean estadoAlg)
    {
        
        this.matricula = matricula ;
        this.tipo = tipo ;
        this.modelo = modelo ;
        this.localizacao = localizacao ;
        this.estadoAdm = estadoAdm ; 
        this.estadoAlg = estadoAlg ; // disponivel - true , indisponivel - false .
    
    }
    

    
    public String getMatricula() 
    {
        return matricula;
    }

    public void setMatricula(String matricula) 
    {
        this.matricula = matricula;
    }

    public String getModelo() 
    {
        return modelo;
    }

    public void setModelo(String modelo) 
    {
        this.modelo = modelo;
    }

    public String getTipo() 
    {
        return tipo;
    }

    public void setTipo(String tipo) 
    {
        this.tipo = tipo;
    }

    public String getLocalizacao() 
    {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) 
    {
        this.localizacao = localizacao;
    }

    public boolean estadoAlg() // return true - veiculo disponivel -  return false - alugado e manutenção  
    {
        return estadoAlg;
    }

    public void setEstadoAlg(boolean estadoAlg) 
    {
        this.estadoAlg = estadoAlg;
    }

    public boolean estadoAdm() 
    {
        return estadoAdm;
    }

    public void setEstadoAdm(boolean aprovado) 
    {
        
        this.estadoAdm = aprovado;
    }

    // Método toString para exibir informações do veículo
    @Override
    public String toString() 
    {
        return "Veiculo{" +
                "matricula='" + matricula + '\'' +
                ", modelo='" + modelo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", localizacao='" + localizacao + '\'' +
                ", disponivel=" + estadoAlg +
                ", aprovado=" + estadoAdm +
                '}';
    }
    
    
}
