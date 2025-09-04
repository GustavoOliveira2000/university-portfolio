/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.servidor;

import t1_sd.entidades.Cliente;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import t1_sd.remoto.ClienteServiceRemote;
import java.sql.Connection;
import t1_sd.database.PostgresConnect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author gustavooliveira
 */

public class ClienteServiceImpl extends UnicastRemoteObject  implements ClienteServiceRemote
{
   
    private final Connection connection;
    
    
    public ClienteServiceImpl (PostgresConnect pgConnect) throws RemoteException 
    {
       this.connection = pgConnect.getConnection();
    }
    
    @Override
    public void registrarCliente(Cliente client) throws RemoteException 
    {
        
        try 
        {
            
            String sql = "INSERT INTO Cliente (nome, documento, contato) VALUES (?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, client.getNome());
            stmt.setString(2, client.getDocumento());
            stmt.setString(3, client.getContacto());
            stmt.executeUpdate();
            System.out.println("Cliente registrado com sucesso: " + client.getNome());
        
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        System.out.println("Cliente adicionando : " + client.getNome());

    }

    @Override
    public Cliente buscarPorId(int id)  throws RemoteException 
    {
    
        Cliente cliente = null;
        try {
            
            String sql = "SELECT * FROM Cliente WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) 
            {
                cliente = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("documento"),
                    rs.getString("contato")
            );
                
            } else 
            {
                
                System.out.println("Cliente com ID " + id + " não encontrado.");
            
            }
            } catch (SQLException e) 
            {
                e.printStackTrace();
                System.out.println("Erro ao buscar cliente pelo ID.");
            
            }
        
        return cliente;
    }

    @Override
    public List<Cliente> listarClientes() throws RemoteException 
    {
        List<Cliente> clientes = new ArrayList<>();
        
        try 
        {
            String sql = "SELECT * FROM Cliente";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) 
            {
                clientes.add(new Cliente(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("documento"),
                    rs.getString("contato")
                ));
            }
        
        } catch (SQLException e) 
        {
            
            e.printStackTrace();
        
        }
        
        return clientes;      
    }
    
}
