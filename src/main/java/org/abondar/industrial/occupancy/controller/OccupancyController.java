package org.abondar.industrial.occupancy.controller;

import org.abondar.industrial.occupancy.data.OccupancyData;
import org.abondar.industrial.occupancy.service.OccupancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/occupancy")
@Validated
public class OccupancyController {

    private final OccupancyService service;

    @Autowired
    public OccupancyController(OccupancyService service) {
        this.service = service;
    }

    @GetMapping(path="/{premiumRooms}/{economyRooms}",produces="application/json")
    public ResponseEntity<OccupancyData> showOccupancy(@PathVariable @Min(0) int premiumRooms,
                                                       @PathVariable @Min(0) int economyRooms) {

        var occupancy = service.calculateOccupancy(premiumRooms, economyRooms);
        return ResponseEntity.ok(occupancy);
    }

}
