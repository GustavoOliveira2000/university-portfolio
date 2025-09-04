/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package t1_sd.remoto;

import t1_sd.entidades.Aluguer;
import java.rmi.Remote;
import java.rmi.RemoteException ;
import java.util.List;
 
/**
 *
 * @author gustavooliveira
 */

public interface AluguerServiceRemote extends Remote
{
    void registrarAluguer( Aluguer aluger) throws RemoteException ;
    List<Aluguer> listarHistoricoAluguerVeiculo ( String matricula ) throws RemoteException ; 
    //List<Aluguer> listarHistoricoAluguerCliente (int id ) throws RemoteException ;
    
}
