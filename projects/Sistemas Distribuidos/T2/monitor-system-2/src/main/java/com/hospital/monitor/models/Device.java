/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 *
 * @author gustavooliveira
 */

@Entity
public class Device 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identifier;
    private String room;
    private String service;
    private int floor;
    private String building;

    
    // Getters e Setters
    public Long getId() 
    {
        return id;
    }

    public void setId(Long id) 
    {
        this.id = id;
    }

    public String getIdentifier() 
    {
        return identifier;
    }

    public void setIdentifier(String identifier) 
    {
        this.identifier = identifier;
    }

    public String getRoom() 
    {
        
        return room;
    }

    public void setRoom(String room) 
    {
        this.room = room;
    }
 
    public String getService() 
    {
        return service;
    }

    public void setService(String service) 
    {
        this.service = service;
    }

    public int getFloor() 
    {
        return floor;
    }

    public void setFloor(int floor) 
    {
        this.floor = floor;
    }

    public String getBuilding() 
    {
        return building;
    }

    public void setBuilding(String building) 
    {
        this.building = building;
    }
    
}

