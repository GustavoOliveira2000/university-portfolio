/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.hospital.monitor.repositories;

import com.hospital.monitor.models.Metric;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author gustavooliveira
 */
public interface MetricRepository extends JpaRepository<Metric, Long> 
{

    @Query("SELECT AVG(m.temperature) AS avgTemperature, AVG(m.humidity) AS avgHumidity " +
           "FROM Metric m " +
           "WHERE (:startDate IS NULL OR m.timestamp >= :startDate) " +
           "AND (:endDate IS NULL OR m.timestamp <= :endDate) " +
           "AND (:room IS NULL OR m.device.room = :room) " +
           "AND (:service IS NULL OR m.device.service = :service) " +
           "AND (:floor IS NULL OR m.device.floor = :floor) " +
           "AND (:building IS NULL OR m.device.building = :building)")
    Map<String, Double> getAggregatedMetrics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("room") String room,
            @Param("service") String service,
            @Param("floor") Integer floor,
            @Param("building") String building);

}