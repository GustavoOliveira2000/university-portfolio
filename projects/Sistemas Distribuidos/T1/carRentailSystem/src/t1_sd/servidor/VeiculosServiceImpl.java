/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.servidor;
import t1_sd.entidades.Veiculo;
import t1_sd.remoto.VeiculoServiceRemote;
import t1_sd.database.PostgresConnect;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gustavooliveira
 */


public class VeiculosServiceImpl extends UnicastRemoteObject implements VeiculoServiceRemote
{
    
    private final Connection connection;

    public VeiculosServiceImpl (PostgresConnect pgConnect) throws RemoteException 
    {
        this.connection = pgConnect.getConnection();
        
    }
    
    public void adicionarVeiculo(Veiculo veiculo) throws RemoteException 
    {
        
        try 
        {
            String sql = "INSERT INTO Veiculo (matricula, modelo, tipo, localizacao, disponivel, estado_adm) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            
            stmt.setString(1, veiculo.getMatricula());
            stmt.setString(2, veiculo.getModelo());
            stmt.setString(3, veiculo.getTipo());
            stmt.setString(4, veiculo.getLocalizacao());
            stmt.setBoolean(5, veiculo.estadoAlg());
            stmt.setBoolean(6, veiculo.estadoAdm());
            stmt.executeUpdate();
            
            System.out.println("Veículo adicionado: " + veiculo.getModelo());
            
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
                  
    }
    
    @Override
    public void removerVeiculo(String matricula) throws RemoteException 
    {
        try {
            
            String sql = "DELETE FROM Veiculo WHERE matricula = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, matricula);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) 
            {
                System.out.println("Veículo com matrícula " + matricula + " removido com sucesso.");
            
            } else 
            {
                System.out.println("Erro: Veículo com matrícula " + matricula + " não encontrado.");
            }
        
        } catch (SQLException e) 
        {
           
            e.printStackTrace();
            System.out.println("Erro ao remover o veículo com matrícula " + matricula + ".");
    
        }
    }
         
        @Override
    public List<Veiculo> listVeiculos() throws RemoteException 
    {
        List<Veiculo> veiculos = new ArrayList<>();
        try 
        {
            String sql = "SELECT * FROM Veiculo";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                veiculos.add(new Veiculo(
                        rs.getString("matricula"),
                        rs.getString("modelo"),
                        rs.getString("tipo"),
                        rs.getString("localizacao"),
                        rs.getBoolean("estado_adm"),
                        rs.getBoolean("disponivel")
                ));
            }
            
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
        return veiculos;
    }
    
    public List<Veiculo> listarVeiculosDisponiveis(String localizacao, String tipo) throws RemoteException 
    {
        List<Veiculo> veiculos = new ArrayList<>();
        try 
        {
            String sql = "SELECT * FROM Veiculo WHERE disponivel = TRUE AND estado_adm = TRUE";
            if (localizacao != null && !localizacao.isEmpty()) 
            {
                sql += " AND localizacao = ?";
            }
            if (tipo != null && !tipo.isEmpty()) 
            {
                sql += " AND tipo = ?";
            }

            PreparedStatement stmt = connection.prepareStatement(sql);
            int paramIndex = 1;
            
            if (localizacao != null && !localizacao.isEmpty()) 
            {
                
                stmt.setString(paramIndex++, localizacao);
            
            }
            if (tipo != null && !tipo.isEmpty()) 
            {
                
                stmt.setString(paramIndex, tipo);
            
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                veiculos.add(new Veiculo(
                        rs.getString("matricula"),
                        rs.getString("modelo"),
                        rs.getString("tipo"),
                        rs.getString("localizacao"),
                        rs.getBoolean("estado_adm"),
                        rs.getBoolean("disponivel")
                ));
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return veiculos ;
    }
    
    public List<String> listarLocalizacoesDeVeiculosAlugados() throws RemoteException 
    {
        
        List<String> localizacoes = new ArrayList<>();
        
        try {
            
            String sql = "SELECT DISTINCT localizacao FROM Veiculo WHERE disponivel = FALSE";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
           while (rs.next()) {
                localizacoes.add(rs.getString("localizacao"));
            }
        
        } catch (SQLException e) 
        {
            
            e.printStackTrace();
        
        }
        return localizacoes;   
   
    }
    
    public void aprovarVeiculo(String matricula) throws RemoteException 
    {
        
        List<Veiculo> veiculos = listVeiculos();
        
        for (Veiculo veiculo : veiculos) 
        {
            if (veiculo.getMatricula().equals(matricula)) 
            {
                veiculo.setEstadoAdm(true);
                try 
                {
                    String sql = "UPDATE Veiculo SET estado_adm = TRUE WHERE matricula = ?";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, matricula);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) 
                    {
                        System.out.println("Veículo aprovado: " + matricula);
                    
                    } else 
                    {
                        
                        System.out.println("Erro: Veículo com matrícula " + matricula + " não encontrado.");
                    
                    }
            } catch (SQLException e) 
            {
                e.printStackTrace();
            }
  
                System.out.println("Veículo aprovado: " + matricula);
                return; 
            }
        }
        
        System.out.println("Erro: Veículo com matrícula " + matricula + " não encontrado.");
    
    }

     public List<Veiculo> listarVeiculosPorEstado(boolean aprovado) throws RemoteException 
      {
          List<Veiculo> veiculos = new ArrayList<>();
            
            try {
                
                String sql = "SELECT * FROM Veiculo WHERE estado_adm = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setBoolean(1, aprovado);
                ResultSet rs = stmt.executeQuery();
                
                while (rs.next()) 
                {
                
                    veiculos.add(new Veiculo(
                            rs.getString("matricula"),
                            rs.getString("modelo"),
                            rs.getString("tipo"),
                            rs.getString("localizacao"),
                            rs.getBoolean("estado_adm"),
                            rs.getBoolean("disponivel")
                    ));
                
                }
                } catch (SQLException e) 
                {
                    e.printStackTrace();
                }
        
            return veiculos;
   
      }
      
     public Veiculo buscarVeiculoPorMatricula(String matricula) throws RemoteException
      {
            Veiculo veiculo = null;
            
            try {
                String sql = "SELECT * FROM Veiculo WHERE matricula = ?";
                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, matricula);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) 
                {
                
                    veiculo = new Veiculo(
                        rs.getString("matricula"),
                        rs.getString("modelo"),
                        rs.getString("tipo"),
                        rs.getString("localizacao"),
                        rs.getBoolean("estado_adm"),
                        rs.getBoolean("disponivel")
                );
                
                }
            
            } catch (SQLException e) 
            {
            
                e.printStackTrace();
            
            }
        
            return veiculo;
            
      }

}
