/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hospital.monitor.controllers;

import com.hospital.monitor.services.MetricService;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author gustavooliveira
 */

@RestController
@RequestMapping("/api/metrics")
public class MetricController 
{
    
    private final MetricService metricService;
    
    public MetricController (MetricService metricService)
    {
        this.metricService = metricService ;
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Double>> getAggregatedMetrics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String room,
            @RequestParam(required = false) String service,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) String building)
    {
        // Converte as datas ou define as últimas 24 horas
        LocalDateTime start = (startDate != null) ? LocalDateTime.parse(startDate) : LocalDateTime.now().minusDays(1);
        LocalDateTime end = (endDate != null) ? LocalDateTime.parse(endDate) : LocalDateTime.now();

        Map<String, Double> metrics = metricService.getAggregatedMetrics(start, end, room, service, floor, building);

        return ResponseEntity.ok(metrics);
    
    }
}
