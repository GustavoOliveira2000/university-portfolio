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
public class Cliente implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    private int id ;
    private String nome ;
    private String documento ;
    private String contacto ;
    
    public Cliente (int id, String nome, String documento, String contacto)
    {
        this.id = id ;
        this.nome = nome ;
        this.documento = documento ;
        this.contacto = contacto ;
        
    }
    
    public int getId()
    {
        return id ;
    }
    
    public void setId( int id )
    {
        this.id = id ;
    }
    
    public String getNome ()
    {
        return nome ;
    }
    
    public void setNome(String Nome)
    {
        this.nome = Nome ;
    }
    
    public String getDocumento ()
    {
        return documento ;
    }
    
    public void setDocumento(String documento)
    {
        this.documento = documento ;
    }
    
    public String getContacto ()
    {
        return contacto ;
    }
    
    public void setContacto(String contacto)
    {
        this.contacto = contacto ;
    }
    
    
    @Override
    public String toString()
    {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", documento='" + documento + '\'' +
                ", contato='" + contacto + '\'' +
                '}';
    
    }
    
    
}
 