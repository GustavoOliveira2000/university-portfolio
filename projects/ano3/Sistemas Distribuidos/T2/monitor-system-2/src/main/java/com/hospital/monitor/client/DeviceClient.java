/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author gustavooliveira
 */
public class DeviceClient 
{
  // listar, criar, editar e remover devices via RestFul
    
    public void listDevice()
    {
        System.out.println("Listando todos os dispositivos...");
        
        try
        {
           URL url = new URL("http://localhost:8080/api/devices");
           HttpURLConnection connection = (HttpURLConnection) url.openConnection();
           connection.setRequestMethod("GET");
           
           int responseCode = connection.getResponseCode();
           
           if( responseCode == HttpURLConnection.HTTP_OK)
           {
               try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8")))
               {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null){
                        response.append(line.trim());
                    }
                    System.out.println("Dispositivos: " + response);  
               } 
 
           } else {
               System.out.println("Erro ao listar dispositivos. Código de resposta: " + responseCode);
           }
        
        
        }catch(Exception e) {
            System.err.println("Erro ao listar dispositivos: " + e.getMessage());
        }

    }
    
    public void createDevice() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Criar um novo dispositivo:");
        System.out.print("Identifier: ");
        String identifier = scanner.nextLine();
        System.out.print("Room: ");
        String room = scanner.nextLine();
        System.out.print("Service: ");
        String service = scanner.nextLine();
        System.out.print("Floor: ");
        int floor = scanner.nextInt();
        scanner.nextLine(); // Consumir o newline
        System.out.print("Building: ");
        String building = scanner.nextLine();

     // Montar o JSON
        String jsonPayload = String.format(
        "{\"identifier\":\"%s\", \"room\":\"%s\", \"service\":\"%s\", \"floor\":%d, \"building\":\"%s\"}",
        identifier, room, service, floor, building
         );

        try {
            // Configurar a conexão HTTP
            URL url = new URL("http://localhost:8080/api/devices");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Enviar o payload JSON
            try (OutputStream os = connection.getOutputStream()) 
            {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Ler a resposta
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) 
            {
                System.out.println("Dispositivo criado com sucesso!");
            } else 
            {
                System.out.println("Erro ao criar dispositivo. Código de resposta: " + responseCode);
            }
        } catch (Exception e) 
        {
                System.err.println("Erro ao realizar chamada POST: " + e.getMessage());
        }
}
    
    public void editDevice()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Atualizar dispositivo...");
        System.out.println("Device id: ");
        Long id = scanner.nextLong();
        scanner.nextLine(); // Consumir o newline
        
        System.out.print("Identifier: ");
        String identifier = scanner.nextLine();
        System.out.print("Room: ");
        String room = scanner.nextLine();
        System.out.print("Service: ");
        String service = scanner.nextLine();
        System.out.print("Floor: ");
        int floor = scanner.nextInt();
        scanner.nextLine(); // Consumir o newline
        System.out.print("Building: ");
        String building = scanner.nextLine();

        // Montar o JSON
        String jsonPayload = String.format(
        "{\"identifier\":\"%s\", \"room\":\"%s\", \"service\":\"%s\", \"floor\":%d, \"building\":\"%s\"}",
        identifier, room, service, floor, building
         );
        
        try
       {
              // Configurar a conexão HTTP
            URL url = new URL("http://localhost:8080/api/devices/"+ id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            
            // enviar o json
            try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Dispositivo atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar dispositivo. Código de resposta: " + responseCode);
            }    
            
        }
        catch(Exception e){
            System.err.println("Erro ao editar dispositivo: " + e.getMessage());
        }
    }
    
    public void removeDevice()
    {
        Scanner scanner = new Scanner ( System.in);
        System.out.println("Remover dispositivo..."); 
        System.out.println("Device id: ");
        Long id = scanner.nextLong();
        
        try
        {
            URL url = new URL ("http://localhost:8080/api/devices/"+ id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            
            int responseCode = connection.getResponseCode();
            
            if(responseCode == HttpURLConnection.HTTP_NO_CONTENT)
            {
                 System.out.println("Dispositivo removido com sucesso!");            
            } else 
            {
                System.out.println("Erro ao remover dispositivo. Código de resposta: " + responseCode);
            }
     
        
        } catch (Exception e)
        {
            System.err.println("Erro ao remover dispositivo: " + e.getMessage());
        
        }

    }

}
