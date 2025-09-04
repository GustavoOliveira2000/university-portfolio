/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import t1_sd.database.PostgresConnect;
import t1_sd.entidades.Aluguer;
import t1_sd.entidades.Cliente;
import t1_sd.entidades.Veiculo;
import t1_sd.remoto.AluguerServiceRemote;
import t1_sd.remoto.ClienteServiceRemote;
import t1_sd.remoto.VeiculoServiceRemote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gustavooliveira
 */

public class AluguerServiceImpl extends UnicastRemoteObject  implements AluguerServiceRemote
{   

    private VeiculoServiceRemote veiculoService;
    private ClienteServiceRemote clienteService;
    private Connection connection;
    
    public AluguerServiceImpl(PostgresConnect pgConnect, VeiculoServiceRemote veiculoService, ClienteServiceRemote clienteService ) throws RemoteException 
    {
        this.veiculoService = veiculoService;
        this.clienteService = clienteService ;
        this.connection =  pgConnect.getConnection();

    }
    
    @Override
    public void registrarAluguer(Aluguer aluguer) throws RemoteException 
    {   
       List<Veiculo> veiculos = veiculoService.listVeiculos();
       List<Cliente> clientes = clienteService.listarClientes();
       
       Veiculo veiculo = veiculos.stream().filter(v -> v.getMatricula().equals(aluguer.getVeiculo().getMatricula())).findFirst().orElse(null);
       
       if(veiculo == null)
       {
           
           System.out.println("Erro - veiculo não encontrado");
           return;
       
       } else if( veiculo.estadoAlg() == false)
       {
           
           System.out.println("Erro - veiculo alugado");
           return;
       
       }else if( veiculo.estadoAdm() == false)
       {
           
           System.out.println("Erro - veiculo não aprovado");
           return;
       
       }
       
       Cliente cliente = clientes.stream().filter( c -> c.getId() == aluguer.getCliente().getId() ).findFirst().orElse(null);
       
       if(cliente == null)
       {
           System.out.println("Erro - cliente não encontrado");
           return;
       }
       
       // Atualizar o estado do veículo para indisponível
        
        veiculoService.removerVeiculo(aluguer.getVeiculo().getMatricula());
        aluguer.getVeiculo().setEstadoAlg(false);
        aluguer.getVeiculo().setEstadoAdm(true);
        veiculoService.adicionarVeiculo(aluguer.getVeiculo()); // adicionar o veiculo outra vez a data base , n vai duplicar ?? 

       

       
       try 
       {
            String sql = "INSERT INTO Aluguer (id_cliente, matricula_veiculo, valor, data_inicio, data_fim) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, aluguer.getCliente().getId());
            stmt.setString(2, aluguer.getVeiculo().getMatricula());
            stmt.setDouble(3, aluguer.getValor());
            stmt.setString(4, aluguer.getDataInicio());
            stmt.setString(5, aluguer.getDataFim());
            stmt.executeUpdate();
            System.out.println("Aluguer registrado com sucesso: ID " + aluguer.getId());
        
       } catch (SQLException e) 
       {
            e.printStackTrace();
       }
       
     }

    @Override
    public List<Aluguer> listarHistoricoAluguerVeiculo(String matricula) throws RemoteException 
    {
        
        List<Aluguer> alugueres = new ArrayList<>();
        
        try 
        {
            String sql = "SELECT * FROM Aluguer WHERE matricula_veiculo = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, matricula);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) 
            {
                     // Buscar o cliente pelo ID
            int clienteId = rs.getInt("id_cliente");
            Cliente cliente = clienteService.buscarPorId(clienteId); // Obtém o cliente usando o método buscarPorId


                    // Obter dados do cliente e veículo se necessário
                    Aluguer aluguer = new Aluguer(
                 rs.getInt("id"),
              veiculoService.buscarVeiculoPorMatricula(matricula), // O veículo será buscado posteriormente, se necessário
              cliente, 
              rs.getDouble("valor"),
             rs.getString("data_inicio"),
             rs.getString("data_fim")
                );
            
                    alugueres.add(aluguer);
            }

        } catch (SQLException e) 
        {
            e.printStackTrace();
            System.out.println("Erro ao listar histórico de alugueres para o veículo: " + matricula);
        }
    
        return alugueres;
    }

//    @Override
//    public List<Aluguer> listarHistoricoAluguerCliente(int idCliente) throws RemoteException 
//    {
//        List<Aluguer> alugueres = new ArrayList<>();
//        try 
//        {
//            String sql = "SELECT * FROM Aluguer WHERE id_cliente = ?";
//            PreparedStatement stmt = connection.prepareStatement(sql);
//            stmt.setInt(1, idCliente);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                alugueres.add(new Aluguer(
//                    rs.getInt("id"),
//                    null, // Buscar veículo e cliente posteriormente, se necessário
//                    null,
//                    rs.getDouble("valor"),
//                    rs.getString("data_inicio"),
//                    rs.getString("data_fim")
//                ));
//            }
//        } catch (SQLException e) 
//        {
//            e.printStackTrace();
//        }
//        return alugueres;
//    }
    
}
