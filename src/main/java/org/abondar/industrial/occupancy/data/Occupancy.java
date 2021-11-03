package org.abondar.industrial.occupancy.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Occupancy {

    private OccupancyUsage premiumData;

    private OccupancyUsage economyData;
}
