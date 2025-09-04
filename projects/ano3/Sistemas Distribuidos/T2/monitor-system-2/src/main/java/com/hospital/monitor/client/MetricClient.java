/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.client;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author gustavooliveira
 */
public class MetricClient 
{
    
    public void queryMetrics() 
    {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Consulta de Métricas ===");
        System.out.print("Start Date (yyyy-MM-ddTHH:mm:ss) ou deixe em branco: ");
        String startDate = scanner.nextLine();

        System.out.print("End Date (yyyy-MM-ddTHH:mm:ss) ou deixe em branco: ");
        String endDate = scanner.nextLine();

        System.out.print("Room (ou deixe em branco): ");
        String room = scanner.nextLine();

        System.out.print("Service (ou deixe em branco): ");
        String service = scanner.nextLine();

        System.out.print("Floor (ou deixe em branco): ");
        String floor = scanner.nextLine();

        System.out.print("Building (ou deixe em branco): ");
        String building = scanner.nextLine();

        try {
            // Construir a URL com os parâmetros opcionais
            StringBuilder urlBuilder = new StringBuilder("http://localhost:8080/api/metrics?");
            if (!startDate.isBlank()) urlBuilder.append("startDate=").append(startDate).append("&");
            if (!endDate.isBlank()) urlBuilder.append("endDate=").append(endDate).append("&");
            if (!room.isBlank()) urlBuilder.append("room=").append(room).append("&");
            if (!service.isBlank()) urlBuilder.append("service=").append(service).append("&");
            if (!floor.isBlank()) urlBuilder.append("floor=").append(floor).append("&");
            if (!building.isBlank()) urlBuilder.append("building=").append(building);

            URL url = new URL(urlBuilder.toString());

            // Conexão HTTP GET
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (Scanner responseScanner = new Scanner(connection.getInputStream())) {
                    System.out.println("\n=== Resultados da Consulta ===");
                    while (responseScanner.hasNextLine()) {
                        System.out.println(responseScanner.nextLine());
                    }
                }
            } else {
                System.out.println("Erro na consulta de métricas. Código de resposta: " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Erro ao consultar métricas: " + e.getMessage());
        }
    }
}
