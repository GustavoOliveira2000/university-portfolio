/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package t1_sd.remoto;
import t1_sd.entidades.Cliente;
import java.rmi.Remote ;
import java.rmi.RemoteException ;
import java.util.List;
/**
 *
 * @author gustavooliveira
 */
public interface ClienteServiceRemote extends Remote // declaração dos métodos a serem utilizados remotamente
{

   void registrarCliente( Cliente client ) throws RemoteException ;
   Cliente buscarPorId( int id ) throws RemoteException ; 
   List<Cliente> listarClientes() throws RemoteException ; 
   
}
