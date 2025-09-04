/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;


/**
 *
 * @author gustavooliveira
 */

@Entity
public class Metric 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    private double temperature;
    private double humidity;
    private LocalDateTime timestamp;

    // Getters e Setters
    public Long getId() 
    {
        return id;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Device getDevice() 
    {
        return device;
    }

    public void setDevice(Device device) 
    {
        this.device = device;
    }

    public double getTemperature() 
    {
        return temperature;
    }

    public void setTemperature(double temperature) 
    {
        this.temperature = temperature;
    }

    public double getHumidity() 
    {
        return humidity;
    }

    public void setHumidity(double humidity) 
    {
        this.humidity = humidity;
    }

    public LocalDateTime getTimestamp() 
    {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) 
    {
        this.timestamp = timestamp;
    }
}
