package org.abondar.industrial.occupancy.data;

import lombok.Data;

@Data
public class Occupancy {

    private OccupancyUsage premiumData;

    private OccupancyUsage economyData;
}
