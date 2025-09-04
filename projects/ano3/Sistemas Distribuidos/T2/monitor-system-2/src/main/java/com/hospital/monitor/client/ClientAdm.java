/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.client;

import java.util.Scanner;

/**
 *
 * @author gustavooliveira
 */

public class ClientAdm 
{
    private final MetricClient metricClient;
    private final DeviceClient deviceClient;
    
    public ClientAdm(MetricClient metricClient,DeviceClient deviceClient )
    {
            this.metricClient = metricClient ;
            this.deviceClient = deviceClient ;
    }
    
    public static void main (String [] args)
    {
        MetricClient metricClient = new MetricClient ();
        DeviceClient deviceClient = new DeviceClient ();
        ClientAdm clientAdm = new ClientAdm(metricClient, deviceClient);
        clientAdm.start();
    }
    
    public void start()
    {
        Scanner scanner = new Scanner(System.in);
        
        while(true)
        {
            System.out.println("\n=== Cliente de Administração ===");
            System.out.println("1. Gerenciar Dispositivos");
            System.out.println("2. Consultar Métricas");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            
            switch(opcao)
            {
                case 1 -> manageDevice();
                case 2 -> consultMetric();
                case 3 -> 
                {        System.out.print("Saindo .. ");
                         System.exit(0);
                
                }
                default -> System.out.print("Opção invalida . ");
            }  
        }
    }
    
    private void manageDevice()
    {   
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== Gerenciamento de Dispositivos ===");
        System.out.println("1. Listar Dispositivos");
        System.out.println("2. Criar Dispositivo");
        System.out.println("3. Atualizar Dispositivo");
        System.out.println("4. Remover Dispositivo");
        System.out.println("5. Voltar");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        
        switch(opcao)
        {
            case 1 -> deviceClient.listDevice();
            case 2 -> deviceClient.createDevice();
            case 3 -> deviceClient.editDevice();
            case 4 -> deviceClient.removeDevice();
            case 5 -> System.out.print(" voltando ... ");
        }
    }
    
    private void consultMetric()
    {
        metricClient.queryMetrics();
    
    }
    
}
