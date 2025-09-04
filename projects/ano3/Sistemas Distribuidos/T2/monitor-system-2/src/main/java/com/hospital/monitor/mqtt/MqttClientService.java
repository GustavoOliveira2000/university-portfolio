/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.monitor.models.Device;
import com.hospital.monitor.models.Metric;
import com.hospital.monitor.repositories.DeviceRepository;
import com.hospital.monitor.repositories.MetricRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

/**
 *
 * @author gustavooliveira
 */

@Service
public class MqttClientService            
{
    private final String brokerUrl = "tcp://broker.mqttdashboard.com:1883"; // Broker público do seu broker HiveMQ
    private final String clientId = "monitor-system-client";
    private final String topic = "ambient-monitor";
    private final DeviceRepository deviceRepository;
    private final MetricRepository metricRepository ;
    private MqttClient mqttClient;
    
    public MqttClientService(DeviceRepository deviceRepository, MetricRepository metricRepository) 
    {    
         this.deviceRepository = deviceRepository;
         this.metricRepository = metricRepository;
        try 
        {
            mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());

            // Configurar as opções de conexão
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true); // Inicia uma nova sessão limpa


            // Conectar ao brokerngue
            System.out.println("Conectando ao broker HiveMQ Cloud: " + brokerUrl);
            mqttClient.connect(options);
            System.out.println("Conectado ao broker HiveMQ Cloud: " + brokerUrl);

             mqttClient.subscribe(topic, (topic, message) -> processMessage(new String(message.getPayload())));

        } catch (MqttException e) 
        {
            System.err.println("Erro ao conectar ao broker MQTT: " + e.getMessage());
        }
    }

    // Válida e pública mensagens no tópico 
    private void processMessage(String payload) 
    {
        try 
         {
                
                System.out.println("ProcessMensage - Mensagem recebida do broker.");
                // Usar ObjectMapper para converter o JSON em um mapa
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> data = objectMapper.readValue(payload, Map.class);

                // Obter os dados do JSON
                String identifier = (String) data.get("id");
                double temperature = (double) data.get("temperature");
                double humidity = (double) data.get("humidity");
                String timestamp = (String) data.get("timestamp");

                // Validar se o dispositivo está registrado
                Device device = deviceRepository.findByIdentifier(identifier);
                if (device == null) 
                {
                    System.out.println("ProcessMensage - Mensagem invalida, Dispositivo não registrado. " + identifier);
                    return; // Ignorar mensagem de dispositivo não registrado
                }

                // Criar e salvar a métrica
                Metric metric = new Metric();
                metric.setDevice(device);
                metric.setTemperature(temperature);
                metric.setHumidity(humidity);
                metric.setTimestamp(LocalDateTime.parse(timestamp));
                metricRepository.save(metric);

                System.out.println("ProcessMensage - Mensagem processada e armazenada em persistencia, identificador: " + identifier);

            } catch (Exception e) 
            {
            System.err.println("Erro ao processar mensagem MQTT: " + e.getMessage());
            }
        }
    
public void publishMessage(String payload) 
{
    try 
    {
        // Publicar a mensagem no tópico
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(2); // Qualidade de Serviço (QoS)
        mqttClient.publish(topic, message);
        System.out.println("Publish Mensage - Mensagem enviada para servidor broker - topico : " + topic + ": " + payload);
    
    } catch (MqttException e) 
    {
        System.err.println("Erro ao publicar mensagem: " + e.getMessage());
    }
}
    
}
