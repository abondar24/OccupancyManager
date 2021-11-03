package org.abondar.industrial.occupancy.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Occupancy {

    @JsonProperty("premium")
    private OccupancyUsage premiumData;

    @JsonProperty("economy")
    private OccupancyUsage economyData;
}
