package org.abondar.industrial.occupancy.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OccupancyData {

    private int rooms;

    private BigDecimal price;

}
