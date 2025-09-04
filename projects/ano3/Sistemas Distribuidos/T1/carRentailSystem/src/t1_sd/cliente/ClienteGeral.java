/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package t1_sd.cliente;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import t1_sd.entidades.Aluguer;
import t1_sd.entidades.Cliente;
import t1_sd.entidades.Veiculo;
import t1_sd.remoto.AluguerServiceRemote;
import t1_sd.remoto.VeiculoServiceRemote;
import t1_sd.remoto.ClienteServiceRemote;
import java.rmi.Naming;

/**
 *
 * @author gustavooliveira
 */

public class ClienteGeral 
{
    public static void main(String[] args)
    {
        try 
        {
            
            Properties config = new Properties(); // /Users/gustavooliveira/Desktop/WORKSPACE/SD/carRentailSystem/resorces/
            config.load(new FileInputStream("/Users/gustavooliveira/Desktop/WORKSPACE/SD/T1-46395-SD-MELHORIA/carRentailSystem/src/resorces/configs.properties")); // Carrega o ficheiro
            String rmiRegistryUrl = config.getProperty("rmi.registry.url");
            

           
            VeiculoServiceRemote veiculoService = (VeiculoServiceRemote) Naming.lookup(rmiRegistryUrl + "/VeiculoService");
            ClienteServiceRemote clienteService = (ClienteServiceRemote) Naming.lookup(rmiRegistryUrl + "/ClienteService");
            AluguerServiceRemote aluguerService = (AluguerServiceRemote) Naming.lookup(rmiRegistryUrl + "/AluguerService");
            System.out.println("Conectdado a porta 1099 com sucesso");
            

            Scanner scanner = new Scanner(System.in);
            int opcao;
            do 
            {
                System.out.println("1. Registrar novo veículo");
                System.out.println("2. Registrar novo cliente");
                System.out.println("3. Registrar novo aluguer");
                System.out.println("4. Listar veículos disponíveis");
                System.out.println("5. Listar localizações de veículos alugados");
                System.out.println("6. Consultar histórico de aluguer de um veículo");
                System.out.println("7. Listar os Veiculos");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcao) 
                {
                    case 1: // Registrar novo veículo  - OK
                        System.out.print("Matrícula: ");
                        String matricula = scanner.nextLine();
                        System.out.print("Modelo: ");
                        String modelo = scanner.nextLine();
                        System.out.print("Tipo: ");
                        String tipo = scanner.nextLine();
                        System.out.print("Localização: ");
                        String localizacao = scanner.nextLine();
                        Veiculo veiculo = new Veiculo(matricula, modelo, tipo, localizacao, false, true);
                        veiculoService.adicionarVeiculo(veiculo);
                        System.out.println("Veículo registrado com sucesso!");
                        break;

                    case 2: // Registrar novo cliente  - Ok 
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Documento: ");
                        String documento = scanner.nextLine();
                        System.out.print("Contato: ");
                        String contato = scanner.nextLine();
                        Cliente cliente = new Cliente(0, nome, documento, contato);
                        clienteService.registrarCliente(cliente);
                        System.out.println("Cliente registrado com sucesso!");
                        break;

                    case 3: // registrar aluguer - testar esse método com especial atenção,sq, será necessária alteração dete
                            // na data base do aluguer...
                        System.out.print("ID do Cliente: ");
                        int clienteId = scanner.nextInt(); 
                        scanner.nextLine();
                        System.out.print("Matrícula do Veículo: ");
                        String veiculoMatricula = scanner.nextLine();
                        System.out.print("Valor do Aluguer: ");
                        double valor = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Data de Início (DD-MM-YYYY): ");
                        String dataInicio = scanner.nextLine();
                        System.out.print("Data de Fim (DD-MM-YYYY): ");
                        String dataFim = scanner.nextLine();
                        Aluguer aluguer = new Aluguer(0, veiculoService.buscarVeiculoPorMatricula(veiculoMatricula), clienteService.buscarPorId(clienteId), valor, dataFim, dataInicio);
                        aluguerService.registrarAluguer(aluguer);
                        System.out.println("Aluguer registrado com sucesso!");
                        break;

                    case 4: // Listar veículos disponíveis -  OK
                        System.out.print("Localização (ou deixe vazio): ");
                        String filtroLocalizacao = scanner.nextLine();
                        System.out.print("Tipo (ou deixe vazio): ");
                        String filtroTipo = scanner.nextLine();
                        List<Veiculo> disponiveis = veiculoService.listarVeiculosDisponiveis(
                                filtroLocalizacao.isEmpty() ? null : filtroLocalizacao,
                                filtroTipo.isEmpty() ? null : filtroTipo
                        );
                        disponiveis.forEach(System.out::println);
                        break;

                    case 5: // Listar localizações de veículos alugados  - OK
                        List<String> localizacoes = veiculoService.listarLocalizacoesDeVeiculosAlugados();
                        localizacoes.forEach(System.out::println);
                        break;

                    case 6: // Consultar histórico de aluguer de um veículo - OK
                        System.out.print("Matrícula do Veículo: ");
                        String histMatricula = scanner.nextLine();
                        List<Aluguer> historico = aluguerService.listarHistoricoAluguerVeiculo(histMatricula);
                        historico.forEach(System.out::println);
                        break;
                    case 7: // Listar veiculos   - OK
                        List<Veiculo> veiculosRegistrados = veiculoService.listVeiculos();
                        veiculosRegistrados.forEach(System.out::println);
                        break;
                     case 8: // Listar CLIENTES   
                        List<Cliente> clientesRegistrados = clienteService.listarClientes();
                        clientesRegistrados.forEach(System.out::println);
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
