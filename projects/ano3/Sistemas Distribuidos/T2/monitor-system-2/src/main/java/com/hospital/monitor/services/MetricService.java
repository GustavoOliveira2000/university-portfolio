/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.services;

import com.hospital.monitor.repositories.MetricRepository;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author gustavooliveira
 */

@Service
public class MetricService 
{   
       
      private final MetricRepository metricRepository ;
      
      public MetricService (MetricRepository metricRepository)
      {
          this.metricRepository = metricRepository ;
      
      }
      

    public Map<String, Double> getAggregatedMetrics(
            LocalDateTime startDate, LocalDateTime endDate,
            String room, String service, Integer floor, String building) 
    {

        return metricRepository.getAggregatedMetrics(startDate, endDate, room, service, floor, building);
    }
              
                  
    
}
