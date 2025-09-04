/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.servidor;
import java.io.FileInputStream;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Properties;
import t1_sd.database.PostgresConnect;
import java.sql.Connection;

/**
 *
 * @author gustavooliveira
 */
public class Servidor 
{   
        public static void main(String [] args) 
        {
            try
            {
                Properties config = new Properties(); // t1_sd/
                config.load(new FileInputStream("/Users/gustavooliveira/Desktop/WORKSPACE/SD/T1-46395-SD-MELHORIA/carRentailSystem/src/resorces/configs.properties")); // Carrega o ficheiro
                String rmiRegistryUrl = config.getProperty("rmi.registry.url");
                
                // Inicializar conexão com o banco de dados
                PostgresConnect pgConnect = new PostgresConnect("/Users/gustavooliveira/Desktop/WORKSPACE/SD/T1-46395-SD-MELHORIA/carRentailSystem/src/resorces/configs.properties");
                pgConnect.connect();
                Connection connection = pgConnect.getConnection();
                
                
                LocateRegistry.createRegistry(1099);
                
                VeiculosServiceImpl veiculoService = new VeiculosServiceImpl(pgConnect);
                Naming.rebind(rmiRegistryUrl+"/VeiculoService", veiculoService);
                
                ClienteServiceImpl clienteService = new ClienteServiceImpl(pgConnect);
                Naming.rebind(rmiRegistryUrl+ "/ClienteService", clienteService);
                
                AluguerServiceImpl aluguerService = new AluguerServiceImpl(pgConnect,veiculoService, clienteService);
                Naming.rebind(rmiRegistryUrl + "/AluguerService", aluguerService);
                
                
                System.out.println("Servidor RMI conectado a porta 1099.");
                
            } catch (Exception e)
            {
                    e.printStackTrace();
            
            }
            
        
        }
    
}
