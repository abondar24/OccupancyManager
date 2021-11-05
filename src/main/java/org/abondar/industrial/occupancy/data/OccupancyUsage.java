package org.abondar.industrial.occupancy.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OccupancyUsage {

    private int rooms;

    private BigDecimal price;

}
