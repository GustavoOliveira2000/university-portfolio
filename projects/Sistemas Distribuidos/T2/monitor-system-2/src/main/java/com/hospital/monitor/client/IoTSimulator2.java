/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.client;

import com.hospital.monitor.mqtt.MqttClientService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author gustavooliveira
 */
@Component
public class IoTSimulator2 
{

    private final MqttClientService mqttClientService;
    private final Random random = new Random();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public IoTSimulator2(MqttClientService mqttClientService) 
    {
        this.mqttClientService = mqttClientService; // inscrever ao broker
    }

    // Método para gerar e publicar mensagens periodicamente
    @Scheduled(fixedRate = 180000) // Enviar mensagens a cada 180 segundos
    public void simulateDeviceData() 
    {
        String[] deviceIds = {"device1234", "device1235", "device789", "device555" };
        String id = deviceIds[random.nextInt(deviceIds.length)]; // Escolhe um dispositivo aleatório

        double temperature = 20 + (5 * random.nextDouble()); // Temperatura entre 20 e 25
        double humidity = 50 + (10 * random.nextDouble());  // Umidade entre 50% e 60%
        String timestamp = LocalDateTime.now().format(formatter); // Timestamp atual

        // Criar o payload em JSON
        String payload = String.format
        (
        Locale.US,
        "{\"id\":\"%s\", \"temperature\":%.2f, \"humidity\":%.2f, \"timestamp\":\"%s\"}",
        id, temperature, humidity, timestamp
        );

        // Publicar no broker
        mqttClientService.publishMessage(payload);

        System.out.println("Simulador enviou mensagem: " + id);
    }
}