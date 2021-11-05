package org.abondar.industrial.occupancy.controller;

import org.abondar.industrial.occupancy.data.Occupancy;
import org.abondar.industrial.occupancy.service.OccupancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static org.abondar.industrial.occupancy.util.OccupancyUtil.OCCUPANCY_ENDPOINT;
import static org.abondar.industrial.occupancy.util.OccupancyUtil.OCCUPANCY_ROOMS;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(OCCUPANCY_ENDPOINT)
@Validated
public class OccupancyController {

    private final OccupancyService service;

    @Autowired
    public OccupancyController(OccupancyService service) {
        this.service = service;
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Occupancy> showOccupancy(@RequestParam(name="premium") @Min(0) int premiumRooms,
                                                   @RequestParam(name="economy") @Min(0) int economyRooms) {

        var occupancy = service.calculateOccupancy(premiumRooms, economyRooms);
        return ResponseEntity.ok(occupancy);
    }

}
