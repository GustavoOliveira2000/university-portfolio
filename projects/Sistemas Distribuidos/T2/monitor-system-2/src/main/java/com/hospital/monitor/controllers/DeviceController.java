/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.controllers;

import com.hospital.monitor.models.Device;
import com.hospital.monitor.services.DeviceService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author gustavooliveira
 */

@RestController
@RequestMapping("/api/devices")
public class DeviceController 
{
    private final DeviceService deviceService;
    
    public DeviceController (DeviceService deviceService)
    {
        this.deviceService = deviceService ;
    }

    // Criar um novo dispositivo
    @PostMapping
    public ResponseEntity<Device> createDevice(@RequestBody Device device) 
    {
        System.out.println("Device adicionado ao sistema : " + device.getIdentifier() );
        return ResponseEntity.ok(deviceService.createDevice(device));
    }
    
    // Listar todos os dispositivos
    @GetMapping
    public ResponseEntity<List<Device>> getAllDevices() 
    {
        System.out.println("GET request - listando devices" );
        return ResponseEntity.ok(deviceService.getAllDevices());
    }

    // Buscar um dispositivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Device> getDeviceById(@PathVariable Long id) 
    {
        Device device = deviceService.getDeviceById(id);
        if (device != null) 
        {
            return ResponseEntity.ok(device);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Atualizar um dispositivo
    @PutMapping("/{id}")
    public ResponseEntity<Device> updateDevice(@PathVariable Long id, @RequestBody Device device) 
    {   
        System.out.println("PUT request - device editado: " + deviceService.getDeviceById(id).getIdentifier());
        Device updatedDevice = deviceService.updateDevice(id, device);
        if (updatedDevice != null) 
        {
            return ResponseEntity.ok(updatedDevice);
        }
        return ResponseEntity.notFound().build();
    }

    // Remover um dispositivo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) 
    {
        System.out.println("DELETE request - device remeovido do sistema : " + deviceService.getDeviceById(id).getIdentifier());
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
    
}
