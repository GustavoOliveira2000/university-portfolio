/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.repositories;

import com.hospital.monitor.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author gustavooliveira
 */



public interface DeviceRepository extends JpaRepository<Device, Long> 
{
    Device findByIdentifier(String identifier); // Buscar dispositivo pelo identificador único
}