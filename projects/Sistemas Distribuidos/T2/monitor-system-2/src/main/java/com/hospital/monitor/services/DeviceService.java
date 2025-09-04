/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.services;

import com.hospital.monitor.models.Device;
import com.hospital.monitor.repositories.DeviceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author gustavooliveira
 */

@Service // classe inicializada pelo spring 
public class DeviceService 
{
    private final DeviceRepository deviceRepository ;
    
    public DeviceService ( DeviceRepository deviceRepository)
    {
        this.deviceRepository = deviceRepository ;
    
    }
    

    // Criar um dispositivo
    public Device createDevice(Device device) 
    {
        return deviceRepository.save(device);
    }
    // Listar todos os dispositivos
    public List<Device> getAllDevices() 
    {
        return deviceRepository.findAll();
    }
    
    // encontrar dispositivo por id 
    public Device getDeviceById(Long id)
    {
         return deviceRepository.findById(id).orElse(null);
    }
    
    // Atualizar um dispositivo
    public Device updateDevice (Long id, Device updatedDevice)
    {   
        Device existingDevice = deviceRepository.findById(id).orElse(null);
        if( existingDevice != null )
        {   
            
            existingDevice.setIdentifier(updatedDevice.getIdentifier());
            existingDevice.setRoom(updatedDevice.getRoom());
            existingDevice.setService(updatedDevice.getService());
            existingDevice.setFloor(updatedDevice.getFloor());
            existingDevice.setBuilding(updatedDevice.getBuilding());
            return deviceRepository.save(existingDevice);
        
        }
        
        return null ;
    }

    // deletar o device 
    public void deleteDevice(Long id)
    {
        deviceRepository.deleteById(id);
    }
    
}
