
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.cliente;

/**
 *
 * @author gustavooliveira
 */

import java.io.FileInputStream;
import t1_sd.entidades.Veiculo;
import t1_sd.remoto.VeiculoServiceRemote;

import java.rmi.Naming;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class ClienteAdm 
{
    public static void main(String[] args) 
    {
        try 
        {
            
            Properties config = new Properties(); // /Users/gustavooliveira/Desktop/WORKSPACE/SD/carRentailSystem/resorces/
            config.load(new FileInputStream("/Users/gustavooliveira/Desktop/WORKSPACE/SD/T1-46395-SD-MELHORIA/carRentailSystem/src/resorces/configs.properties")); // Carrega o ficheiro
            String rmiRegistryUrl = config.getProperty("rmi.registry.url");
            
            VeiculoServiceRemote veiculoService = (VeiculoServiceRemote) Naming.lookup(rmiRegistryUrl + "/VeiculoService");
            Scanner scanner = new Scanner(System.in);
            int opcao;
            do {
                System.out.println("1. Listar veículos por estado administrativo");
                System.out.println("2. Aprovar veículo");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) 
                {
                    case 1: // Listar veículos por estado administrativo - OK
                        System.out.print("Estado (1 para aprovado, 0 para não aprovado): ");
                        boolean estado = scanner.nextInt() == 1;
                        List<Veiculo> veiculos = veiculoService.listarVeiculosPorEstado(estado);
                        veiculos.forEach(System.out::println);
                        break;

                    case 2: // Aprovar veículo - OK
                        System.out.print("Matrícula do Veículo: ");
                        String matricula = scanner.nextLine();
                        veiculoService.aprovarVeiculo(matricula);
                        System.out.println("Veículo aprovado com sucesso!");
                        break;

                    case 0: // Sair
                        System.out.println("Encerrando...");
                        break;

                    default:
                        System.out.println("Opção inválida!");
                }
            } while (opcao != 0);

        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}