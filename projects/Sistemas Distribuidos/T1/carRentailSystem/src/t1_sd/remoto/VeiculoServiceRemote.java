/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package t1_sd.remoto;

import t1_sd.entidades.Veiculo;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author gustavooliveira
 */
public interface VeiculoServiceRemote extends Remote
{   
    void adicionarVeiculo(Veiculo veiculo) throws RemoteException;
    List<Veiculo> listarVeiculosDisponiveis(String localizacao, String tipo) throws RemoteException; // com filtros
    List<String> listarLocalizacoesDeVeiculosAlugados() throws RemoteException; // novo método
    void aprovarVeiculo(String matricula) throws RemoteException;
    List<Veiculo> listarVeiculosPorEstado(boolean aprovado) throws RemoteException;
    Veiculo buscarVeiculoPorMatricula(String matricula) throws RemoteException ;
    public List<Veiculo> listVeiculos() throws RemoteException  ;
    public void removerVeiculo(String matricula) throws RemoteException ;
}
