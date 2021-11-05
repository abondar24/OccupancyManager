package org.abondar.industrial.occupancy.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Occupancy {

    @JsonProperty("premium")
    private OccupancyData premiumData;

    @JsonProperty("economy")
    private OccupancyData economyData;
}
