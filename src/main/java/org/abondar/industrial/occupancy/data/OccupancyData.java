package org.abondar.industrial.occupancy.data;

import lombok.Data;

@Data
public class OccupancyData {

    private int premiumRooms;

    private int premiumPrice;

    private int economyRooms;

    private int economyPrice;
}
